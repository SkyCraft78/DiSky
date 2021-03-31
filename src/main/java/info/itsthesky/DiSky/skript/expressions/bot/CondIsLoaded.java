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
import org.bukkit.event.Event;

@Name("Is a bot loaded")
@Description("See if a specific bot is loaded on the server or not.")
@Examples("if bot \"name\" is loaded:")
@Since("1.5.2")
public class CondIsLoaded extends Condition {

	static {
		Skript.registerCondition(CondIsLoaded.class,
				"["+ Utils.getPrefixName() +"] bot [(with name|named)] %string% (is|was) (loaded|online) [on the server]",
		"["+ Utils.getPrefixName() +"] bot [(with name|named)] %string% (isn't|is not|wasn't|was not) (loaded|online) [on the server]");
	}

	private Expression<String> exprName;
	private int pattern;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprName = (Expression<String>) exprs[0];
		pattern = matchedPattern;
		return true;
	}

	@Override
	public boolean check(Event e) {
		String name = exprName.getSingle(e);
		if (name == null) return false;
		if (pattern == 0) {
			return BotManager.getBots().containsKey(name);
		} else {
			return !BotManager.getBots().containsKey(name);
		}
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "bot named " + exprName.toString(e, debug) + " is loaded";
	}

}