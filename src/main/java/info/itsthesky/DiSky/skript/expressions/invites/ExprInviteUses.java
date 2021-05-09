package info.itsthesky.disky.skript.expressions.invites;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import net.dv8tion.jda.api.entities.Invite;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Invite Uses")
@Description("Get the amount of uses of a discord invite.")
@Since("1.7")
public class ExprInviteUses extends SimplePropertyExpression<Invite, Number> {

    static {
        register(ExprInviteUses.class, Number.class,
                "[discord] [invite] [url] use[s]",
                "invite"
        );
    }

    @Nullable
    @Override
    public Number convert(Invite entity) {
        return entity.getUses();
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    protected String getPropertyName() {
        return "invite uses";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {

    }
}