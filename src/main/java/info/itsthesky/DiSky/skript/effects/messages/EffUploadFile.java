package info.itsthesky.disky.skript.effects.messages;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky.skript.expressions.messages.ExprLastMessage;
import info.itsthesky.disky.tools.AsyncEffect;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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
public class EffUploadFile extends AsyncEffect {

    static {
        String pImage = "[file] %string%";
        if (DiSky.getPluginManager().isPluginEnabled("SkImage")) pImage = "[(file|image)] %string/image%";
        Skript.registerEffect(EffUploadFile.class,
                "["+ Utils.getPrefixName() +"] upload "+pImage+" [with [the] [content] %-string/embed/messagebuilder%] to [the] [(channel|user)] %channel/textchannel/user/member% [with %-bot%] [and store it in %-object%]");
    }

    private Expression<Object> exprFile;
    private Expression<Object> exprChannel;
    private Expression<Object> exprVar;
    private Expression<JDA> exprBot;
    private Expression<Object> exprContent;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprFile = (Expression<Object>) exprs[0];
        exprContent = (Expression<Object>) exprs[1];
        exprChannel = (Expression<Object>) exprs[2];
        if (exprs.length == 3) return true;
        exprBot = (Expression<JDA>) exprs[3];
        if (exprs.length == 4) return true;
        exprVar = (Expression<Object>) exprs[4];
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            Object entity = exprChannel.getSingle(e);
            Object content = exprContent.getSingle(e);
            Object f = exprFile.getSingle(e);
            if (entity == null || content == null) return;
            Message storedMessage;

            /* Message cast */
            MessageBuilder toSend = null;
            switch (content.getClass().getSimpleName()) {
                case "EmbedBuilder":
                    toSend = new MessageBuilder().setEmbed(((EmbedBuilder) content).build());
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
            if (channel == null) {
                Skript.error("[DiSky] Cannot parse or cast the message channel in the upload effect!");
                return;
            }

            /* 'with bot' verification */
            if (exprBot != null && exprBot.getSingle(e) != null) {
                JDA bot = exprBot.getSingle(e);
                if (!Utils.areJDASimilar(channel.getJDA(), bot)) return;
            }

            if (f instanceof BufferedImage) {
                BufferedImage image = (BufferedImage) f;
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                try {
                    ImageIO.write(image, "png", os);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());

                if (toSend == null) {
                    storedMessage = channel.sendFile(is, "image.png").complete();
                } else {
                    storedMessage = channel.sendMessage(toSend.build()).addFile(is, "image.png").complete();
                }
                ExprLastMessage.lastMessage = UpdatingMessage.from(storedMessage);
                if (exprVar == null) return;
                Utils.setSkriptVariable((Variable<?>) exprVar, UpdatingMessage.from(storedMessage), e);
                return;
            }

            String url = f.toString();
            if (Utils.containURL(url)) {

                InputStream stream = getFileFromURL(url);
                String ext = getExtensionFromUrl(url);
                if (stream == null) return;


                if (toSend == null) {
                    storedMessage = channel.sendFile(stream, "file." + ext).complete();
                } else {
                    storedMessage = channel.sendMessage(toSend.build()).addFile(stream, "file." + ext).complete();
                }

            } else {

                File file = new File(url);
                if (!file.exists()) DiSkyErrorHandler.logException(new FileNotFoundException("Can't found the file to send in a channel! (Path: "+file.getPath()+")"));

                if (toSend == null) {
                    storedMessage = channel.sendFile(file).complete();
                } else {
                    storedMessage = channel.sendMessage(toSend.build()).addFile(file).complete();
                }
            }
            ExprLastMessage.lastMessage = UpdatingMessage.from(storedMessage);
            if (exprVar == null) return;
            Utils.setSkriptVariable((Variable<?>) exprVar, UpdatingMessage.from(storedMessage), e);

        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "upload " + exprFile.toString(e, debug) + " in channel or to user " + exprChannel.toString(e, debug);
    }

    public static InputStream getFileFromURL(String url) {
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/4.77");
            return connection.getInputStream();
        } catch (MalformedURLException ex) {
            DiSkyErrorHandler.logException(new MalformedURLException("DiSky tried to load an URL, but this one was malformed."));
        } catch (IOException | IllegalArgumentException ignored) { }
        return null;
    }

    public static String getExtensionFromUrl(String s) {
        return s.substring(s.lastIndexOf("."));
    }

    public static byte[] getByteArray(BufferedImage bi, String format) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, format, stream);
        } catch (IOException e) {
            Skript.error("Can't convert an Image (from skImage addon) to send it in Discord!");
        }
        return stream.toByteArray();

    }
}
