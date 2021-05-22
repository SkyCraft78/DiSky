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
                Object entity = exprChannel.getSingle(e);
                Object content = exprMessage.getSingle(e);
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
                if (toSend == null) {
                    Skript.error("[DiSky] Cannot parse or cast the message in the send effect!");
                    return;
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
                    Skript.error("[DiSky] Cannot parse or cast the message channel in the send effect!");
                    return;
                }

                /* 'with bot' verification */
                if (exprBot != null && exprBot.getSingle(e) != null) {
                    JDA bot = exprBot.getSingle(e);
                    if (!Utils.areJDASimilar(channel.getJDA(), bot)) return;
                }

                /* Final send :D */
                storedMessage = channel.sendMessage(toSend.build()).complete(true);

                /* Store section */
                ExprLastMessage.lastMessage = storedMessage;
                if (exprVar == null) return;
                if (!exprVar.getClass().getName().equalsIgnoreCase("ch.njol.skript.lang.Variable")) return;
                Variable var = (Variable) exprVar;
                Utils.setSkriptVariable(var, storedMessage, e);
            } catch (RateLimitedException ex) {
                DiSky.getInstance().getLogger().severe("DiSky tried to get a message, but was rate limited. ("+ex.getMessage()+")");
            }
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "send discord message " + exprMessage.toString(e, debug) + " to channel or user " + exprChannel.toString(e, debug);
    }

}
