package info.itsthesky.disky.skript.effects.messages;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky.tools.async.AsyncEffect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.skript.expressions.messages.ExprLastMessage;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.bukkit.event.Event;

@Name("Reply To message")
@Description("Reply TO a message, using the Discord's reply system.")
@Examples({"reply to event-message with \"Hello!\"", "reply to event-message with \"Hello!\" with mentioning false"})
@Since("1.7")
public class EffReplyTo extends AsyncEffect {

    static {
        Skript.registerEffect(EffReplyTo.class,
                "["+ Utils.getPrefixName() +"] reply to [the] [message] %message% (using|with|via) [message] %string/message/messagebuilder/embed% [[with] mention[ing] %-boolean%] [(with|using) %-bot%] [with [(component|row)[s]] %-buttonrows/selectbuilder%] [and store (it|the message) in %-object%]");
    }

    private Expression<UpdatingMessage> exprTarget;
    private Expression<Object> exprMessage;
    private Expression<JDA> exprBot;
    private Variable<?> var;
    private Expression<Boolean> exprMention;
    private Expression<Object> exprRows;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprTarget = (Expression<UpdatingMessage>) exprs[0];
        exprMessage = (Expression<Object>) exprs[1];
        exprMention = (Expression<Boolean>) exprs[2];
        exprBot = (Expression<JDA>) exprs[3];
        exprRows = (Expression<Object>) exprs[4];
        Expression<?> expr = exprs[5];
        if (expr != null && !(expr instanceof Variable)) {
            Skript.error("Cannot store the sent message in a non-variable expression!");
        }
        var = (Variable<?>) expr;
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            UpdatingMessage target = exprTarget.getSingle(e);
            Object msg = exprMessage.getSingle(e);
            JDA bot = Utils.verifyVar(e, exprBot);
            Object[] components = Utils.verifyVars(e, exprRows);
            Message storedMessage = null;
            MessageAction action = null;
            boolean mention = exprMention != null && (exprMention.getSingle(e) != null && exprMention.getSingle(e));
            if (target == null || msg == null) return;

            if (bot != null)
                target = UpdatingMessage.from(bot.getTextChannelById(target.getMessage().getTextChannel().getId()).retrieveMessageById(target.getID()).complete());

            if (msg instanceof Message) action = target.getMessage().reply((Message) msg).mentionRepliedUser(mention);
            if (msg instanceof EmbedBuilder) action = target.getMessage().replyEmbeds(((EmbedBuilder) msg).build()).mentionRepliedUser(mention);
            if (msg instanceof MessageBuilder) action = target.getMessage().reply(((MessageBuilder) msg).build()).mentionRepliedUser(mention);
            if (action == null) action = target.getMessage().reply(msg.toString()).mentionRepliedUser(mention);

            action = Utils.parseComponents(action, components);

            storedMessage = action.complete();
            ExprLastMessage.lastMessage = UpdatingMessage.from(storedMessage);
            if (var != null)
                var.change(e, new Object[] {UpdatingMessage.from(storedMessage)}, Changer.ChangeMode.SET);
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "reply to the message " + exprMessage.toString(e, debug);
    }

}
