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
import net.dv8tion.jda.api.entities.User;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

@Name("Guild Banned USer")
@Description("Return all users who are currently banned from a specific guild.")
@Examples("reply with \"This server have %size of banned user of event-guild% banned user!\"")
@Since("1.3")
public class ExprGuildBanned extends SimpleExpression<User> {

	static {
		Skript.registerExpression(ExprGuildBanned.class, User.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] [discord] [guild] ban[ned] user[s] of [the] [guild] %guild%");
	}

	private Expression<Guild> exprGuild;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprGuild = (Expression<Guild>) exprs[0];
		return true;
	}

	@Override
	protected User[] get(final Event e) {
		Guild guild = exprGuild.getSingle(e);
		if (guild == null) return new User[0];
		List<User> user = new ArrayList<>();
		// TODO: 06/07/2021 Add a way to use async effect in expression
		guild.retrieveBanList().complete().forEach(ban -> user.add(ban.getUser()));
		return user.toArray(new User[0]);
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends User> getReturnType() {
		return User.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "banned user of guild " + exprGuild.toString(e, debug);
	}

}