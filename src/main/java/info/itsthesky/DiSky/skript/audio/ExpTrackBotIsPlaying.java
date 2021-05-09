package info.itsthesky.disky.skript.audio;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky.managers.music.AudioUtils;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;

@Name("Track the Bot is Playing")
@Description("Return the track (if set) which the bot is currently playing.")
@Since("1.6")
public class ExpTrackBotIsPlaying extends SimpleExpression<AudioTrack> {

	static {
		Skript.registerExpression(ExpTrackBotIsPlaying.class, AudioTrack.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] track [the] [bot] is playing in [the] [guild] %guild%");
	}

	Expression<Guild> exprGuild;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprGuild = (Expression<Guild>) exprs[0];
		return true;
	}

	@Override
	protected AudioTrack[] get(final Event e) {
		Guild guild = exprGuild.getSingle(e);
		if (guild == null) return new AudioTrack[0];
		return !AudioUtils.MUSIC_MANAGERS.containsKey(guild.getIdLong()) ? new AudioTrack[0] : new AudioTrack[] {AudioUtils.MUSIC_MANAGERS.get(guild.getIdLong()).getPlayer().getPlayingTrack()};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends AudioTrack> getReturnType() {
		return AudioTrack.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "track the bot is playing in guild " + exprGuild.toString(e, debug);
	}

}