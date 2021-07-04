package info.itsthesky.disky.skript.effects.messages;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.effects.Delay;
import ch.njol.skript.lang.*;
import ch.njol.skript.timings.SkriptTimings;
import ch.njol.skript.variables.Variables;
import info.itsthesky.disky.skript.expressions.messages.ExprLastMessage;
import info.itsthesky.disky.tools.AsyncEffect;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

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
        Skript.registerEffect(EffUploadFile.class,
                "["+ Utils.getPrefixName() +"] upload %"+(DiSky.SKIMAGE_INSTALLED ? "string/image" : "string")+"% [(with name|named) %-string%] [with [the] [content] %-string/embed/messagebuilder%] to [the] [(channel|user)] %channel/textchannel/user/member% [with %-bot%] [and store it in %-object%]");
    }

    private Expression<Object> exprFile;
    private Expression<String> exprFileName;
    private Expression<Object> exprChannel;
    private Variable<?> variable;
    private Expression<JDA> exprBot;
    private Expression<Object> exprContent;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprFile = (Expression<Object>) exprs[0];
        exprFileName = (Expression<String>) exprs[1];
        exprContent = (Expression<Object>) exprs[2];
        exprChannel = (Expression<Object>) exprs[3];
        exprBot = (Expression<JDA>) exprs[4];

        Expression<?> var = exprs[5];
        if (var != null && !(var instanceof Variable)) {
            Skript.error("Cannot store the message in a non-variable expression");
            return false;
        } else {
            variable = (Variable<?>) var;
        }
        return true;
    }

    @Override
    protected void execute(Event event) { }

    @Override
    protected @Nullable TriggerItem walk(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, event -> {
            Object entity = exprChannel.getSingle(e);
            Object content = exprContent == null ? null : exprContent.getSingle(e);
            Object f = exprFile.getSingle(e);
            String fileName = exprFileName == null ? "image.png" : (exprFileName.getSingle(e) == null ? "image.png" : exprFileName.getSingle(e));
            if (entity == null) return;

            debug(e, true);

            Delay.addDelayedEvent(e); // Mark this event as delayed
            Object _localVars = null;
            if (DiSky.SkriptUtils.MANAGE_LOCALES)
                _localVars = Variables.removeLocals(e); // Back up local variables
            Object localVars = _localVars;

            if (!Skript.getInstance().isEnabled()) // See https://github.com/SkriptLang/Skript/issues/3702
                return;

            /* Message cast */
            MessageBuilder toSend = null;
            if (content != null)
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
                    channel.sendFile(is, fileName).queue(m -> {
                        // Re-set local variables
                        if (DiSky.SkriptUtils.MANAGE_LOCALES && localVars != null)
                            Variables.setLocalVariables(event, localVars);

                        ExprLastMessage.lastMessage = UpdatingMessage.from(m);
                        if (variable != null) {
                            variable.change(event, new Object[] {UpdatingMessage.from(m)}, Changer.ChangeMode.SET);
                        }

                        if (getNext() != null) {
                            Bukkit.getScheduler().runTask(Skript.getInstance(), () -> { // Walk to next item synchronously
                                Object timing = null;
                                if (SkriptTimings.enabled()) { // getTrigger call is not free, do it only if we must
                                    Trigger trigger = getTrigger();
                                    if (trigger != null) {
                                        timing = SkriptTimings.start(trigger.getDebugLabel());
                                    }
                                }

                                TriggerItem.walk(getNext(), event);

                                if (DiSky.SkriptUtils.MANAGE_LOCALES)
                                    Variables.removeLocals(event); // Clean up local vars, we may be exiting now

                                SkriptTimings.stop(timing); // Stop timing if it was even started
                            });
                        } else {
                            if (DiSky.SkriptUtils.MANAGE_LOCALES)
                                Variables.removeLocals(event);
                        }
                    });
                } else {
                    channel.sendMessage(toSend.build()).addFile(is, fileName).queue(m -> {
                        // Re-set local variables
                        if (DiSky.SkriptUtils.MANAGE_LOCALES && localVars != null)
                            Variables.setLocalVariables(event, localVars);

                        ExprLastMessage.lastMessage = UpdatingMessage.from(m);
                        if (variable != null) {
                            variable.change(event, new Object[] {UpdatingMessage.from(m)}, Changer.ChangeMode.SET);
                        }

                        if (getNext() != null) {
                            Bukkit.getScheduler().runTask(Skript.getInstance(), () -> { // Walk to next item synchronously
                                Object timing = null;
                                if (SkriptTimings.enabled()) { // getTrigger call is not free, do it only if we must
                                    Trigger trigger = getTrigger();
                                    if (trigger != null) {
                                        timing = SkriptTimings.start(trigger.getDebugLabel());
                                    }
                                }

                                TriggerItem.walk(getNext(), event);

                                if (DiSky.SkriptUtils.MANAGE_LOCALES)
                                    Variables.removeLocals(event); // Clean up local vars, we may be exiting now

                                SkriptTimings.stop(timing); // Stop timing if it was even started
                            });
                        } else {
                            if (DiSky.SkriptUtils.MANAGE_LOCALES)
                                Variables.removeLocals(event);
                        }
                    });
                }
                return;
            }

            if (f == null) return;
            String url = f.toString();
            if (Utils.containURL(url)) {

                InputStream stream = getFileFromURL(url);
                String ext = getExtensionFromUrl(url);
                if (stream == null) return;

                if (toSend == null) {
                    channel.sendFile(stream, "file." + ext).queue(m -> {
                        // Re-set local variables
                        if (DiSky.SkriptUtils.MANAGE_LOCALES && localVars != null)
                            Variables.setLocalVariables(event, localVars);

                        ExprLastMessage.lastMessage = UpdatingMessage.from(m);
                        if (variable != null) {
                            variable.change(event, new Object[] {UpdatingMessage.from(m)}, Changer.ChangeMode.SET);
                        }

                        if (getNext() != null) {
                            Bukkit.getScheduler().runTask(Skript.getInstance(), () -> { // Walk to next item synchronously
                                Object timing = null;
                                if (SkriptTimings.enabled()) { // getTrigger call is not free, do it only if we must
                                    Trigger trigger = getTrigger();
                                    if (trigger != null) {
                                        timing = SkriptTimings.start(trigger.getDebugLabel());
                                    }
                                }

                                TriggerItem.walk(getNext(), event);

                                if (DiSky.SkriptUtils.MANAGE_LOCALES)
                                    Variables.removeLocals(event); // Clean up local vars, we may be exiting now

                                SkriptTimings.stop(timing); // Stop timing if it was even started
                            });
                        } else {
                            if (DiSky.SkriptUtils.MANAGE_LOCALES)
                                Variables.removeLocals(event);
                        }
                    });
                } else {
                    channel.sendMessage(toSend.build()).addFile(stream, "file." + ext).queue(m -> {
                        // Re-set local variables
                        if (DiSky.SkriptUtils.MANAGE_LOCALES && localVars != null)
                            Variables.setLocalVariables(event, localVars);

                        ExprLastMessage.lastMessage = UpdatingMessage.from(m);
                        if (variable != null) {
                            variable.change(event, new Object[] {UpdatingMessage.from(m)}, Changer.ChangeMode.SET);
                        }

                        if (getNext() != null) {
                            Bukkit.getScheduler().runTask(Skript.getInstance(), () -> { // Walk to next item synchronously
                                Object timing = null;
                                if (SkriptTimings.enabled()) { // getTrigger call is not free, do it only if we must
                                    Trigger trigger = getTrigger();
                                    if (trigger != null) {
                                        timing = SkriptTimings.start(trigger.getDebugLabel());
                                    }
                                }

                                TriggerItem.walk(getNext(), event);

                                if (DiSky.SkriptUtils.MANAGE_LOCALES)
                                    Variables.removeLocals(event); // Clean up local vars, we may be exiting now

                                SkriptTimings.stop(timing); // Stop timing if it was even started
                            });
                        } else {
                            if (DiSky.SkriptUtils.MANAGE_LOCALES)
                                Variables.removeLocals(event);
                        }
                    });
                }

            } else {

                File file = new File(url);
                if (!file.exists()) DiSkyErrorHandler.logException(new FileNotFoundException("Can't found the file to send in a channel! (Path: "+file.getPath()+")"));

                if (toSend == null) {
                    channel.sendFile(file).queue(m -> {
                        // Re-set local variables
                        if (DiSky.SkriptUtils.MANAGE_LOCALES && localVars != null)
                            Variables.setLocalVariables(event, localVars);

                        ExprLastMessage.lastMessage = UpdatingMessage.from(m);
                        if (variable != null) {
                            variable.change(event, new Object[] {UpdatingMessage.from(m)}, Changer.ChangeMode.SET);
                        }

                        if (getNext() != null) {
                            Bukkit.getScheduler().runTask(Skript.getInstance(), () -> { // Walk to next item synchronously
                                Object timing = null;
                                if (SkriptTimings.enabled()) { // getTrigger call is not free, do it only if we must
                                    Trigger trigger = getTrigger();
                                    if (trigger != null) {
                                        timing = SkriptTimings.start(trigger.getDebugLabel());
                                    }
                                }

                                TriggerItem.walk(getNext(), event);

                                if (DiSky.SkriptUtils.MANAGE_LOCALES)
                                    Variables.removeLocals(event); // Clean up local vars, we may be exiting now

                                SkriptTimings.stop(timing); // Stop timing if it was even started
                            });
                        } else {
                            if (DiSky.SkriptUtils.MANAGE_LOCALES)
                                Variables.removeLocals(event);
                        }
                    });
                } else {
                    channel.sendMessage(toSend.build()).addFile(file).queue(m -> {
                        // Re-set local variables
                        if (DiSky.SkriptUtils.MANAGE_LOCALES && localVars != null)
                            Variables.setLocalVariables(event, localVars);

                        ExprLastMessage.lastMessage = UpdatingMessage.from(m);
                        if (variable != null) {
                            variable.change(event, new Object[] {UpdatingMessage.from(m)}, Changer.ChangeMode.SET);
                        }

                        if (getNext() != null) {
                            Bukkit.getScheduler().runTask(Skript.getInstance(), () -> { // Walk to next item synchronously
                                Object timing = null;
                                if (SkriptTimings.enabled()) { // getTrigger call is not free, do it only if we must
                                    Trigger trigger = getTrigger();
                                    if (trigger != null) {
                                        timing = SkriptTimings.start(trigger.getDebugLabel());
                                    }
                                }

                                TriggerItem.walk(getNext(), event);

                                if (DiSky.SkriptUtils.MANAGE_LOCALES)
                                    Variables.removeLocals(event); // Clean up local vars, we may be exiting now

                                SkriptTimings.stop(timing); // Stop timing if it was even started
                            });
                        } else {
                            if (DiSky.SkriptUtils.MANAGE_LOCALES)
                                Variables.removeLocals(event);
                        }
                    });
                }
            }

        });
        return getNext();
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
