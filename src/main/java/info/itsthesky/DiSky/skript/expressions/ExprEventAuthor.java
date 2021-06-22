package info.itsthesky.disky.skript.expressions;

import ch.njol.skript.ScriptLoader;
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
import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.managers.BotManager;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.events.LogEvent;
import info.itsthesky.disky.tools.object.ButtonBuilder;
import info.itsthesky.disky.tools.object.Emote;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;

@Name("Event Author")
@Description("The user author in a log-related event, like the user who removed a role from another member.")
@Examples("event log author")
@Since("2.0")
public class ExprEventAuthor extends SimpleExpression<User> {

	static {
		Skript.registerExpression(ExprEventAuthor.class, User.class, ExpressionType.SIMPLE,
				"event-author",
				"event [log] author");
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		if (!(ScriptLoader.getCurrentEvents()[0].getSuperclass() == LogEvent.class)) {
			Skript.error("Cannot use the event author in a no log-related event!");
			return false;
		}
		return true;
	}

	@Override
	protected User[] get(final Event e) {
		if (!(e instanceof LogEvent)) return new User[0];
		return new User[] {((LogEvent) e).getActionAuthor()};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends User> getReturnType() {
		return User.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "event-author";
	}

}