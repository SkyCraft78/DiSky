package info.itsthesky.DiSky.skript.expressions.fromid;

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
import info.itsthesky.DiSky.managers.BotManager;
import info.itsthesky.DiSky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.event.Event;

@Name("Member from ID")
@Description("Return a Member from its id. Can specify a guild, which will take a lot less time.")
@Examples("set {_member} to member with id \"731885527762075648\" in guild with id \"731885527762075648\"")
@Since("1.11")
public class ExprFIDMember extends SimpleExpression<Member> {

	static {
		Skript.registerExpression(ExprFIDMember.class, Member.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] Member with [the] id %string% [in [the] [guild] %guild%]"
		);
	}

	private Expression<String> exprID;
	private Expression<Guild> exprGuild;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprID = (Expression<String>) exprs[0];
		if (exprs.length != 1) exprGuild = (Expression<Guild>) exprs[1];
		return true;
	}

	@Override
	protected Member[] get(Event e) {
		String id = exprID.getSingle(e);
		JDA bot = BotManager.getFirstBot();
		if (bot == null || id == null) return new Member[0];
		if (!Utils.isNumeric(id)) return new Member[0];
		if (exprGuild != null && exprGuild.getSingle(e) != null) {
			return new Member[] {exprGuild.getSingle(e).getMemberById(id)};
		} else {
			return new Member[] {Utils.searchMember(bot, id)};
		}
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Member> getReturnType() {
		return Member.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "member with id " + exprID.toString(e, debug);
	}

}