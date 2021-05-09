package info.itsthesky.disky.managers.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.skript.events.skript.audio.EventTrackEnd;
import info.itsthesky.disky.skript.events.skript.audio.EventTrackStart;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class schedules tracks for the audio player. It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private final Guild guild;
    private final JDA bot;
    private boolean shouldFiredEnd = true;
    private boolean isRepeated;

    /**
     * @param player The audio player this scheduler uses
     */
    public TrackScheduler(AudioPlayer player, Guild guild, JDA bot) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.guild = guild;
        this.bot = bot;
        this.isRepeated = false;
    }

    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     *
     * @param track The track to play or add to queue.
     */
    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    public void setRepeated(boolean repeated) {
        isRepeated = repeated;
    }

    public boolean isRepeated() {
        return isRepeated;
    }

    public void shuffleQueue() {
        Collections.shuffle((List<?>) queue);
    }

    /**
     * Start the next track, stopping the current one if it is playing.
     */
    public AudioTrack nextTrack() {
        AudioTrack next = queue.poll();
        shouldFiredEnd = false;
        player.startTrack(next, false);
        shouldFiredEnd = true;
        return next;
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }

    public void clearQueue() {
        player.stopTrack();
        queue.clear();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (!shouldFiredEnd) return;
        if (isRepeated)
            player.startTrack(track.makeClone(), false);
       VoiceChannel channel = guild.getAudioManager().getConnectedChannel();
       Utils.sync(() -> DiSky.getInstance().getServer().getPluginManager().callEvent(new EventTrackEnd(
               track,
               guild,
               bot,
               channel
       )));
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        VoiceChannel channel = guild.getAudioManager().getConnectedChannel();
        Utils.sync(() -> DiSky.getInstance().getServer().getPluginManager().callEvent(new EventTrackStart(
                track,
                guild,
                bot,
                channel
        )));
    }
}
