package info.itsthesky.disky.skript.expressions.member;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.event.Event;

@Name("Member is a Booster")
@Description("See if a member is boosting a specific guild.")
@Examples("discord command isBooster <member>:\n" +
		"\tprefixes: !\n" +
		"\ttrigger:\n" +
		"\t\tif arg-1 is a booster:\n" +
		"\t\t\treply with \":v: Yay! %mention tag of arg 1% is boosting the server, say a big thanks :heart:\"\n" +
		"\t\telse:\n" +
		"\t\t\treply with \":x: Oh no, %mention tag of arg 1% is not boosting the server! What a bad guy :<\"")
@Since("1.5.3")
public class CondIsBooster extends Condition {

	static {
		Skript.registerCondition(CondIsBooster.class,
				"["+ Utils.getPrefixName() +"] [the] [member] %member% is [a] boost[er]",
				"["+ Utils.getPrefixName() +"] [the] [member] %member% (is not|isn't) [a] boost[er]"
		);
	}

	private Expression<Member> exprMember;
	private int pattern;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprMember = (Expression<Member>) exprs[0];
		pattern = matchedPattern;
		return true;
	}

	@Override
	public boolean check(Event e) {
		Member member = exprMember.getSingle(e);
		if (member == null) return false;
		if (pattern == 0) {
			return member.getRoles().contains(
					member.getGuild().getBoostRole()
			);
		} else {
			return !member.getRoles().contains(
					member.getGuild().getBoostRole()
			);
		}
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "member " + exprMember.toString(e, debug) + " is a booster";
	}

}