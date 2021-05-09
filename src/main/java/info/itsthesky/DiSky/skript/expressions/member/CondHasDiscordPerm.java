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
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.event.Event;

@Name("Has Discord Permission")
@Description("See if a member has a specific DISCORD permission, in the guild or in a specific channel.")
@Examples("discord command hasPerm <member> <permission>:\n" +
		"\tprefixes: !\n" +
		"\ttrigger:\n" +
		"\t\tif arg-1 has permission arg-2:\n" +
		"\t\t\treply with \"Yes, %discord name of arg-1% have this permission!\"\n" +
		"\t\telse:\n" +
		"\t\t\treply with \"Oh no, %discord name of arg-1% don't have this permission :(\"")
@Since("1.6")
public class CondHasDiscordPerm extends Condition {

	static {
		Skript.registerCondition(CondHasDiscordPerm.class,
				"["+ Utils.getPrefixName() +"] [the] [member] %member% has [discord] permission %permission% [in [channel] %-channel/textchannel%]",
				"["+ Utils.getPrefixName() +"] [the] [member] %member% (has not|hasn't|don't have|doesn't have) [discord] permission %permission% [in [channel] %-channel/textchannel%]"
		);
	}

	private Expression<Member> exprMember;
	private Expression<Permission> exprPerm;
	private Expression<Object> exprChannel;
	private int pattern;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprMember = (Expression<Member>) exprs[0];
		exprPerm = (Expression<Permission>) exprs[1];
		exprChannel = exprs.length == 3 ? null : (Expression<Object>) exprs[2];
		pattern = matchedPattern;
		return true;
	}

	@Override
	public boolean check(Event e) {
		Member member = exprMember.getSingle(e);
		Permission permission = exprPerm.getSingle(e);
		TextChannel channel = null;
		if (exprChannel != null) channel = Utils.checkChannel(exprChannel);

		if (member == null || permission == null) return false;
		if (pattern == 0) {
			if (channel == null) {
				return member.hasPermission(permission);
			} else {
				return member.hasPermission(channel, permission);
			}
		} else {
			if (channel == null) {
				return !member.hasPermission(permission);
			} else {
				return !member.hasPermission(channel, permission);
			}
		}
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "member " + exprMember.toString(e, debug) + " has the permission " + exprPerm.toString(e, debug);
	}

}