package info.itsthesky.disky.skript.expressions.emote;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.Emote;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

@Name("Emote is Animated")
@Description("Check if an emote is animated or not.")
@Examples("if event-emote is animated:")
@Since("1.13")
public class CondIsAnimated extends Condition {

	static {
		Skript.registerCondition(CondIsAnimated.class,
				"["+ Utils.getPrefixName() +"] [the] [emote] %emote% is animated",
				"["+ Utils.getPrefixName() +"] [the] [emote] %emote% (is not|isn't) animated"
		);
	}

	private Expression<Emote> exprEmote;
	private int pattern;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprEmote = (Expression<Emote>) exprs[0];
		pattern = matchedPattern;
		return true;
	}

	@Override
	public boolean check(Event e) {
		Emote emote = exprEmote.getSingle(e);
		if (emote == null) return false;
		if (pattern == 0) {
			return emote.isAnimated();
		} else {
			return !emote.isAnimated();
		}
	}

	@Override
	public @NotNull String toString(Event e, boolean debug) {
		return "emote " + exprEmote.toString(e, debug) + " is animated";
	}

}