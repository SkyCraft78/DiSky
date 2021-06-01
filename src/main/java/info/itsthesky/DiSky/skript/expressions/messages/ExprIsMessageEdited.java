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
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.Message;
import org.bukkit.event.Event;

@Name("Is Message Edited")
@Description("See if a message was edited or not.")
@Examples({"if event-message was edited:"
		+ "\treply with \"This message has been edited!\""})
@Since("1.0")
public class ExprIsMessageEdited extends SimpleExpression<Boolean> {

	static {
		Skript.registerExpression(ExprIsMessageEdited.class, Boolean.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] %message% (is|was) edited"
		);
	}

	private Expression<UpdatingMessage> exprMessage;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprMessage = (Expression<UpdatingMessage>) exprs[0];
		return true;
	}

	@Override
	protected Boolean[] get(Event e) {
		UpdatingMessage message = exprMessage.getSingle(e);
		if (message == null) return new Boolean[0];
		return new Boolean[] {message.getMessage().isEdited()};
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
		return "edited state of message " + exprMessage.toString(e, debug);
	}

}