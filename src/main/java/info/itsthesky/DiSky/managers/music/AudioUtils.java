package info.itsthesky.disky.managers.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeSearchProvider;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.*;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.AudioSite;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.annotation.Nullable;
import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class AudioUtils {

    public static HashMap<AudioPlayer, AudioData> MUSIC_DATA = new HashMap<>();
    public static AudioPlayerManager MANAGER;
    public static YoutubeAudioSourceManager YOUTUBE_MANAGER_SOURCE;
    public static final YoutubeSearchProvider YOUTUBE_MANAGER_SEARCH = new YoutubeSearchProvider();
    private static final SoundCloudAudioSourceManager SOUNDCLOUD_AUDIO_MANAGER = SoundCloudAudioSourceManager.createDefault();
    public static Map<Long, GuildAudioManager> MUSIC_MANAGERS;
    private static final Map<Long, EffectData> GUILDS_EFFECTS = new HashMap<>();
    private final static DefaultAudioPlayerManager DEFAULT_MANAGER = new DefaultAudioPlayerManager();
    private final static LocalAudioSourceManager LOCAL_MANAGER = new LocalAudioSourceManager();

    public static EffectData getEffectData(Guild guild) {
        if (!GUILDS_EFFECTS.containsKey(guild.getIdLong())) {
            EffectData data = new EffectData(guild);
            GUILDS_EFFECTS.put(guild.getIdLong(), data);
            return data;
        } else {
            return GUILDS_EFFECTS.get(guild.getIdLong());
        }
    }

    public static void initializeAudio() {
        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        playerManager.getConfiguration().setFilterHotSwapEnabled(true);
        MANAGER = playerManager;
        MUSIC_MANAGERS = new HashMap<>();
        AudioSourceManagers
                .registerRemoteSources(playerManager);
        AudioSourceManagers
                .registerLocalSource(playerManager);
        AudioSourceManagers
                .registerRemoteSources(DEFAULT_MANAGER);
        YOUTUBE_MANAGER_SOURCE = new YoutubeAudioSourceManager();
    }

    public static AudioTrack[] search(String url, AudioSite site)
    {
        final String trackUrl;

        //Strip <>'s that prevent discord from embedding link resources
        if (url.startsWith("<") && url.endsWith(">"))
            trackUrl = url.substring(1, url.length() - 1);
        else
            trackUrl = url;

        String siteKey = site.equals(AudioSite.SOUNDCLOUD) ? "scsearch:" : "ytsearch:";

        CompletableFuture<List<AudioTrack>> cf = new CompletableFuture<>();
        DEFAULT_MANAGER.loadItem((Utils.containURL(trackUrl) ? "" : siteKey) + trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                cf.complete(Collections.singletonList(track));
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                cf.complete(playlist.getTracks());
            }

            @Override
            public void noMatches() {
                cf.complete(new ArrayList<>());
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                cf.completeExceptionally(exception);
            }
        });
        return cf.join().toArray(new AudioTrack[0]);
    }

    @Nullable
    public static AudioTrack loadFromFile(final File path) {
        AudioItem item = LOCAL_MANAGER.loadItem(DEFAULT_MANAGER, new AudioReference(path.getPath(), ""));
        return item instanceof AudioTrack ? (AudioTrack) item : null;
    }

    public static synchronized GuildAudioManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildAudioManager musicManager = MUSIC_MANAGERS.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildAudioManager(guild, guild.getJDA());
            MUSIC_MANAGERS.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
        return musicManager;
    }

    public static void play(Guild guild, VoiceChannel channel, AudioTrack... tracks) {
        GuildAudioManager musicManager = getGuildAudioPlayer(guild);
        connectToFirstVoiceChannel(guild.getAudioManager(), channel);
        for (AudioTrack track : tracks) {
            musicManager.trackScheduler.queue(track);
        }
    }

    public static void connectToFirstVoiceChannel(AudioManager audioManager, VoiceChannel channel) {
        audioManager.closeAudioConnection();
        if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
            audioManager.openAudioConnection(channel);
        }
    }

    public static AudioTrack skipTrack(Guild guild) {
        GuildAudioManager musicManager = getGuildAudioPlayer(guild);
        return musicManager.trackScheduler.nextTrack();
    }

    public static void loadAndPlay(final Guild guild, final VoiceChannel channel, String id) {
        GuildAudioManager musicManager = getGuildAudioPlayer(guild);
        MANAGER.loadItemOrdered(musicManager, id, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                play(guild, channel, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();
                if (firstTrack == null) firstTrack = playlist.getTracks().get(0);
                play(guild, channel, firstTrack);
            }

            @Override
            public void noMatches() { }

            @Override
            public void loadFailed(FriendlyException exception) { }
        });
    }
}
