package info.itsthesky.DiSky.skript.audio.effects;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import com.github.natanbc.lavadsp.timescale.TimescalePcmAudioFilter;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import info.itsthesky.DiSky.DiSky;
import info.itsthesky.DiSky.managers.music.AudioUtils;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.Collections;

@Name("Guild Pitch")
@Description("Get or change the audio pith of a guild.")
@Examples("set audio pitch of event-guild to 1.3")
@Since("1.7")
public class ExprGuildPitch extends SimplePropertyExpression<Guild, Number> {

    static {
        register(ExprGuildPitch.class, Number.class,
                "[discord] audio [guild] pitch",
                "guild"
        );
    }

    @Nullable
    @Override
    public Number convert(Guild guild) {
        return AudioUtils.getEffectData(guild).getPitch();
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    protected String getPropertyName() {
        return "guild pitch";
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
                            TimescalePcmAudioFilter audioFilter = new TimescalePcmAudioFilter(output, format.channelCount, format.sampleRate);
                            audioFilter.setPitch(value.doubleValue());
                            return Collections.singletonList(audioFilter);
                        });
                        AudioUtils.getEffectData(guild).setPitch(value.doubleValue());
                    }
                    break;
                case ADD:
                    for (Guild guild : getExpr().getArray(e)) {
                        AudioPlayer player = AudioUtils.getGuildAudioPlayer(guild).getPlayer();
                        player.setFilterFactory((track, format, output)->{
                            TimescalePcmAudioFilter audioFilter = new TimescalePcmAudioFilter(output, format.channelCount, format.sampleRate);
                            audioFilter.setPitch(audioFilter.getSpeed() + value.doubleValue());
                            return Collections.singletonList(audioFilter);
                        });
                        AudioUtils.getEffectData(guild).addPitch(value.doubleValue());
                    }
                    break;
                case REMOVE:
                    for (Guild guild : getExpr().getArray(e)) {
                        AudioPlayer player = AudioUtils.getGuildAudioPlayer(guild).getPlayer();
                        player.setFilterFactory((track, format, output)->{
                            TimescalePcmAudioFilter audioFilter = new TimescalePcmAudioFilter(output, format.channelCount, format.sampleRate);
                            audioFilter.setPitch(audioFilter.getSpeed() - value.doubleValue());
                            return Collections.singletonList(audioFilter);
                        });
                        AudioUtils.getEffectData(guild).removePitch(value.doubleValue());
                    }
                    break;
            }
        } catch (IllegalArgumentException ex) {
            DiSky.getInstance().getLogger().severe("Cannot change the pitch of the guild, internal error occurred:" + ex.getMessage());
        }
    }
}