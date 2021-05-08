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
import org.bukkit.event.Event;

@Name("Bot")
@Description("Return  bot object using the bot name.")
@Examples("set {_bot} to bot named \"MyBot\"")
@Since("1.0")
public class ExprJDA extends SimpleExpression<JDA> {

	static {
		Skript.registerExpression(ExprJDA.class, JDA.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] bot [(named|with name)] %string%");
	}

	private Expression<String> exprName;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprName = (Expression<String>) exprs[0];
		return true;
	}

	@Override
	protected JDA[] get(Event e) {
		Object entity = exprName.getSingle(e);
		if (entity == null) return new JDA[0];
		JDA bot = BotManager.getBot(entity.toString());
		if (bot == null) return new JDA[0];
		return new JDA[] {bot};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends JDA> getReturnType() {
		return JDA.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "bot named " + exprName.toString(e, debug);
	}

}