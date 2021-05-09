package info.itsthesky.disky.skript.effects.messages;

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
import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.skript.expressions.messages.ExprLastMessage;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import org.bukkit.event.Event;

@Name("Send discord Message")
@Description("Send a message in a specific channel, with a specific bot. Use that syntax only for non-textchannel event.")
@Examples("on load:\n" +
        "\tmake embed:\n" +
        "\t\tset title of embed to \"The bot has been started!\"\n" +
        "\t\tset color of embed to green\n" +
        "\t\tset timestamp of embed to now\n" +
        "\tsend last embed to text channel with id \"818182473502294066\"")
@Since("1.0")
public class EffSendMessage extends Effect {

    static {
        Skript.registerEffect(EffSendMessage.class,
                "["+ Utils.getPrefixName() +"] send [message] %string/message/embed/messagebuilder% to [the] [(user|channel)] %user/member/textchannel/channel% [with [the] %-bot%] [and store it in %-object%]");
    }

    private Expression<Object> exprMessage;
    private Expression<Object> exprChannel;
    private Expression<Object> exprVar;
    private Expression<JDA> exprBot;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprMessage = (Expression<Object>) exprs[0];
        exprChannel = (Expression<Object>) exprs[1];
        if (exprs.length == 2) return true;
        exprBot = (Expression<JDA>) exprs[2];
        if (exprs.length == 3) return true;
        exprVar = (Expression<Object>) exprs[3];
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            try {
                Object channel = exprChannel.getSingle(e);
                Object content = exprMessage.getSingle(e);
                if (channel == null || content == null) return;
                Message storedMessage;

                TextChannel channel1 = null;
                if (channel instanceof TextChannel) channel1 = (TextChannel) channel;
                if (channel instanceof GuildChannel && ((GuildChannel) channel).getType().equals(ChannelType.TEXT)) channel1 = (TextChannel) channel;

                if (channel instanceof User || channel instanceof Member) {
                    User user;
                    if (channel instanceof Member) {
                        user = ((Member) channel).getUser();
                    } else {
                        user = (User) channel;
                    }
                    if (content instanceof EmbedBuilder) {
                        storedMessage = user.openPrivateChannel()
                                .flatMap(channel2 -> channel2.sendMessage(((EmbedBuilder) content).build()))
                                .complete(true);
                    } else if (content instanceof MessageBuilder) {
                        storedMessage = user.openPrivateChannel()
                                .flatMap(channel2 -> channel2.sendMessage(((MessageBuilder) content).build()))
                                .complete(true);
                    } else {
                        storedMessage = user.openPrivateChannel()
                                .flatMap(channel2 -> channel2.sendMessage(content.toString()))
                                .complete(true);
                    }
                    ExprLastMessage.lastMessage = storedMessage;
                    if (exprVar == null) return;
                    if (!exprVar.getClass().getName().equalsIgnoreCase("ch.njol.skript.lang.Variable")) return;
                    Variable var = (Variable) exprVar;
                    Utils.setSkriptVariable(var, storedMessage, e);
                }

                if (channel1 == null) return;
                if (!Utils.areJDASimilar(channel1.getJDA(), exprBot == null ? null : exprBot.getSingle(e))) return;

                if (content instanceof EmbedBuilder) {
                    storedMessage = channel1.sendMessage(((EmbedBuilder) content).build()).complete();
                } else if (content instanceof MessageBuilder) {
                    storedMessage = channel1.sendMessage(((MessageBuilder) content).build()).complete();
                } else {
                    storedMessage = channel1.sendMessage(content.toString()).complete();
                }
                ExprLastMessage.lastMessage = storedMessage;
                if (exprVar == null) return;
                if (!exprVar.getClass().getName().equalsIgnoreCase("ch.njol.skript.lang.Variable")) return;
                Variable var = (Variable) exprVar;
                Utils.setSkriptVariable(var, storedMessage, e);
            } catch (RateLimitedException ex) {
                DiSky.getInstance().getLogger().severe("DiSky tried to get a message, but was rate limited.");
            }
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "send discord message " + exprMessage.toString(e, debug) + " to channel or user " + exprChannel.toString(e, debug);
    }

}
