package info.itsthesky.disky.skript.expressions.scope.text;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Slowmode of a text channel (or builder)")
@Description("Get or set the Slowmode of text channel or builder.")
@Examples("set Slowmode of text channel builder to 0 # Use 0 to reset (deactivate) the slowmode")
@Since("1.4")
public class ExprSlowMode extends SimplePropertyExpression<Object, Number> {

    static {
        register(ExprSlowMode.class, Number.class,
                "slow[ ]mode",
                "channel/textchannel/textchannelbuilder"
        );
    }

    @Nullable
    @Override
    public Number convert(Object entity) {
        if (entity instanceof TextChannel) return ((TextChannel) entity).getSlowmode();
        if (entity instanceof GuildChannel) return ((GuildChannel) entity).getType().equals(ChannelType.TEXT) ? ((TextChannel) entity).getSlowmode() : null;
        return null;
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    protected String getPropertyName() {
        return "slowmode";
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
        int slow = Utils.round(Double.valueOf(delta[0].toString()));
        if (mode == Changer.ChangeMode.SET) {
            for (Object entity : getExpr().getArray(e)) {
                if (entity instanceof TextChannel) ((TextChannel) entity).getManager().setSlowmode(slow).queue();
                if ((entity instanceof GuildChannel) && ((GuildChannel) entity).getType().equals(ChannelType.TEXT)) ((TextChannel) entity).getManager().setSlowmode(slow).queue();
            }
        }
    }
}