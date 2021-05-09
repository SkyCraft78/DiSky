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

@Name("Is Message Pinned")
@Description("See if a message is pinned or not.")
@Examples({"if event-message is pinned:"
		+ "\treply with \"Nobody reads pinned messages anyway.\""})
@Since("1.0")
public class ExprIsMessagePinned extends SimpleExpression<Boolean> {

	static {
		Skript.registerExpression(ExprIsMessagePinned.class, Boolean.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] %message% (is|was) pin[ned]"
		);
	}

	private Expression<Message> exprMessage;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprMessage = (Expression<Message>) exprs[0];
		return true;
	}

	@Override
	protected Boolean[] get(Event e) {
		Message message = exprMessage.getSingle(e);
		if (message == null) return new Boolean[0];
		return new Boolean[] {message.isPinned()};
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
		return "pinned state of message " + exprMessage.toString(e, debug);
	}

}