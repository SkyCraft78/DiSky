package info.itsthesky.DiSky.skript.expressions.bot;

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
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;

@Name("Guilds of Bot")
@Description("Return all guilds of a specific guild.")
@Examples("set {_guilds::*} to guilds of bot named \"MyBot\"")
@Since("1.8.1")
public class ExprGuilds extends SimpleExpression<Guild> {

	static {
		Skript.registerExpression(ExprGuilds.class, Guild.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] guilds [instance] of [the] [bot] [(named|with name)] %string/bot%");
	}

	private Expression<Object> exprName;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprName = (Expression<Object>) exprs[0];
		return true;
	}

	@Override
	protected Guild[] get(Event e) {
		Object entity = exprName.getSingle(e);
		if (entity == null) return new Guild[0];
		if (entity instanceof JDA) {
			return ((JDA) entity).getGuilds().toArray(new Guild[0]);
		} else {
			JDA bot = BotManager.getBot(entity.toString());
			if (bot == null) return new Guild[0];
			return bot.getGuilds().toArray(new Guild[0]);
		}
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends Guild> getReturnType() {
		return Guild.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "guilds of bot named" + exprName.toString(e, debug);
	}

}