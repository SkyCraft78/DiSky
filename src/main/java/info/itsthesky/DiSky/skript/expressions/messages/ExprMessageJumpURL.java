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

@Name("JumpURL of Message")
@Description("Return the jump URL of a message instance.")
@Examples("set {_url} to discord link of event-message")
@Since("1.0")
public class ExprMessageJumpURL extends SimpleExpression<String> {

	static {
		Skript.registerExpression(ExprMessageJumpURL.class, String.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] (jump[ ]url|link|url) of [the] [message] %message%"
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
	protected String[] get(Event e) {
		UpdatingMessage message = exprMessage.getSingle(e);
		if (message == null) return new String[0];
		return new String[] {message.getMessage().getJumpUrl()};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "jump url of message " + exprMessage.toString(e, debug);
	}

}