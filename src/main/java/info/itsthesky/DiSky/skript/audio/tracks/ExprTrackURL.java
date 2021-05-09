package info.itsthesky.disky.skript.audio.tracks;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Track URL")
@Description("Return the YouTube URL of a track")
@Examples("set {_url} to url of last played track.")
@Since("1.6-pre2")
public class ExprTrackURL extends SimplePropertyExpression<AudioTrack, String> {

    static {
        register(ExprTrackURL.class, String.class,
                "[discord] [audio] track (url|uri)",
                "track"
        );
    }

    @Nullable
    @Override
    public String convert(AudioTrack entity) {
        return entity.getInfo().uri;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "url";
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