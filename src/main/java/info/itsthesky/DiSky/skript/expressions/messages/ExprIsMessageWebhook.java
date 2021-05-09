package info.itsthesky.disky.skript.expressions.messages;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.entities.Message;
import org.bukkit.event.Event;

@Name("Is Message a Webhook")
@Description("See if a message was edited or not.")
@Examples({"if event-message is a webhook:"
		+ "\treply with \"This message has been edited!\""})
@Since("1.0")
public class ExprIsMessageWebhook extends SimpleExpression<Boolean> {

	static {
		Skript.registerExpression(ExprIsMessageWebhook.class, Boolean.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] %message% (is|was) [a] webhook [message]",
				"["+ Utils.getPrefixName() +"] %message% (isn't|is not|wasn't|was not) [a] webhook [message]"
		);
	}

	private Expression<Message> exprMessage;
	private boolean isNegate;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprMessage = (Expression<Message>) exprs[0];
		isNegate = matchedPattern != 0;
		return true;
	}

	@Override
	protected Boolean[] get(Event e) {
		Message message = exprMessage.getSingle(e);
		if (message == null) return new Boolean[0];
		if (isNegate) {
			return new Boolean[] {!message.isWebhookMessage()};
		} else {
			return new Boolean[] {message.isWebhookMessage()};
		}
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Boolean> getReturnType() {
		return Boolean.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "webhook state of message " + exprMessage.toString(e, debug);
	}

}