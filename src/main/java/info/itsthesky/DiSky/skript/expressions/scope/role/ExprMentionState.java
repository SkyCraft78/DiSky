package info.itsthesky.disky.skript.expressions.scope.role;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky.skript.scope.role.ScopeRole;
import info.itsthesky.disky.tools.object.RoleBuilder;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Mention State of a role (or builder)")
@Description("Get or set the mentionable state of a role or builder.")
@Examples("set mentionable state of role to true")
@Since("1.4")
public class ExprMentionState extends SimplePropertyExpression<Object, Boolean> {

    static {
        register(ExprMentionState.class, Boolean.class,
                "mentionable state",
                "role/rolebuilder"
        );
    }

    @Nullable
    @Override
    public Boolean convert(Object entity) {
        if (entity instanceof Role) {
            return ((Role) entity).isMentionable();
        } else if (entity instanceof RoleBuilder) {
            return ((RoleBuilder) entity).isMentionable();
        }
        return false;
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    protected String getPropertyName() {
        return "mentionable state";
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
                if (entity instanceof Role) {
                    ((Role) entity).getManager().setMentionable(newState).complete();
                } else if (entity instanceof RoleBuilder) {
                    ((RoleBuilder) entity).setMentionable(newState);
                    ScopeRole.lastBuilder.setMentionable(newState);
                }
            }
        }
    }
}