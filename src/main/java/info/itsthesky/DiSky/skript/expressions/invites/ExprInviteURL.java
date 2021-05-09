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

@Name("Invite URL")
@Description("Get the url of a discord invite.")
@Since("1.7")
public class ExprInviteURL extends SimplePropertyExpression<Invite, String> {

    static {
        register(ExprInviteURL.class, String.class,
                "[discord] [invite] ur(l|i)",
                "invite"
        );
    }

    @Nullable
    @Override
    public String convert(Invite entity) {
        return entity.getUrl();
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "invite url";
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