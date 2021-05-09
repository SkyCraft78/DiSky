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

@Name("Separate State of a role (or builder)")
@Description("Get or set the separate state of a role or builder. If true, any member who got that role will be separate from other member.")
@Examples("set separate state of role to true")
@Since("1.4")
public class ExprSeparateState extends SimplePropertyExpression<Object, Boolean> {

    static {
        register(ExprSeparateState.class, Boolean.class,
                "separate state",
                "role/rolebuilder"
        );
    }

    @Nullable
    @Override
    public Boolean convert(Object entity) {
        if (entity instanceof Role) {
            return ((Role) entity).isHoisted();
        } else if (entity instanceof RoleBuilder) {
            return ((RoleBuilder) entity).isSeparate();
        }
        return false;
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    protected String getPropertyName() {
        return "separate state";
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
                    ((Role) entity).getManager().setHoisted(newState).complete();
                } else if (entity instanceof RoleBuilder) {
                    ((RoleBuilder) entity).setSeparate(newState);
                    ScopeRole.lastBuilder.setSeparate(newState);
                }
            }
        }
    }
}