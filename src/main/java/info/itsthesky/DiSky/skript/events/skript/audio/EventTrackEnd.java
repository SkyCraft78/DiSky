package info.itsthesky.disky.skript.events.skript.audio;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Track End")
@Description("Fired when a track just ended in a guild.")
@Examples("on track end:")
@Since("1.6, 1.8 (more event-value)")
public class EventTrackEnd extends Event {

    static {
        Skript.registerEvent("Track End", SimpleEvent.class, EventTrackEnd.class, "[discord] (audio|track) (finish[ed]|end)")
            .description("Fired when a track just ended in a guild.")
            .examples("on track end:")
            .since("1.6, 1.8 (more event-value)");

        EventValues.registerEventValue(EventTrackEnd.class, AudioTrack.class, new Getter<AudioTrack, EventTrackEnd>() {
            @Nullable
            @Override
            public AudioTrack get(final @NotNull EventTrackEnd event) {
                return event.getTrack();
            }
        }, 0);

        EventValues.registerEventValue(EventTrackEnd.class, Guild.class, new Getter<Guild, EventTrackEnd>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventTrackEnd event) {
                return event.getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventTrackEnd.class, JDA.class, new Getter<JDA, EventTrackEnd>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventTrackEnd event) {
                return event.getBot();
            }
        }, 0);

        EventValues.registerEventValue(EventTrackEnd.class, VoiceChannel.class, new Getter<VoiceChannel, EventTrackEnd>() {
            @Nullable
            @Override
            public VoiceChannel get(final @NotNull EventTrackEnd event) {
                return event.getChannel();
            }
        }, 0);

    }

    private final AudioTrack track;
    private final Guild guild;
    private final JDA bot;
    private final VoiceChannel channel;

    public EventTrackEnd(
            final AudioTrack track,
            final Guild guild,
            final JDA bot,
            final VoiceChannel channel
            ) {
        super(Utils.areEventAsync());
        this.guild = guild;
        this.track = track;
        this.bot = bot;
        this.channel = channel;
    }

    public AudioTrack getTrack() {
        return track;
    }
    public JDA getBot() {
        return bot;
    }
    public Guild getGuild() {
        return guild;
    }
    public VoiceChannel getChannel() {
        return channel;
    }

    private static final HandlerList HANDLERS = new HandlerList();
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}