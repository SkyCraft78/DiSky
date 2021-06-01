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
import net.dv8tion.jda.api.entities.User;
import org.bukkit.event.Event;

@Name("Author of Message")
@Description("Return the author (user) of a message instance.")
@Examples("set {_author} to discord author of event-message")
@Since("1.0")
public class ExprMessageAuthor extends SimpleExpression<User> {

	static {
		Skript.registerExpression(ExprMessageAuthor.class, User.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] (user|author) of [the] [message] %message%"
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
	protected User[] get(Event e) {
		UpdatingMessage message = exprMessage.getSingle(e);
		if (message == null) return new User[0];
		return new User[] {message.getMessage().getAuthor()};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends User> getReturnType() {
		return User.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "author of message " + exprMessage.toString(e, debug);
	}

}