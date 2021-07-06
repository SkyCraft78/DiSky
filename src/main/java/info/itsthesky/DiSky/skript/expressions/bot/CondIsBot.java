package info.itsthesky.disky.skript.expressions.bot;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky.tools.PropertyCondition;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

@Name("Is a user a bot")
@Description("See if a specific user is is a bot or not. Don't forget the 'DISCORD' word in the syntaxes!")
@Examples("if event-user is a discord bot:")
@Since("1.5.2")
public class CondIsBot extends PropertyCondition<Object>  {

	static {
		register(CondIsBot.class,
				"[a] [discord] bot",
				"member/user");
	}

	@Override
	public boolean check(Object entity) {
		User user = entity instanceof User ? (User) entity : ((Member) entity).getUser();
		return user.isBot();
	}

	@Override
	protected String getPropertyName() {
		return "discord bot";
	}


}