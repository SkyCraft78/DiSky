package info.itsthesky.disky.skript.expressions.bot;

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
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.event.Event;

@Name("Is a user a bot")
@Description("See if a specific user is is a bot or not. Don't forget the 'DISCORD' word in the syntaxes!")
@Examples("if event-user is a discord bot:")
@Since("1.5.2")
public class CondIsBot extends Condition {

	static {
		Skript.registerCondition(CondIsBot.class,
				"["+ Utils.getPrefixName() +"] %user/member% (is|was) a discord bot",
				"["+ Utils.getPrefixName() +"] %user/member% (is not|isn't) a discord bot");
	}

	private Expression<Object> exprUser;
	private int pattern;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprUser = (Expression<Object>) exprs[0];
		pattern = matchedPattern;
		return true;
	}

	@Override
	public boolean check(Event e) {
		Object entity = exprUser.getSingle(e);
		if (entity == null) return true;
		User user;
		if (entity instanceof User) {
			user = (User) entity;
		} else {
			user = ((Member) entity).getUser();
		}
		if (pattern == 0) {
			return user.isBot();
		} else {
			return !user.isBot();
		}
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "user " + exprUser.toString(e, debug) + " is a discord bot";
	}

}