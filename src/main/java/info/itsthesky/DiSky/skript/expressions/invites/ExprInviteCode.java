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

@Name("Invite Code")
@Description("Get the code of a discord invite.")
@Since("1.7")
public class ExprInviteCode extends SimplePropertyExpression<Invite, String> {

    static {
        register(ExprInviteCode.class, String.class,
                "[discord] [invite] [url] code",
                "invite"
        );
    }

    @Nullable
    @Override
    public String convert(Invite entity) {
        return entity.getCode();
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "invite code";
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