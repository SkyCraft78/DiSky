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
import org.bukkit.event.Event;

@Name("Member Join Date")
@Description("Return the date of the joining of member.")
@Examples("join date of event-user")
@Since("1.6")
public class ExprMemberJoinDate extends SimpleExpression<Date> {

	static {
		Skript.registerExpression(ExprMemberJoinDate.class, Date.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [discord] join date of [the] [member] %member%");
	}

	private Expression<Member> exprMember;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprMember = (Expression<Member>) exprs[0];
		return true;
	}

	@Override
	protected Date[] get(final Event e) {
		Member member = exprMember.getSingle(e);
		if (member == null) return new Date[0];
		return new Date[] {new Date(member.getTimeJoined().toInstant().toEpochMilli())};
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
		return "join date of user  " + exprMember.toString(e, debug);
	}

}