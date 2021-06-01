package info.itsthesky.disky.skript.effects.messages;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky.tools.AsyncEffect;
import ch.njol.skript.lang.Effect;
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
import org.bukkit.event.Event;

@Name("Reply To message")
@Description("Reply TO a message, using the Discord's reply system.")
@Examples({"reply to event-message with \"Hello!\"", "reply to event-message with \"Hello!\" with mentioning false"})
@Since("1.7")
public class EffReplyTo extends AsyncEffect {

    static {
        Skript.registerEffect(EffReplyTo.class,
                "["+ Utils.getPrefixName() +"] reply to [the] [message] %message% (using|with|via) [message] %string/message/messagebuilder/embed% [[with] mention[ing] %-boolean%] [(with|using) %-bot%] [and store (it|the message) in %-object%]");
    }

    private Expression<UpdatingMessage> exprTarget;
    private Expression<Object> exprMessage;
    private Expression<JDA> exprBot;
    private Expression<Object> exprVar;
    private Expression<Boolean> exprMention;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprTarget = (Expression<UpdatingMessage>) exprs[0];
        exprMessage = (Expression<Object>) exprs[1];
        if (exprs.length > 2) exprMention = (Expression<Boolean>) exprs[2];
        if (exprs.length > 3) exprBot = (Expression<JDA>) exprs[3];
        if (exprs.length > 4) exprVar = (Expression<Object>) exprs[4];
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            UpdatingMessage target = exprTarget.getSingle(e);
            Object msg = exprMessage.getSingle(e);
            Message storedMessage = null;
            boolean mention = exprMention != null && (exprMention.getSingle(e) != null && exprMention.getSingle(e));
            if (target == null || msg == null) return;
            if (exprBot != null && !Utils.areJDASimilar(target.getMessage().getJDA(), exprBot.getSingle(e))) return;

            if (msg instanceof Message) storedMessage = target.getMessage().reply((Message) msg).mentionRepliedUser(mention).complete();
            if (msg instanceof EmbedBuilder) storedMessage = target.getMessage().reply(((EmbedBuilder) msg).build()).mentionRepliedUser(mention).complete();
            if (msg instanceof MessageBuilder) storedMessage = target.getMessage().reply(((MessageBuilder) msg).build()).mentionRepliedUser(mention).complete();
            if (storedMessage == null) storedMessage = target.getMessage().reply(msg.toString()).mentionRepliedUser(mention).complete();

            ExprLastMessage.lastMessage = UpdatingMessage.from(storedMessage);
            if (exprVar == null) return;
            if (!exprVar.getClass().getName().equalsIgnoreCase("ch.njol.skript.lang.Variable")) return;
            Variable var = (Variable) exprVar;
            Utils.setSkriptVariable(var, storedMessage, e);
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "reply to the message " + exprMessage.toString(e, debug);
    }

}
