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
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.bukkit.event.Event;

@Name("Voice channel from ID")
@Description("Return a Voice channel from its id.")
@Examples("set {_cha} to voice channel with id \"731885527762075648\"")
@Since("1.11")
public class ExprFIDVoice extends SimpleExpression<VoiceChannel> {

	static {
		Skript.registerExpression(ExprFIDVoice.class, VoiceChannel.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] voice channel with [the] id %string%"
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
	protected VoiceChannel[] get(Event e) {
		String id = exprID.getSingle(e);
		JDA bot = BotManager.getFirstBot();
		if (bot == null || id == null) return new VoiceChannel[0];
		if (!Utils.isNumeric(id)) return new VoiceChannel[0];
		return new VoiceChannel[] {bot.getVoiceChannelById(Long.parseLong(id))};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends VoiceChannel> getReturnType() {
		return VoiceChannel.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "voice channel with id " + exprID.toString(e, debug);
	}

}