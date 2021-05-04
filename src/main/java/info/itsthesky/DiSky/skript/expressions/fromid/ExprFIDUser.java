package info.itsthesky.DiSky.skript.expressions.fromid;

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
import info.itsthesky.DiSky.managers.BotManager;
import info.itsthesky.DiSky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.event.Event;

@Name("User from ID")
@Description("Return a user from its id.")
@Examples("set {_usr} to user with id \"731885527762075648\"")
@Since("1.11")
public class ExprFIDUser extends SimpleExpression<User> {

	static {
		Skript.registerExpression(ExprFIDUser.class, User.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] user with [the] id %string%"
		);
	}

	private Expression<String> exprID;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprID = (Expression<String>) exprs[0];
		return true;
	}

	@Override
	protected User[] get(Event e) {
		String id = exprID.getSingle(e);
		JDA bot = BotManager.getFirstBot();
		if (bot == null || id == null) return new User[0];
		if (!Utils.isNumeric(id)) return new User[0];
		return new User[] {bot.getUserById(Long.parseLong(id))};
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
		return "user with id " + exprID.toString(e, debug);
	}

}