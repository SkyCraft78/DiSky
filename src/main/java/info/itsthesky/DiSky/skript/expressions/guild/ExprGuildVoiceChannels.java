package info.itsthesky.disky.skript.expressions.guild;

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
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.bukkit.event.Event;

@Name("Guild Voice Channels")
@Description("Return all voice channels of the guilds")
@Examples("set {_voice::*} to voice channels of event-guild")
@Since("1.6")
public class ExprGuildVoiceChannels extends SimpleExpression<VoiceChannel> {

	static {
		Skript.registerExpression(ExprGuildVoiceChannels.class, VoiceChannel.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] [discord] voice[ ]channel[s] of [the] [guild] %guild%");
	}

	private Expression<Guild> exprGuild;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprGuild = (Expression<Guild>) exprs[0];
		return true;
	}

	@Override
	protected VoiceChannel[] get(final Event e) {
		Guild guild = exprGuild.getSingle(e);
		if (guild == null) return new VoiceChannel[0];
		return guild.getVoiceChannels().toArray(new VoiceChannel[0]);
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends VoiceChannel> getReturnType() {
		return VoiceChannel.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "voice channels of guild " + exprGuild.toString(e, debug);
	}

}