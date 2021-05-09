package info.itsthesky.disky.skript.expressions.bot;

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
import net.dv8tion.jda.api.JDA;
import org.bukkit.event.Event;

@Name("Gateway Ping of Bot")
@Description("Return the ping of the link between the bot and the Discord API")
@Examples("set {_ping} to ping of bot named \"MyBot\"")
@Since("1.4.2")
public class ExprPing extends SimpleExpression<Number> {

	static {
		Skript.registerExpression(ExprPing.class, Number.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] [(gateway|rest)] ping of [the] %bot%");
	}

	private Expression<JDA> exprBot;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprBot = (Expression<JDA>) exprs[0];
		return true;
	}

	@Override
	protected Number[] get(Event e) {
		JDA bot = exprBot.getSingle(e);
		if (bot == null) return new Number[0];
		return new Number[] {bot.getGatewayPing()};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "ping of bot named" + exprBot.toString(e, debug);
	}

}