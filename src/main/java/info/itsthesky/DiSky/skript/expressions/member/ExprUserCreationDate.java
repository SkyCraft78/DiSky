package info.itsthesky.disky.skript.expressions.member;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Date;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.event.Event;

@Name("User Creation Date")
@Description("Return the date of the user account creation.")
@Examples("creation date of event-user")
@Since("1.6")
public class ExprUserCreationDate extends SimpleExpression<Date> {

	static {
		Skript.registerExpression(ExprUserCreationDate.class, Date.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [discord] creation date of [the] [user] %user/member%");
	}

	private Expression<Object> exprMember;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprMember = (Expression<Object>) exprs[0];
		return true;
	}

	@Override
	protected Date[] get(final Event e) {
		Object entity = exprMember.getSingle(e);
		if (entity == null) return new Date[0];
		User user = null;
		if (entity instanceof Member) {
			user = ((Member) entity).getUser();
		} else if (entity instanceof User) {
			user = (User) entity;
		}
		if (user == null) return new Date[0];
		return new Date[] {new Date(user.getTimeCreated().toInstant().toEpochMilli())};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Date> getReturnType() {
		return Date.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "creation date of user  " + exprMember.toString(e, debug);
	}

}