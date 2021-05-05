package info.itsthesky.DiSky.skript.expressions.bot;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import info.itsthesky.DiSky.managers.BotManager;
import info.itsthesky.DiSky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import org.bukkit.event.Event;

@Name("Uptime of Bot")
@Description("Return the total uptime of a bot.")
@Examples("set {_uptime} to uptime of bot named \"MyBot\"")
@Since("1.4.2")
public class ExprUptime extends SimpleExpression<Timespan> {

	static {
		Skript.registerExpression(ExprUptime.class, Timespan.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] up[ ]time of [the] %bot%");
	}

	private Expression<JDA> exprBot;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprBot = (Expression<JDA>) exprs[0];
		return true;
	}

	@Override
	protected Timespan[] get(Event e) {
		JDA bot = exprBot.getSingle(e);
		if (bot == null) return new Timespan[0];
		return new Timespan[] {Utils.getUpTime(bot)};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Timespan> getReturnType() {
		return Timespan.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "uptime of " + exprBot.toString(e, debug);
	}

}