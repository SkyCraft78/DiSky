package info.itsthesky.disky.skript.expressions.member;

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
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.event.Event;

@Name("User in Guild")
@Description("Get the member type of a user in a specific guild.")
@Examples("event-user in event-guild")
@Since("1.14")
public class ExprUserInGuild extends SimpleExpression<Member> {

	static {
		Skript.registerExpression(ExprUserInGuild.class, Member.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] [discord] %user% in [the] [guild] %guild%");
	}

	private Expression<User> exprUser;
	private Expression<Guild> exprGuild;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprUser = (Expression<User>) exprs[0];
		exprGuild = (Expression<Guild>) exprs[1];
		return true;
	}

	@Override
	protected Member[] get(final Event e) {
		User user = exprUser.getSingle(e);
		Guild guild = exprGuild.getSingle(e);
		if (user == null || guild == null) return new Member[0];
		return new Member[] {guild.getMember(user)};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Member> getReturnType() {
		return Member.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "user " + exprUser.toString(e, debug) + " in guild " + exprGuild.toString(e, debug);
	}

}