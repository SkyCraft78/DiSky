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
import net.dv8tion.jda.api.entities.GuildChannel;
import org.bukkit.event.Event;

@Name("Channel from ID")
@Description("Return a guild channel from its id.")
@Examples("set {_cha} to channel with id \"731885527762075648\"")
@Since("1.11")
public class ExprFIDChannel extends SimpleExpression<GuildChannel> {

	private static Class<?> aClass;

	static {
		Skript.registerExpression(ExprFIDChannel.class, GuildChannel.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [guild] channel with [the] id %string%"
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
	protected GuildChannel[] get(Event e) {
		String id = exprID.getSingle(e);
		JDA bot = BotManager.getFirstBot();
		if (bot == null || id == null) return new GuildChannel[0];
		if (!Utils.isNumeric(id)) return new GuildChannel[0];
		return new GuildChannel[] {bot.getGuildChannelById(Long.parseLong(id))};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends GuildChannel> getReturnType() {
		return GuildChannel.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "guild channel with id " + exprID.toString(e, debug);
	}

}