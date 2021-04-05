package info.itsthesky.DiSky.skript.audio.tracks;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.DiSky.managers.BotManager;
import info.itsthesky.DiSky.managers.music.AudioUtils;
import info.itsthesky.DiSky.managers.music.TrackScheduler;
import info.itsthesky.DiSky.tools.MultiplyPropertyExpression;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Bot Queue")
@Description("Return all tracks from a specific guild.")
@Examples("set {_tracks::*} to queue of event-guild")
@Since("1.6-pre2")
public class ExprBotQueue extends MultiplyPropertyExpression<Guild, AudioTrack> {

    static {
        register(ExprBotQueue.class, AudioTrack.class,
                "[discord] [audio] queue",
                "guild"
        );
    }

    @Nullable
    @Override
    public AudioTrack[] convert(Guild guild) {
        return AudioUtils.getGuildAudioPlayer(guild).trackScheduler.getQueue().toArray(new AudioTrack[0]);
    }

    @Override
    public Class<? extends AudioTrack> getReturnType() {
        return AudioTrack.class;
    }

    @Override
    protected String getPropertyName() {
        return "bot queue";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        
    }
}