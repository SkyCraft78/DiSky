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
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;

import java.io.IOException;
import java.net.URL;

@Name("Upload File")
@Description("Upload a file from an URL to a channel or a private user.")
@Examples("upload file \"https://ih1.redbubble.net/image.696592591.0364/flat,750x,075,f-pad,750x1000,f8f8f8.u2.jpg\" to event-channel")
@Since("1.4")
public class EffUploadFile extends Effect {

    static {
        Skript.registerEffect(EffUploadFile.class,
                "["+ Utils.getPrefixName() +"] upload [file] %string% to [the] [(channel|user)] %channel/textchannel/user/member% [with bot [(named|with name)] %-string%] [and store it in %-object%]");
    }

    private Expression<String> exprURL;
    private Expression<Object> exprChannel;
    private Expression<Object> exprVar;
    private Expression<String> exprName;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprURL = (Expression<String>) exprs[0];
        exprChannel = (Expression<Object>) exprs[1];
        if (exprs.length == 2) return true;
        exprName = (Expression<String>) exprs[2];
        if (exprs.length == 3) return true;
        exprVar = (Expression<Object>) exprs[3];
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            Object channel = exprChannel.getSingle(e);
            String url = exprURL.getSingle(e);
            if (channel == null || url == null) return;

            Message storedMessage = null;
            String ext = url.substring(url.lastIndexOf("."));

            TextChannel channel1 = null;
            if (channel1 instanceof TextChannel) channel1 = (TextChannel) channel;
            if (channel instanceof GuildChannel && ((GuildChannel) channel).getType().equals(ChannelType.TEXT)) channel1 = (TextChannel) channel;

            if (channel instanceof User || channel instanceof Member) {
                User user;
                if (channel instanceof Member) {
                    user = ((Member) channel).getUser();
                } else {
                    user = (User) channel;
                }
                URL co = null;
                try {
                    co = new URL(url);
                    URL finalCo = co;
                    storedMessage = user.openPrivateChannel()
                            .flatMap(channel2 -> {
                                try {
                                    return channel2.sendFile(finalCo.openStream(), "file."+ ext);
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
                                return null;
                            })
                            .complete();
                } catch (IOException malformedURLException) {
                    malformedURLException.printStackTrace();
                }
                ExprLastMessage.lastMessage = storedMessage;
                if (exprVar == null) return;
                if (!exprVar.getClass().getName().equalsIgnoreCase("ch.njol.skript.lang.Variable")) return;
                Variable var = (Variable) exprVar;
                Utils.setSkriptVariable(var, storedMessage, e);
            } else return;

            if (channel1 == null) return;
            JDA bot = null;
            if (exprName != null) {
                bot = BotManager.getBot(exprName.getSingle(e));
            } else {
                bot = channel1.getJDA();
            }
            if (bot == null) return;

            TextChannel channel2 = bot.getTextChannelById(channel1.getId());
            if (channel2 == null) {
                DiSky.getInstance().getLogger().severe("Cannot get the right text channel with id '"+channel1.getId()+"'!");
                return;
            }

            URL co = null;
            try {
                co = new URL(url);
                URL finalCo = co;
                try {
                    channel2.sendFile(finalCo.openStream(), "file.png").queue();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } catch (IOException malformedURLException) {
                malformedURLException.printStackTrace();
            }

            ExprLastMessage.lastMessage = storedMessage;
            if (exprVar == null) return;
            if (!exprVar.getClass().getName().equalsIgnoreCase("ch.njol.skript.lang.Variable")) return;
            Variable var = (Variable) exprVar;
            Utils.setSkriptVariable(var, storedMessage, e);
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "upload file from url " + exprURL.toString(e, debug) + " in channel or to user " + exprChannel.toString(e, debug);
    }

}
