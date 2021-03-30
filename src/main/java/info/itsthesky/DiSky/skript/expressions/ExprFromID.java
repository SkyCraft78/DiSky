package info.itsthesky.DiSky.skript.expressions;

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
import info.itsthesky.DiSky.DiSky;
import info.itsthesky.DiSky.managers.BotManager;
import info.itsthesky.DiSky.tools.Utils;
import info.itsthesky.DiSky.tools.object.messages.Channel;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import org.bukkit.event.Event;

@Name("Discord Entity from ID")
@Description("Return a discord entity such as channel, role, category, etc... from its id.")
@Examples("set {_channel} to channel with id \"731885527762075648\"")
@Since("1.0")
public class ExprFromID extends SimpleExpression<Object> {

	static {
		Skript.registerExpression(ExprFromID.class, Object.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [text][ ][-][ ]channel with [the] id %string%",
				"["+ Utils.getPrefixName() +"] (user|member) with [the] id %string%",
				"["+ Utils.getPrefixName() +"] (guild|server) with [the] id %string%",
				"["+ Utils.getPrefixName() +"] [guild] role with [the] id %string%",
				"["+ Utils.getPrefixName() +"] message with [the] id %string% in [channel] %channel/textchannel%",
				"["+ Utils.getPrefixName() +"] category with [the] id %string%"
		);
	}

	private Expression<String> exprID;
	private Expression<Object> exprChannel;
	private int pattern;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		pattern = matchedPattern;
		exprID = (Expression<String>) exprs[0];
		if (!(exprs.length == 1)) exprChannel = (Expression<Object>) exprs[1];
		return true;
	}

	@Override
	protected Object[] get(Event e) {
		String id = exprID.getSingle(e);
		JDA bot = BotManager.getFirstBot();
		if (bot == null || id == null) return new Object[0];
		try {
			switch (pattern) {
				case 0:
					return new TextChannel[] {bot.getTextChannelById(Long.parseLong(id))};
				case 1:
					return new User[] {bot.getUserById(Long.parseLong(id))};
				case 2:
					return new Guild[] {bot.getGuildById(Long.parseLong(id))};
				case 3:
					return new Role[] {bot.getRoleById(Long.parseLong(id))};
				case 4:
					TextChannel channel = Utils.checkChannel(exprChannel.getSingle(e));
					if (channel == null) return new String[0];
					return new Message[] {channel.retrieveMessageById(Long.parseLong(id)).complete()};
				case 5:
					return new Category[] {bot.getCategoryById(Long.parseLong(id))};
			}
			return new String[0];
		} catch (ErrorResponseException ex) {
			DiSky.getInstance().getLogger().severe("DiSky can't retrieve discord entity from id '"+id+"' cause of response error: " + ex.getMeaning());
		}
		return new String[0];
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<?> getReturnType() {
		return Object.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "discord entity from id " + exprID.toString(e, debug);
	}

}