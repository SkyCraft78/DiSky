package info.itsthesky.DiSky.skript.effects.messages;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.DiSky.DiSky;
import info.itsthesky.DiSky.managers.BotManager;
import info.itsthesky.DiSky.skript.expressions.messages.ExprLastMessage;
import info.itsthesky.DiSky.tools.DiSkyErrorHandler;
import info.itsthesky.DiSky.tools.Utils;
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
public class EffUploadFile extends Effect {

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
            Object target = exprChannel.getSingle(e);
            Object f = exprFile.getSingle(e);
            @Nullable Object content = exprContent == null ? null : exprContent.getSingle(e);
            if (target == null || f == null) return;
            Message resultMessage;
            MessageChannel channel = null;
            if (exprBot != null && !Utils.areJDASimilar(((GuildChannel) target).getJDA(), exprBot.getSingle(e))) return;

            if (target instanceof GuildChannel && ((GuildChannel) target).getType().equals(ChannelType.TEXT)) {
                channel = (MessageChannel) target;
            } else if (target instanceof Member || target instanceof User) {
                User user = (User) target;
                channel = user.openPrivateChannel().complete();
            }

            if (channel == null) return;

            if (f instanceof BufferedImage) {
                BufferedImage image = (BufferedImage) f;
                byte[] array = getByteArray(image, "png");

                if (content == null) {
                    resultMessage = channel.sendFile(array, "image.png").complete();
                } else {

                    if (content instanceof MessageBuilder) {
                        resultMessage = channel.sendMessage(((MessageBuilder) content).build()).addFile(array, "image.png").complete();
                    } else if (content instanceof EmbedBuilder) {
                        resultMessage = channel.sendMessage(((EmbedBuilder) content).build()).addFile(array, "image.png").complete();
                    } else {
                        resultMessage = channel.sendMessage(content.toString()).addFile(array, "image.png").complete();
                    }
                }
                return;
            }

            String url = f.toString();
            if (Utils.containURL(url)) {

                InputStream stream = getFileFromURL(url);
                String ext = getExtensionFromUrl(url);
                if (stream == null) return;


                if (content == null) {
                    resultMessage = channel.sendFile(stream, "file." + ext).complete();
                } else {

                    if (content instanceof MessageBuilder) {
                        resultMessage = channel.sendMessage(((MessageBuilder) content).build()).addFile(stream, "file." + ext).complete();
                    } else if (content instanceof EmbedBuilder) {
                        resultMessage = channel.sendMessage(((EmbedBuilder) content).build()).addFile(stream, "file." + ext).complete();
                    } else {
                        resultMessage = channel.sendMessage(content.toString()).addFile(stream, "file." + ext).complete();
                    }
                }

            } else {

                File file = new File(url);
                if (!file.exists()) DiSkyErrorHandler.logException(new FileNotFoundException("Can't found the file to send in a channel! (Path: "+file.getPath()+")"));

                if (content == null) {
                    resultMessage = channel.sendFile(file).complete();
                } else {

                    if (content instanceof MessageBuilder) {
                        resultMessage = channel.sendMessage(((MessageBuilder) content).build()).addFile(file).complete();
                    } else if (content instanceof EmbedBuilder) {
                        resultMessage = channel.sendMessage(((EmbedBuilder) content).build()).addFile(file).complete();
                    } else {
                        resultMessage = channel.sendMessage(content.toString()).addFile(file).complete();
                    }
                }
            }

            if (!(exprVar == null) && !(exprVar instanceof Variable)) return;
            Utils.setSkriptVariable((Variable<?>) exprVar, resultMessage, e);
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
