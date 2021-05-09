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

@Name("Track Start")
@Description("Fired when a track just started in a guild.")
@Examples("on track start:")
@Since("1.6, 1.8 (more event-value)")
public class EventTrackStart extends Event {

    static {
        Skript.registerEvent("Track Start", SimpleEvent.class, EventTrackStart.class, "[discord] (audio|track) start");

        EventValues.registerEventValue(EventTrackStart.class, AudioTrack.class, new Getter<AudioTrack, EventTrackStart>() {
            @Nullable
            @Override
            public AudioTrack get(final @NotNull EventTrackStart event) {
                return event.getTrack();
            }
        }, 0);

        EventValues.registerEventValue(EventTrackStart.class, Guild.class, new Getter<Guild, EventTrackStart>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventTrackStart event) {
                return event.getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventTrackStart.class, JDA.class, new Getter<JDA, EventTrackStart>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventTrackStart event) {
                return event.getBot();
            }
        }, 0);

        EventValues.registerEventValue(EventTrackStart.class, VoiceChannel.class, new Getter<VoiceChannel, EventTrackStart>() {
            @Nullable
            @Override
            public VoiceChannel get(final @NotNull EventTrackStart event) {
                return event.getChannel();
            }
        }, 0);

    }

    private final AudioTrack track;
    private final Guild guild;
    private final JDA bot;
    private final VoiceChannel channel;

    public EventTrackStart(
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