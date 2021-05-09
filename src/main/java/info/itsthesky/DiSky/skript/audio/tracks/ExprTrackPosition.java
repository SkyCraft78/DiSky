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

@Name("Track Position")
@Description("Return the position of a specific track")
@Examples("set {_position} to position of last played track.")
@Since("1.6-pre2")
public class ExprTrackPosition extends SimplePropertyExpression<AudioTrack, Timespan> {

    static {
        register(ExprTrackPosition.class, Timespan.class,
                "[discord] [audio] track position",
                "track"
        );
    }

    @Nullable
    @Override
    public Timespan convert(AudioTrack entity) {
        return new Timespan(entity.getPosition());
    }

    @Override
    public Class<? extends Timespan> getReturnType() {
        return Timespan.class;
    }

    @Override
    protected String getPropertyName() {
        return "position";
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