package info.itsthesky.disky.skript.expressions.member;

import ch.njol.skript.Skript;
import info.itsthesky.disky.tools.PropertyCondition;
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
import net.dv8tion.jda.api.entities.GuildChannel;
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
		PropertyCondition.register(CondHasDiscordPerm.class,
				PropertyCondition.PropertyType.HAVE,
				"[discord] permission %permissions% [in [channel] %-channel/voicechannel/textchannel%]",
				"member"
		);
	}

	private Expression<Member> exprMember;
	private Expression<Permission> exprPerm;
	private Expression<GuildChannel> exprChannel;
	private int pattern;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprMember = (Expression<Member>) exprs[0];
		exprPerm = (Expression<Permission>) exprs[1];
		exprChannel = (Expression<GuildChannel>) exprs[2];
		pattern = matchedPattern;
		return true;
	}

	@Override
	public boolean check(Event e) {
		Member member = exprMember.getSingle(e);
		Permission[] permissions = exprPerm.getAll(e);
		GuildChannel channel = Utils.verifyVar(e, exprChannel);

		if (member == null || permissions.length == 0) return false;
		if (pattern == 0) {
			if (channel == null) {
				return member.hasPermission(permissions);
			} else {
				return member.hasPermission(channel, permissions);
			}
		} else {
			if (channel == null) {
				return !member.hasPermission(permissions);
			} else {
				return !member.hasPermission(channel, permissions);
			}
		}
	}

	@Override
	public String toString(Event e, boolean debug) {
		return PropertyCondition.toString(
				this,
				PropertyCondition.PropertyType.HAVE,
				e, debug,
				exprMember,
				"discord permission " + exprPerm.toString(e, debug) + (exprChannel == null ? "" : " in channel " + exprChannel.toString(e, debug))
		);
	}

}