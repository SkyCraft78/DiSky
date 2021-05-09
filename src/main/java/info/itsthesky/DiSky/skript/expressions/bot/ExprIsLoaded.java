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
import info.itsthesky.disky.managers.BotManager;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import org.bukkit.event.Event;

@Name("Is a bot loaded")
@Description("See if a specific bot is loaded on the server or not.")
@Examples("if bot \"name\" is loaded:")
@Since("1.4")
public class ExprIsLoaded extends SimpleExpression<Boolean> {

	static {
		Skript.registerExpression(ExprIsLoaded.class, Boolean.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [bot] [(with name|named)] %string/bot% (is|was) (loaded|online) [on the server]",
		"["+ Utils.getPrefixName() +"] [bot] [(with name|named)] %string/bot% (isn't|is not|wasn't|was not) (loaded|online) [on the server]");
	}

	private Expression<Object> exprName;
	private int pattern;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprName = (Expression<Object>) exprs[0];
		pattern = matchedPattern;
		return true;
	}

	@Override
	protected Boolean[] get(Event e) {
		Object name = exprName.getSingle(e);
		if (name == null) return new Boolean[0];
		if (name instanceof JDA) {
			return new Boolean[] {true};
		} else {
			if (pattern == 0) {
				return new Boolean[] {BotManager.getBots().containsKey(name.toString())};
			} else {
				return new Boolean[] {!BotManager.getBots().containsKey(name.toString())};
			}
		}
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Boolean> getReturnType() {
		return Boolean.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "bot named " + exprName.toString(e, debug) + " is loaded";
	}

}