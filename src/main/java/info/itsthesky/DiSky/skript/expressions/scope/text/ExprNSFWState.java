package info.itsthesky.disky.skript.expressions.scope.text;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("NSFW State of a text channel (or builder)")
@Description("Get or set the NSFW state of text channel or builder.")
@Examples("set nsfw state of text channel builder to true")
@Since("1.4")
public class ExprNSFWState extends SimplePropertyExpression<Object, Boolean> {

    static {
        register(ExprNSFWState.class, Boolean.class,
                "nsfw state",
                "channel/textchannel/textchannelbuilder"
        );
    }

    @Nullable
    @Override
    public Boolean convert(Object entity) {
        if (entity instanceof TextChannel) return ((TextChannel) entity).isNews();
        if (entity instanceof GuildChannel) return ((GuildChannel) entity).getType().equals(ChannelType.TEXT) ? ((TextChannel) entity).isNSFW() : null;
        return false;
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    protected String getPropertyName() {
        return "nsfw state";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Boolean.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (delta == null || delta.length == 0) return;
        if (!(delta[0] instanceof Boolean)) return;
        boolean newState = (Boolean) delta[0];
        if (mode == Changer.ChangeMode.SET) {
            for (Object entity : getExpr().getArray(e)) {
                if (entity instanceof TextChannel) ((TextChannel) entity).getManager().setNSFW(newState).queue();
                if ((entity instanceof GuildChannel) && ((GuildChannel) entity).getType().equals(ChannelType.TEXT)) ((TextChannel) entity).getManager().setNSFW(newState).queue();
            }
        }
    }
}