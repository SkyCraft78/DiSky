package info.itsthesky.disky.skript.expressions.guild;

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

@Name("Public Role of Guild")
@Description("Return the public role (@everyone) of the specific guild.")
@Examples("reply with mention tag of public role of event-guild")
@Since("1.4")
public class ExprPublicRole extends SimpleExpression<Role> {

	static {
		Skript.registerExpression(ExprPublicRole.class, Role.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] [discord] public role of [the] [guild] %guild%");
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
		return new Role[] {guild.getPublicRole()};
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
		return "public role of guild " + exprGuild.toString(e, debug);
	}

}