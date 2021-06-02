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
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;

@Name("Guild Boost Tier")
@Description("Return the boost tier (1, 2 or 3) of specific guild")
@Examples("reply with \"Guild tier: %boost tier of event-guild%\"")
@Since("1.2")
public class ExprBoostTier extends SimpleExpression<String> {

	static {
		Skript.registerExpression(ExprBoostTier.class, String.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] [discord] boost tier of [the] [guild] %guild%");
	}

	private Expression<Guild> exprGuild;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprGuild = (Expression<Guild>) exprs[0];
		return true;
	}

	@Override
	protected String[] get(final Event e) {
		Guild guild = exprGuild.getSingle(e);
		if (guild == null) return new String[0];
		return new String[] {String.valueOf(guild.getBoostTier().getKey())};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "boost tier of guild " + exprGuild.toString(e, debug);
	}

}