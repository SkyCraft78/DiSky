package info.itsthesky.disky.skript.audio.effects;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import com.github.natanbc.lavadsp.timescale.TimescalePcmAudioFilter;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import info.itsthesky.disky.managers.music.AudioUtils;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.Collections;

@Name("Guild Speed")
@Description("Get or change the speed of a guild. (Of course, the audio speed)")
@Examples("discord command speed [<number>]:\n" +
        "\tprefixes: *\n" +
        "\ttrigger:\n" +
        "\t\tif arg 1 is not set:\n" +
        "\t\t\treply with \":cloud_tornado: **Current __speed__: `%speed of event-guild%`**\"\n" +
        "\t\t\tstop\n" +
        "\t\tset guild speed of event-guild to arg-1\n" +
        "\t\treply with \":cloud_tornado: **The __speed__ has been changed to `%guild speed of event-guild%` !**\"")
@Since("1.7")
public class ExprGuildSpeed extends SimplePropertyExpression<Guild, Number> {

    static {
        register(ExprGuildSpeed.class, Number.class,
                "[discord] [audio] [guild] speed",
                "guild"
        );
    }

    @Nullable
    @Override
    public Number convert(Guild guild) {
        return AudioUtils.getEffectData(guild).getSpeed();
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    protected String getPropertyName() {
        return "guild speed";
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
        if (delta == null || delta[0] == null) return;
        Number value = (Number) delta[0];
        switch (mode) {
            case SET:
                for (Guild guild : getExpr().getArray(e)) {
                    AudioPlayer player = AudioUtils.getGuildAudioPlayer(guild).getPlayer();
                    player.setFilterFactory((track, format, output)->{
                        TimescalePcmAudioFilter audioFilter = new TimescalePcmAudioFilter(output, format.channelCount, format.sampleRate);
                        audioFilter.setSpeed(value.doubleValue());
                        return Collections.singletonList(audioFilter);
                    });
                    AudioUtils.getEffectData(guild).setSpeed(value.doubleValue());
                }
                break;
            case ADD:
                for (Guild guild : getExpr().getArray(e)) {
                    AudioPlayer player = AudioUtils.getGuildAudioPlayer(guild).getPlayer();
                    player.setFilterFactory((track, format, output)->{
                        TimescalePcmAudioFilter audioFilter = new TimescalePcmAudioFilter(output, format.channelCount, format.sampleRate);
                        audioFilter.setSpeed(audioFilter.getSpeed() + value.doubleValue());
                        return Collections.singletonList(audioFilter);
                    });
                    AudioUtils.getEffectData(guild).addSpeed(value.doubleValue());
                }
                break;
            case REMOVE:
                for (Guild guild : getExpr().getArray(e)) {
                    AudioPlayer player = AudioUtils.getGuildAudioPlayer(guild).getPlayer();
                    player.setFilterFactory((track, format, output)->{
                        TimescalePcmAudioFilter audioFilter = new TimescalePcmAudioFilter(output, format.channelCount, format.sampleRate);
                        audioFilter.setSpeed(audioFilter.getSpeed() - value.doubleValue());
                        return Collections.singletonList(audioFilter);
                    });
                    AudioUtils.getEffectData(guild).removeSpeed(value.doubleValue());
                }
                break;
        }
    }
}