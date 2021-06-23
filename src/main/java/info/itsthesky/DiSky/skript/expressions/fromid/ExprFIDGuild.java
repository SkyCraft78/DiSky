package info.itsthesky.disky.skript.expressions.fromid;

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
import info.itsthesky.disky.managers.BotManager;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;

@Name("Guild from ID")
@Description("Return a guild from its id.")
@Examples("set {_guild} to guild with id \"731885527762075648\"")
@Since("1.11")
public class ExprFIDGuild extends SimpleExpression<Guild> {

	private static Class<?> aClass;

	static {
		Skript.registerExpression(ExprFIDGuild.class, Guild.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] (guild|server) with [the] id %string%"
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
	protected Guild[] get(Event e) {
		String id = exprID.getSingle(e);
		if (id == null) return new Guild[0];
		if (!Utils.isNumeric(id)) return new Guild[0];
		return new Guild[] {BotManager.search(bot -> bot.getGuildById(id))};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Guild> getReturnType() {
		return Guild.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "guild with id " + exprID.toString(e, debug);
	}

}