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
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import net.dv8tion.jda.api.interactions.Interaction;
import org.bukkit.event.Event;

@Name("Reply with Message")
@Description("Reply with a message to channel-based events (work with private message too!). You can get the sent message using 'and store it in {_var}' pattern!")
@Examples("reply with \"Hello World :globe_with_meridians:\"")
@Since("1.0")
public class EffReplyWith extends Effect {

    public static MessageChannel LAST_CHANNEL;
    public static Boolean IS_HOOK = false;
    public static Interaction LAST_INTERACTION;

    static {
        Skript.registerEffect(EffReplyWith.class,
                "["+ Utils.getPrefixName() +"] reply with [the] [message] %string/message/messagebuilder/embed% [and store it in %-object%]");
    }

    private Expression<Object> exprMessage;
    private Expression<Object> exprVar;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (exprs.length == 1) {
            exprMessage = (Expression<Object>) exprs[0];
        } else {
            exprMessage = (Expression<Object>) exprs[0];
            exprVar = (Expression<Object>) exprs[1];
        }
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            try {
                Object content = exprMessage.getSingle(e);
                if (content == null) return;

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

                Message storedMessage;
                if (IS_HOOK) {
                    storedMessage = LAST_INTERACTION.getHook().setEphemeral(true).sendMessage(toSend.build()).complete(true);
                    IS_HOOK = false;
                } else {
                    storedMessage = LAST_CHANNEL.sendMessage(toSend.build()).complete(true);
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
        return "reply with message " + exprMessage.toString(e, debug) + exprVar != null ? " and store it in " + exprVar.toString(e, debug) : "";
    }

}
