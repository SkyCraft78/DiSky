package info.itsthesky.disky.skript.expressions.member;

import info.itsthesky.disky.tools.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.event.Event;

@Name("Has Discord Role")
@Description("See if a member has a specific DISCORD role or not.")
@Examples("discord command role <member> <role>:\n" +
		"\tprefixes: !\n" +
		"\ttrigger:\n" +
		"\t\tif arg-1 has role arg-2:\n" +
		"\t\t\treply with \"%discord name of arg-1% have the role %discord name of arg-2%!\"\n" +
		"\t\telse:\n" +
		"\t\t\treply with \"%discord name of arg-1% doesn't have this role :/\"")
@Since("1.6")
public class CondHasRole extends Condition {

	static {
		PropertyCondition.register(CondHasRole.class, PropertyCondition.PropertyType.HAVE, "[discord] [role] %role%", "member");
	}

	private Expression<Member> exprMember;
	private Expression<Role> exprRole;
	private int pattern;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprMember = (Expression<Member>) exprs[0];
		exprRole = (Expression<Role>) exprs[1];
		pattern = matchedPattern;
		return true;
	}

	@Override
	public boolean check(Event e) {
		Member member = exprMember.getSingle(e);
		Role role = exprRole.getSingle(e);
		if (member == null || role == null) return false;
		if (pattern == 0) {
			return member.getRoles().contains(role);
		} else {
			return !member.getRoles().contains(role);
		}
	}

	@Override
	public String toString(Event e, boolean debug) {
		return PropertyCondition.toString(
				this,
				PropertyCondition.PropertyType.HAVE,
				e, debug,
				exprMember,
				"role " + exprRole.toString(e, debug)
		);
	}

}