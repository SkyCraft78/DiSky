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
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.event.Event;

@Name("Guild Booster Role")
@Description("Return the booster role of specific guild")
@Examples("reply with \"Say thanks to %mention tag of booster role of event-guild%\"")
@Since("1.2")
public class ExprBoosterRole extends SimpleExpression<Role> {

	static {
		Skript.registerExpression(ExprBoosterRole.class, Role.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] [discord] booster role of [the] [guild] %guild%");
	}

	private Expression<Guild> exprGuild;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprGuild = (Expression<Guild>) exprs[0];
		return true;
	}

	@Override
	protected Role[] get(final Event e) {
		Guild guild = exprGuild.getSingle(e);
		if (guild == null) return new Role[0];
		return new Role[] {guild.getBoostRole()};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Role> getReturnType() {
		return Role.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "booster role of guild " + exprGuild.toString(e, debug);
	}

}