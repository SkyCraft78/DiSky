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
import info.itsthesky.DiSky.managers.BotManager;
import info.itsthesky.DiSky.skript.expressions.messages.ExprLastMessage;
import info.itsthesky.DiSky.tools.DiSkyErrorHandler;
import info.itsthesky.DiSky.tools.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import org.bukkit.event.Event;

@Name("Reply To message")
@Description("Reply TO a message, using the Discord's reply system.")
@Examples("reply to event-message with \"Hello!\"")
@Since("1.7")
public class EffReplyTo extends Effect {

    static {
        Skript.registerEffect(EffReplyTo.class,
                "["+ Utils.getPrefixName() +"] reply to [the] [message] %message% (using|with|via) [message] %string/message/messagebuilder/embed% [[with] mention[ning] %-boolean%] [(with|using) [bot] [(with name|named)] %-string%] [and store (it|the message) in %-object%]");
    }

    private Expression<Message> exprTarget;
    private Expression<Object> exprMessage;
    private Expression<String> exprName;
    private Expression<Object> exprVar;
    private Expression<Boolean> exprMention;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprTarget = (Expression<Message>) exprs[0];
        exprMessage = (Expression<Object>) exprs[1];
        if (exprs.length > 2) exprMention = (Expression<Boolean>) exprs[2];
        if (exprs.length > 3) exprName = (Expression<String>) exprs[3];
        if (exprs.length > 4) exprVar = (Expression<Object>) exprs[4];
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            Message target = exprTarget.getSingle(e);
            Object msg = exprMessage.getSingle(e);
            Message storedMessage = null;
            boolean mention = exprMention != null && (exprMention.getSingle(e) != null && exprMention.getSingle(e));
            if (target == null || msg == null) return;
            if (exprName != null) {
                JDA msgJDA = target.getJDA();
                JDA botJDA = BotManager.getBot(exprName.getSingle(e));
                if (!msgJDA.equals(botJDA)) return;
            }

            if (msg instanceof Message) storedMessage = target.reply((Message) msg).mentionRepliedUser(mention).complete();
            if (msg instanceof EmbedBuilder) storedMessage = target.reply(((EmbedBuilder) msg).build()).mentionRepliedUser(mention).complete();
            if (msg instanceof MessageBuilder) storedMessage = target.reply(((MessageBuilder) msg).build()).mentionRepliedUser(mention).complete();
            if (storedMessage == null) storedMessage = target.reply(msg.toString()).complete();

            ExprLastMessage.lastMessage = storedMessage;
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
