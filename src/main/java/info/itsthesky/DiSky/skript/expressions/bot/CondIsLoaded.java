package info.itsthesky.disky.skript.expressions.bot;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.managers.BotManager;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

@Name("Is a bot loaded")
@Description("See if a specific bot is loaded on the server or not.")
@Examples("if bot \"name\" is loaded:")
@Since("1.5.2")
public class CondIsLoaded extends Condition {

	static {
		Skript.registerCondition(CondIsLoaded.class,
				"["+ Utils.getPrefixName() +"] [bot] [(named|with name)] %string% (is|was) (loaded|online) [on the server]",
		"["+ Utils.getPrefixName() +"] [bot] [(named|with name)] %string% (isn't|is not|wasn't|was not) (loaded|online) [on the server]");
	}

	private Expression<String> exprBot;
	private int pattern;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprBot = (Expression<String>) exprs[0];
		pattern = matchedPattern;
		return true;
	}

	@Override
	public boolean check(Event e) {
		String bot = exprBot.getSingle(e);
		if (bot == null) return false;
		if (pattern == 0) {
			return BotManager.getBot(bot, true) != null;
		} else {
			return BotManager.getBot(bot, true) == null;
		}
	}

	@Override
	public @NotNull String toString(@NotNull Event e, boolean debug) {
		return exprBot.toString(e, debug) + " "+ (pattern == 0 ? " is" : " is not") +" loaded";
	}

}