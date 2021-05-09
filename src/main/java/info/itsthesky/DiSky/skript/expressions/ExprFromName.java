package info.itsthesky.disky.skript.expressions;

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
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;

@Name("Discord Entity from ID")
@Description("Return a discord entity such as channel, role, category, etc... from its name.\nWe highly DON'T recommend use that method to get entity. Use \"from id\" expression!")
@Examples("set {_channel} to channel named \"epic-channel\"")
@Since("1.5.1")
public class ExprFromName extends SimpleExpression<Object> {

	static {
		Skript.registerExpression(ExprFromName.class, Object.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] text[ ][-][ ]channel (named|with name) %string%",
				"["+ Utils.getPrefixName() +"] voice[ ][-][ ]channel (named|with name) %string%",
				"["+ Utils.getPrefixName() +"] (user|member) (named|with name) %string%",
				"["+ Utils.getPrefixName() +"] (guild|server) (named|with name) %string%",
				"["+ Utils.getPrefixName() +"] [guild] role (named|with name) %string%",
				"["+ Utils.getPrefixName() +"] category (named|with name) %string%"
		);
	}

	private Expression<String> exprID;
	private int pattern;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		pattern = matchedPattern;
		exprID = (Expression<String>) exprs[0];
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
					return new TextChannel[] {bot.getTextChannelsByName(id, true).get(0)};
				case 1:
					return new VoiceChannel[] {bot.getVoiceChannelsByName(id, true).get(0)};
				case 2:
					return new User[] {bot.getUsersByName(id, true).get(0)};
				case 3:
					return new Guild[] {bot.getGuildsByName(id, true).get(0)};
				case 4:
					return new Role[] {bot.getRolesByName(id, true).get(0)};
				case 5:
					return new Category[] {bot.getCategoriesByName(id, true).get(0)};
			}
			return new String[0];
		} catch (Exception ex) {
			DiSky.getInstance().getLogger().severe("DiSky can't retrieve discord entity from name '"+id+"' cause of response error: " + ex.getMessage());
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
		return "discord entity from name " + exprID.toString(e, debug);
	}

}