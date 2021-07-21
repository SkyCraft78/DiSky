package info.itsthesky.disky.skript.effects.messages;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.util.Validate;
import info.itsthesky.disky.tools.*;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.tools.async.WaiterEffect;
import info.itsthesky.disky.tools.events.InteractionEvent;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.requests.RestAction;
import org.bukkit.event.Event;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

@Name("Upload File")
@Description("Upload a file from an URL to a channel or a private user. If SkImage is installed, you can also send an image directly.")
@Examples("discord command upload [<text>] [<text>]:\n" +
        "\tprefixes: *\n" +
        "\ttrigger:\n" +
        "\t\tif arg-1 is not set:\n" +
        "\t\t\tupload \"https://media.discordapp.net/attachments/818182473502294072/834832709061967913/image.png?width=1440&height=648\" to event-channel\n" +
        "\t\t\tstop\n" +
        "\t\tif arg-2 is not set:\n" +
        "\t\t\tupload arg-1 to event-channel\n" +
        "\t\t\tstop\n" +
        "\t\tupload arg-1 with content arg-2 to event-channel")
@Since("1.4, 1.10 (added locale files & custom content)")
public class EffUploadFile extends WaiterEffect<UpdatingMessage> {

    static {
        Skript.registerEffect(EffUploadFile.class,
                "["+ Utils.getPrefixName() +"] upload %"+(DiSky.SKIMAGE_INSTALLED ? "string/image" : "string")+"% [(with name|named) %-string%] [with [the] [content] %-string/embed/messagebuilder%] [to [the] [(channel|user)] %-channel/textchannel/user/member%] [with %-bot%] [and store it in %-object%]");
    }

    private Expression<Object> exprFile;
    private Expression<String> exprFileName;
    private Expression<Object> exprChannel;
    private Expression<JDA> exprBot;
    private Expression<Object> exprContent;
    private boolean interaction = false;
    private NodeInformation info;

    @SuppressWarnings("unchecked")
    @Override
    public boolean initEffect(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprFile = (Expression<Object>) exprs[0];
        exprFileName = (Expression<String>) exprs[1];
        exprContent = (Expression<Object>) exprs[2];
        exprChannel = (Expression<Object>) exprs[3];
        exprBot = (Expression<JDA>) exprs[4];

        info = new NodeInformation();

        interaction = Arrays.asList(ScriptLoader.getCurrentEvents()[0].getInterfaces()).contains(InteractionEvent.class);

        if (exprChannel == null && !interaction) {
            Skript.error("Need to specific a channel if you are not using the upload effect in an interaction event!");
            return false;
        }

        Expression<?> var = exprs[5];
        if (var != null && !(var instanceof Variable)) {
            Skript.error("Cannot store the message in a non-variable expression");
            return false;
        } else {
            setChangedVariable((Variable) var);
        }
        return true;
    }

    @Override
    public void runEffect(Event e) {
        Object entity = exprChannel.getSingle(e);
        Object content = exprContent == null ? null : exprContent.getSingle(e);
        Object f = exprFile.getSingle(e);
        String fileName = Utils.verifyVar(e, exprFileName, "image.png");
        if (!interaction && entity == null) return;
        if (f == null) return;

        /* Message cast */
        MessageBuilder toSend = null;
        if (content != null)
            switch (content.getClass().getSimpleName()) {
                case "EmbedBuilder":
                    toSend = new MessageBuilder().setEmbeds(((EmbedBuilder) content).build());
                    break;
                case "String":
                    toSend = new MessageBuilder(content.toString());
                    break;
                case "MessageBuilder":
                    toSend = (MessageBuilder) content;
                    break;
                case "Message":
                    toSend = new MessageBuilder((Message) content);
                    break;
            }

        /* Channel Cast */
        MessageChannel channel = null;
        if (!interaction) {
            switch (entity.getClass().getSimpleName()) {
                case "TextChannel":
                case "TextChannelImpl":
                    channel = (MessageChannel) entity;
                    break;
                case "GuildChannel":
                case "GuildChannelImpl":
                    channel = ((GuildChannel) entity).getType().equals(ChannelType.TEXT) ? (MessageChannel) entity : null;
                    break;
                case "User":
                case "UserImpl":
                    channel = ((User) entity).openPrivateChannel().complete();
                    break;
                case "Member":
                case "MemberImpl":
                    channel = ((Member) entity).getUser().openPrivateChannel().complete();
                    break;
            }
        }
        if (!interaction && channel == null) {
            Skript.error("[DiSky] Cannot parse or cast the message channel in the upload effect!");
            return;
        }

        InputStream stream = convert(f);
        if (stream == null) return;

        RestAction<Message> action;
        Message message;

        // Mean we reply to the interaction with a file
        if (interaction) {
            GenericInteractionCreateEvent event = ((InteractionEvent) e).getInteractionEvent();
            Validate.notNull(event); // Just in case of anything, should not be null lmao

            if (toSend == null) {
                Utils.handleRestAction(
                        event
                                .getHook()
                                .sendFile(stream, fileName),
                        msg -> restart(UpdatingMessage.from(msg)),
                        null
                );
            } else {
                Utils.handleRestAction(
                        event
                                .getHook()
                                .sendMessage(toSend.build())
                                .addFile(stream, fileName),
                        msg -> restart(UpdatingMessage.from(msg)),
                        null
                );
            }

        } else {

            if (toSend == null) {
                Utils.handleRestAction(
                        channel
                                .sendFile(stream, fileName),
                        msg -> restart(UpdatingMessage.from(msg)),
                        null
                );
            } else {
                Utils.handleRestAction(
                        channel
                                .sendMessage(toSend.build())
                                .addFile(stream, fileName),
                        msg -> restart(UpdatingMessage.from(msg)),
                        null
                );
            }

        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "upload " + exprFile.toString(e, debug) + " in channel or to user " + exprChannel.toString(e, debug);
    }

    public InputStream convert(Object entity) {
        if (entity instanceof BufferedImage) return getStreamFromImage((BufferedImage) entity);
        if (Utils.containURL(entity.toString())) {
            return getStreamFromURL(entity.toString());
        } else {
            return getStreamFromFile(new File(entity.toString()));
        }
    }

    public InputStream getStreamFromImage(BufferedImage image) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", os);
        } catch (IOException ex) {
            DiSky.error("Cannot create the InputStream from an image! Error: " + ex.getMessage());
            DiSky.error("Related line: " + info.getDebugLabel());
        }
        return new ByteArrayInputStream(os.toByteArray());
    }

    public InputStream getStreamFromFile(File file) {
        InputStream targetStream = null;
        try {
            targetStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            DiSky.error("The specified file doesn't exist! " + info.getDebugLabel());
        }
        return targetStream;
    }

    public InputStream getStreamFromURL(String url) {
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/4.77");
            return connection.getInputStream();
        } catch (MalformedURLException ex) {
            DiSky.error("DiSky tried to load an URL, but this one was malformed. " + info.getDebugLabel());
        } catch (IOException | IllegalArgumentException ex) {
            DiSky.error("DiSky tried to load an URL, but got an unknown error: " + ex.getMessage());
        }
        return null;
    }

    public String getExtensionFromUrl(String s) {
        return s.substring(s.lastIndexOf("."));
    }

}
