package info.itsthesky.disky.skript.expressions.scope.category;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky.skript.scope.category.ScopeCategory;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.CategoryBuilder;
import net.dv8tion.jda.api.entities.Category;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Category Position")
@Description("Get or set the position of a category or builder.")
@Examples("set position of category builder to 5")
@Since("1.4.1")
public class ExprCategoryPosition extends SimplePropertyExpression<Object, Number> {

    static {
        register(ExprCategoryPosition.class, Number.class,
                "[discord] [category] position",
                "category/categorybuilder"
        );
    }

    @Nullable
    @Override
    public Number convert(Object entity) {
        if (entity instanceof Category) {
            return ((Category) entity).getPosition();
        } else if (entity instanceof CategoryBuilder) {
            return ((CategoryBuilder) entity).getPosition();
        }
        return null;
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    protected String getPropertyName() {
        return "category position";
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
        if (!(delta[0] instanceof Number)) return;
        Number newState = (Number) delta[0];
        if (mode == Changer.ChangeMode.SET) {
            for (Object entity : getExpr().getArray(e)) {
                if (entity instanceof Category) {
                    ((Category) entity).getManager().setPosition(Utils.round(newState.doubleValue())).complete();
                } else if (entity instanceof CategoryBuilder) {
                    ((CategoryBuilder) entity).setPosition(Utils.round(newState.doubleValue()));
                    ScopeCategory.lastBuilder.setPosition(Utils.round(newState.doubleValue()));
                }
            }
        }
    }
}