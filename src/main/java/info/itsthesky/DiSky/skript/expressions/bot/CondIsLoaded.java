package info.itsthesky.DiSky.skript.expressions.bot;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.DiSky.managers.BotManager;
import info.itsthesky.DiSky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import org.bukkit.event.Event;

@Name("Is a bot loaded")
@Description("See if a specific bot is loaded on the server or not.")
@Examples("if bot \"name\" is loaded:")
@Since("1.5.2")
public class CondIsLoaded extends Condition {

	static {
		Skript.registerCondition(CondIsLoaded.class,
				"["+ Utils.getPrefixName() +"] %bot% (is|was) (loaded|online) [on the server]",
		"["+ Utils.getPrefixName() +"] %bot% (isn't|is not|wasn't|was not) (loaded|online) [on the server]");
	}

	private Expression<JDA> exprBot;
	private int pattern;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprBot = (Expression<JDA>) exprs[0];
		pattern = matchedPattern;
		return true;
	}

	@Override
	public boolean check(Event e) {
		JDA bot = exprBot.getSingle(e);
		if (bot == null) return false;
		if (pattern == 0) {
			return BotManager.getBots().containsKey(BotManager.getNameByJDA(bot));
		} else {
			return !BotManager.getBots().containsKey(BotManager.getNameByJDA(bot));
		}
	}

	@Override
	public String toString(Event e, boolean debug) {
		return exprBot.toString(e, debug) + " is loaded";
	}

}