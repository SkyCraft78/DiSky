package info.itsthesky.disky.skript.events;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky.tools.events.BukkitEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

public class TrackEvent extends BukkitEvent {
    static {
        EventValues.registerEventValue(TrackEvent.class, JDA.class, new Getter<JDA, TrackEvent>() {
            @Override
            public JDA get(TrackEvent event) {
                return event.getJDA();
            }
        }, 0);

        EventValues.registerEventValue(TrackEvent.class, Guild.class, new Getter<Guild, TrackEvent>() {
            @Override
            public Guild get(TrackEvent event) {
                return event.getGuild();
            }
        }, 0);

        EventValues.registerEventValue(TrackEvent.class, AudioTrack.class, new Getter<AudioTrack, TrackEvent>() {
            @Override
            public AudioTrack get(TrackEvent event) {
                return event.getTrack();
            }
        }, 0);
    }

    private TrackState state;
    private JDA jda;
    private Guild guild;
    private AudioTrack track;

    public TrackEvent() {

    }

    public TrackEvent(TrackState state, JDA jda, Guild guild, AudioTrack track) {
        this.state = state;
        this.jda = jda;
        this.guild = guild;
        this.track = track;
    }

    public TrackState getState() {
        return state;
    }

    public JDA getJDA() {
        return jda;
    }

    public Guild getGuild() {
        return guild;
    }

    public AudioTrack getTrack() {
        return track;
    }

    public enum TrackState {

        PAUSE, PLAY, START, END, SEEK

    }

}