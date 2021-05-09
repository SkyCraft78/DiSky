package info.itsthesky.disky.skript.audio.effects;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import com.github.natanbc.lavadsp.tremolo.TremoloPcmAudioFilter;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.managers.music.AudioUtils;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.Collections;

@Name("Guild Frequency")
@Description("Get or change the audio frequency of a guild. MUST be bigger than 0 (default is 0.01)")
@Examples("set frequency of event-guild to 0.1")
@Since("1.11")
public class ExprGuildFrequency extends SimplePropertyExpression<Guild, Number> {

    static {
        register(ExprGuildFrequency.class, Number.class,
                "[discord] audio [guild] frequency",
                "guild"
        );
    }

    @Nullable
    @Override
    public Number convert(Guild guild) {
        return AudioUtils.getEffectData(guild).getFrequency();
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    protected String getPropertyName() {
        return "guild frequency";
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
        try {
            switch (mode) {
                case SET:
                    for (Guild guild : getExpr().getArray(e)) {
                        AudioPlayer player = AudioUtils.getGuildAudioPlayer(guild).getPlayer();
                        player.setFilterFactory((track, format, output)->{
                            TremoloPcmAudioFilter tremolo = new TremoloPcmAudioFilter(output, format.channelCount, format.sampleRate);
                            tremolo.setFrequency(value.floatValue());
                            return Collections.singletonList(tremolo);
                        });
                        AudioUtils.getEffectData(guild).setFrequency(value.doubleValue());
                    }
                    break;
                case ADD:
                    for (Guild guild : getExpr().getArray(e)) {
                        AudioPlayer player = AudioUtils.getGuildAudioPlayer(guild).getPlayer();
                        player.setFilterFactory((track, format, output)->{
                            TremoloPcmAudioFilter tremolo = new TremoloPcmAudioFilter(output, format.channelCount, format.sampleRate);
                            tremolo.setFrequency(value.floatValue() + tremolo.getFrequency());
                            return Collections.singletonList(tremolo);
                        });
                        AudioUtils.getEffectData(guild).addFrequency(value.doubleValue());
                    }
                    break;
                case REMOVE:
                    for (Guild guild : getExpr().getArray(e)) {
                        AudioPlayer player = AudioUtils.getGuildAudioPlayer(guild).getPlayer();
                        player.setFilterFactory((track, format, output)->{
                            TremoloPcmAudioFilter tremolo = new TremoloPcmAudioFilter(output, format.channelCount, format.sampleRate);
                            tremolo.setFrequency(tremolo.getFrequency() - value.floatValue());
                            return Collections.singletonList(tremolo);
                        });
                        AudioUtils.getEffectData(guild).removeFrequency(value.doubleValue());
                    }
                    break;
            }
        } catch (Exception ex) {
            DiSky.getInstance().getLogger().severe("Cannot change the frequency of the guild, internal error occurred:" + ex.getMessage());
        }
    }
}