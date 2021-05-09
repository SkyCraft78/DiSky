package info.itsthesky.disky.skript.expressions.invites;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky.tools.object.InviteBuilder;
import net.dv8tion.jda.api.entities.Invite;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Invite Max Uses")
@Description("Get the amount of max uses of a discord invite.")
@Since("1.7")
public class ExprInviteMaxUses extends SimplePropertyExpression<Object, Number> {

    static {
        register(ExprInviteMaxUses.class, Number.class,
                "[discord] [invite] max[imum] use[s]",
                "invite/invitebuilder"
        );
    }

    @Nullable
    @Override
    public Number convert(Object entity) {
        if (entity instanceof InviteBuilder) return ((InviteBuilder) entity).getMaxUse();
        if (entity instanceof Invite) return ((Invite) entity).getMaxUses();
        return null;
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    protected String getPropertyName() {
        return "invite max uses";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode.equals(Changer.ChangeMode.SET)) {
            return CollectionUtils.array(Number.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (delta == null || delta.length == 0 || delta[0] == null || !(delta[0] instanceof Number) || !mode.equals(Changer.ChangeMode.SET)) return;
        Number value = (Number) delta[0];
        for (Object entity : getExpr().getAll(e)) {
            if (!(entity instanceof InviteBuilder)) return;
            InviteBuilder builder = (InviteBuilder) entity;
            builder.setMaxUse(value.intValue());
        }
    }
}