package info.itsthesky.disky.managers.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

public class GuildAudioManager {

    private final AudioPlayer musicPlayer;
    public final TrackScheduler trackScheduler;
    private final Guild guild;
    private final JDA jda;

    public GuildAudioManager(Guild guild, JDA jda) {
        musicPlayer = AudioUtils.MANAGER.createPlayer();
        trackScheduler = new TrackScheduler(musicPlayer, guild, jda);
        musicPlayer.addListener(trackScheduler);
        this.guild = guild;
        this.jda = jda;
    }

    public AudioPlayerSendHandler getSendHandler() {
        return new AudioPlayerSendHandler(musicPlayer);
    }

    public AudioPlayer getPlayer() {
        return musicPlayer;
    }

    public TrackScheduler getScheduler() {
        return trackScheduler;
    }

    public Guild getGuild() {
        return guild;
    }

    public JDA getJda() {
        return jda;
    }
}
