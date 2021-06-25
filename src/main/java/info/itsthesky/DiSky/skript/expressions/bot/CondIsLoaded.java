package info.itsthesky.disky.skript.expressions.bot;

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
		PropertyCondition.register(CondIsLoaded.class,
				PropertyCondition.PropertyType.BE,
				"(loaded|online) [on the server]",
		"string");
	}

	private Expression<String> exprBot;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprBot = (Expression<String>) exprs[0];
		return true;
	}

	@Override
	public boolean check(Event e) {
		String bot = exprBot.getSingle(e);
		if (bot == null) return false;
		return BotManager.getBot(bot, true) != null;
	}

	@Override
	public @NotNull String toString(@NotNull Event e, boolean debug) {
		return PropertyCondition.toString(
				this,
				PropertyCondition.PropertyType.HAVE,
				e, debug,
				exprBot,
				"loaded"
		);
	}

}