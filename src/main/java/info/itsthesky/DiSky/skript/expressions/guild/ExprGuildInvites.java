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
import net.dv8tion.jda.api.entities.Invite;
import org.bukkit.event.Event;

@Name("Guild Invites")
@Description("Return all invites of the guilds")
@Examples("set {_invites::*} to invites of event-guild")
@Since("1.3")
public class ExprGuildInvites extends SimpleExpression<Invite> {

	static {
		Skript.registerExpression(ExprGuildInvites.class, Invite.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] [discord] invite[s] of [the] [guild] %guild%");
	}

	private Expression<Guild> exprGuild;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprGuild = (Expression<Guild>) exprs[0];
		return true;
	}

	@Override
	protected Invite[] get(final Event e) {
		Guild guild = exprGuild.getSingle(e);
		if (guild == null) return new Invite[0];
		return guild.retrieveInvites().complete().toArray(new Invite[0]);
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends Invite> getReturnType() {
		return Invite.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "invites of guild " + exprGuild.toString(e, debug);
	}

}