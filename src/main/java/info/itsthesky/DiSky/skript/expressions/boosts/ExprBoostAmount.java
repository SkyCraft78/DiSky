package info.itsthesky.disky.skript.expressions.boosts;

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
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;

@Name("Guild Boosts Amount")
@Description("Return the boost amount of specific guild")
@Examples("reply with \"This server got %boost amount of event-guild% boosts!\"")
@Since("1.2")
public class ExprBoostAmount extends SimpleExpression<Number> {

	static {
		Skript.registerExpression(ExprBoostAmount.class, Number.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] [discord] boosts amount of [the] [guild] %guild%");
	}

	private Expression<Guild> exprGuild;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprGuild = (Expression<Guild>) exprs[0];
		return true;
	}

	@Override
	protected Number[] get(final Event e) {
		Guild guild = exprGuild.getSingle(e);
		if (guild == null) return new Number[0];
		return new Number[] {guild.getBoostCount()};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "boosts amount of guild " + exprGuild.toString(e, debug);
	}

}