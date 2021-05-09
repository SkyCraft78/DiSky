package info.itsthesky.disky.skript.audio.tracks;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.util.Timespan;
import ch.njol.util.coll.CollectionUtils;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Track Duration")
@Description("Return the duration of a specific track")
@Examples("set {_duration} to duration of last played track.")
@Since("1.6-pre2")
public class ExprTrackDuration extends SimplePropertyExpression<AudioTrack, Timespan> {

    static {
        register(ExprTrackDuration.class, Timespan.class,
                "[discord] [audio] track duration",
                "track"
        );
    }

    @Nullable
    @Override
    public Timespan convert(AudioTrack entity) {
        return new Timespan(entity.getDuration());
    }

    @Override
    public Class<? extends Timespan> getReturnType() {
        return Timespan.class;
    }

    @Override
    protected String getPropertyName() {
        return "duration";
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