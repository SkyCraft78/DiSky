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
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;

@Name("Guild Roles")
@Description("Return all roles of the specific guild")
@Examples("reply with \"This server have %size of roles of event-guild% roles!!\"")
@Since("1.6")
public class ExprGuildRoles extends SimpleExpression<Role> {

	static {
		Skript.registerExpression(ExprGuildRoles.class, Role.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] [discord] [guild] role[s] of [the] [guild] %guild%");
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
		return guild.getRoles().toArray(new Role[0]);
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends Role> getReturnType() {
		return Role.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "roles of guild " + exprGuild.toString(e, debug);
	}

}