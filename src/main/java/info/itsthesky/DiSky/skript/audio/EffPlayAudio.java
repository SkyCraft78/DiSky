package info.itsthesky.disky.skript.audio;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky.managers.music.AudioUtils;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;

@Name("Play Audio")
@Description("")
@Examples("play {_r::1} in event-guild to event-member")
@Since("1.6, 1.9 (rework using search)")
public class EffPlayAudio extends Effect {

    static {
        Skript.registerEffect(EffPlayAudio.class, // [the] [bot] [(named|with name)] %string%
                "["+ Utils.getPrefixName() +"] play [tracks] %tracks% in [the] [voice] [channel] %voicechannel/channel%");
    }

    private Expression<AudioTrack> exprTracks;
    private Expression<GuildChannel> exprChannel;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprTracks = (Expression<AudioTrack>) exprs[0];
        exprChannel = (Expression<GuildChannel>) exprs[1];
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void execute(Event e) {
        AudioTrack[] tracks = exprTracks.getAll(e);
        GuildChannel channel = exprChannel.getSingle(e);
        if (tracks == null || channel == null) return;
        if (!channel.getType().equals(ChannelType.VOICE)) return;
        AudioUtils.play(channel.getGuild(), (VoiceChannel) channel, tracks);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "play tracks " + exprTracks.toString(e, debug) + " in voice channel " + exprChannel.toString(e, debug);
    }

}
