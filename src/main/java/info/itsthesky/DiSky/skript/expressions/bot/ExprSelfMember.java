package info.itsthesky.disky.skript.expressions.bot;

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
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.event.Event;

@Name("Self Member of Bot")
@Description("Return the self member of a bot in a specific guild.")
@Examples("set {_member} to self member of bot named \"MyBot\" in event-guild")
@Since("1.4.2")
public class ExprSelfMember extends SimpleExpression<Member> {

	static {
		Skript.registerExpression(ExprSelfMember.class, Member.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] [(gateway|rest)] self member of [the] %bot% in [the] [guild] %guild%");
	}

	private Expression<JDA> exprBot;
	private Expression<Guild> exprGuild;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprBot = (Expression<JDA>) exprs[0];
		exprGuild = (Expression<Guild>) exprs[1];
		return true;
	}

	@Override
	protected Member[] get(Event e) {
		JDA bot = exprBot.getSingle(e);
		Guild guild = exprGuild.getSingle(e);
		if (bot == null || guild == null) return new Member[0];
		return new Member[] {guild.getMember(bot.getSelfUser())};
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
		return "self member of " + exprBot.toString(e, debug) + " in guild " + exprGuild.toString(e, debug);
	}

}