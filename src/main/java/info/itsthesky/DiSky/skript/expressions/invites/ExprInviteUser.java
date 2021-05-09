package info.itsthesky.disky.skript.expressions.invites;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Invite User")
@Description("Get the user creator of a discord invite.")
@Since("1.7")
public class ExprInviteUser extends SimplePropertyExpression<Invite, User> {

    static {
        register(ExprInviteUser.class, User.class,
                "[discord] [invite] (user|inviter|creator)",
                "invite"
        );
    }

    @Nullable
    @Override
    public User convert(Invite entity) {
        return entity.getInviter();
    }

    @Override
    public Class<? extends User> getReturnType() {
        return User.class;
    }

    @Override
    protected String getPropertyName() {
        return "invite user";
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