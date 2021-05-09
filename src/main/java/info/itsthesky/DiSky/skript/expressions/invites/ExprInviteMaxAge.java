package info.itsthesky.disky.skript.expressions.invites;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.util.Timespan;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky.tools.object.InviteBuilder;
import net.dv8tion.jda.api.entities.Invite;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Invite Max Age")
@Description("Get the max age of a discord invite.")
@Since("1.7")
public class ExprInviteMaxAge extends SimplePropertyExpression<Object, Timespan> {

    static {
        register(ExprInviteMaxAge.class, Timespan.class,
                "[discord] [invite] max (age|date)",
                "invite/invitebuilder"
        );
    }

    @Nullable
    @Override
    public Timespan convert(Object entity) {
        if (entity instanceof InviteBuilder) return new Timespan(((Invite) entity).getMaxAge() / 1000);
        if (entity instanceof Invite) return new Timespan(((Invite) entity).getMaxAge() / 1000);
        return null;
    }

    @Override
    public Class<? extends Timespan> getReturnType() {
        return Timespan.class;
    }

    @Override
    protected String getPropertyName() {
        return "invite max age";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode.equals(Changer.ChangeMode.SET)) {
            return CollectionUtils.array(Timespan.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (delta == null || delta.length == 0 || delta[0] == null || !(delta[0] instanceof Timespan) || !mode.equals(Changer.ChangeMode.SET)) return;
        Timespan value = (Timespan) delta[0];
        for (Object entity : getExpr().getAll(e)) {
            if (!(entity instanceof InviteBuilder)) return;
            InviteBuilder builder = (InviteBuilder) entity;
            builder.setMaxAge(value.getMilliSeconds() * 1000);
        }
    }
}