package info.itsthesky.disky.skript.audio.effects;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import info.itsthesky.disky.managers.music.AudioUtils;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Guild Volume")
@Description("Get or change the volume of a guild.")
@Examples("set volume of event-guild to 50")
@Since("1.6-pre2")
public class ExprGuildVolume extends SimplePropertyExpression<Guild, Number> {

    static {
        register(ExprGuildVolume.class, Number.class,
                "[discord] [audio] [guild] volume",
                "guild"
        );
    }

    @Nullable
    @Override
    public Number convert(Guild guild) {
        return AudioUtils.getGuildAudioPlayer(guild).getPlayer().getVolume();
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    protected String getPropertyName() {
        return "guild volume";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (
                mode.equals(Changer.ChangeMode.ADD) ||
                        mode.equals(Changer.ChangeMode.REMOVE) ||
                        mode.equals(Changer.ChangeMode.SET)
        ) {
            return CollectionUtils.array(Number.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (delta == null || delta[0] == null || !(delta[0] instanceof Number)) return;
        Number value = (Number) delta[0];
        switch (mode) {
            case SET:
                for (Guild guild : getExpr().getArray(e)) {
                    AudioPlayer player = AudioUtils.getGuildAudioPlayer(guild).getPlayer();
                    player.setVolume(value.intValue());
                }
                break;
            case ADD:
                for (Guild guild : getExpr().getArray(e)) {
                    AudioPlayer player = AudioUtils.getGuildAudioPlayer(guild).getPlayer();
                    player.setVolume(
                            player.getVolume() +
                                    value.intValue()
                    );
                }
                break;
            case REMOVE:
                for (Guild guild : getExpr().getArray(e)) {
                    AudioPlayer player = AudioUtils.getGuildAudioPlayer(guild).getPlayer();
                    player.setVolume(
                            player.getVolume() -
                                    value.intValue()
                    );
                }
                break;
        }
    }
}