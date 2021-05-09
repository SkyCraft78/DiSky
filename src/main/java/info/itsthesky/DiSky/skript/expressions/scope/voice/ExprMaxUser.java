package info.itsthesky.disky.skript.expressions.scope.voice;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky.tools.object.VoiceChannelBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Max User of Voice Channel")
@Description("Get or set the max user amount of a voice chanenl (or builder)")
@Examples("set max users of voice channel to 20")
@Since("1.6")
public class ExprMaxUser extends SimplePropertyExpression<Object, Number> {

    static {
        register(ExprMaxUser.class, Number.class,
                "max user[s]",
                "voicechannel/voicechannelbuilder/channel"
        );
    }

    @Nullable
    @Override
    public Number convert(Object entity) {
        if (entity instanceof VoiceChannelBuilder) return ((VoiceChannelBuilder) entity).getUserLimit();
        if (entity instanceof VoiceChannel) return ((VoiceChannel) entity).getUserLimit();
        if (entity instanceof GuildChannel) return ((GuildChannel) entity).getType().equals(ChannelType.VOICE) ? ((VoiceChannel) entity).getUserLimit() : null;
        return null;
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    protected String getPropertyName() {
        return "max users amount";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Number.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (delta == null || delta.length == 0) return;
        if (!(delta[0] instanceof Number)) return;
        Number value = (Number) delta[0];
        if (mode == Changer.ChangeMode.SET) {
            for (Object entity : getExpr().getArray(e)) {
                if (entity instanceof VoiceChannelBuilder) ((VoiceChannelBuilder) entity).setUserLimit(value.intValue());
                if (entity instanceof VoiceChannel) ((VoiceChannel) entity).getManager().setUserLimit(value.intValue()).queue();
                if ((entity instanceof GuildChannel) && ((GuildChannel) entity).getType().equals(ChannelType.VOICE)) ((TextChannel) entity).getManager().setUserLimit(value.intValue()).queue();
            }
        }
    }
}