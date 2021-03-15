package info.itsthesky.DiSky.skript.expressions.boosts;

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
import info.itsthesky.DiSky.DiSky;
import info.itsthesky.DiSky.tools.Utils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

@Name("Guild Boosters")
@Description("Return all boosters of the guilds")
@Examples("set {_boosters::*} to boosters of event-guild")
@Since("1.2")
public class ExprBoosters extends SimpleExpression<Member> {

	static {
		Skript.registerExpression(ExprBoosters.class, Member.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] [discord] [member] boosters of [the] [guild] %guild%");
	}

	private Expression<Guild> exprGuild;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprGuild = (Expression<Guild>) exprs[0];
		return true;
	}

	@Override
	protected Member[] get(final Event e) {
		Guild guild = exprGuild.getSingle(e);
		if (guild == null) return new Member[0];
		guild.loadMembers();
		return guild.getBoosters().toArray(new Member[0]);
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends Member> getReturnType() {
		return Member.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "boosters of guild " + exprGuild.toString(e, debug);
	}

}