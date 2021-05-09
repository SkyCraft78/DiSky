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
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.event.Event;

@Name("Member Highest Role")
@Description("Return the highest role of a member, or none if he doesn't have any roles.")
@Examples("set {_r} to highest role of event-member")
@Since("1.5.3")
public class ExprMemberHighestRole extends SimpleExpression<Role> {

	static {
		Skript.registerExpression(ExprMemberHighestRole.class, Role.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] [discord] (highest|first) role of [the] [member] %member%");
	}

	private Expression<Object> exprMember;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprMember = (Expression<Object>) exprs[0];
		return true;
	}

	@Override
	protected Role[] get(final Event e) {
		Object entity = exprMember.getSingle(e);
		if (!(entity instanceof Member)) return new Role[0];
		Member member = (Member) entity;
		if (member.getRoles().isEmpty()) return new Role[0];
		return new Role[] {member.getRoles().get(0)};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Role> getReturnType() {
		return Role.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "highest role of member " + exprMember.toString(e, debug);
	}

}