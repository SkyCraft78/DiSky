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

@Name("Guild Depth")
@Description("Get or change the audio depth of a guild. MUST be between 0.1 AND 0.9 (default is 0.001)")
@Examples("set depth of event-guild to 1.3")
@Since("1.7")
public class ExprGuildDepth extends SimplePropertyExpression<Guild, Number> {

    static {
        register(ExprGuildDepth.class, Number.class,
                "[discord] [audio] [guild] depth",
                "guild"
        );
    }

    @Nullable
    @Override
    public Number convert(Guild guild) {
        return AudioUtils.getEffectData(guild).getDepth();
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    protected String getPropertyName() {
        return "guild depth";
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
                            tremolo.setDepth(value.floatValue());
                            return Collections.singletonList(tremolo);
                        });
                        AudioUtils.getEffectData(guild).setDepth(value.doubleValue());
                    }
                    break;
                case ADD:
                    for (Guild guild : getExpr().getArray(e)) {
                        AudioPlayer player = AudioUtils.getGuildAudioPlayer(guild).getPlayer();
                        player.setFilterFactory((track, format, output)->{
                            TremoloPcmAudioFilter tremolo = new TremoloPcmAudioFilter(output, format.channelCount, format.sampleRate);
                            tremolo.setDepth(value.floatValue() + tremolo.getDepth());
                            return Collections.singletonList(tremolo);
                        });
                        AudioUtils.getEffectData(guild).addDepth(value.doubleValue());
                    }
                    break;
                case REMOVE:
                    for (Guild guild : getExpr().getArray(e)) {
                        AudioPlayer player = AudioUtils.getGuildAudioPlayer(guild).getPlayer();
                        player.setFilterFactory((track, format, output)->{
                            TremoloPcmAudioFilter tremolo = new TremoloPcmAudioFilter(output, format.channelCount, format.sampleRate);
                            tremolo.setDepth(tremolo.getDepth() - value.floatValue());
                            return Collections.singletonList(tremolo);
                        });
                        AudioUtils.getEffectData(guild).removeDepth(value.doubleValue());
                    }
                    break;
            }
        } catch (IllegalArgumentException ex) {
            DiSky.getInstance().getLogger().severe("Cannot change the depth of the guild, internal error occurred:" + ex.getMessage());
        }
    }
}