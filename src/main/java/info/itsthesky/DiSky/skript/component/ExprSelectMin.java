package info.itsthesky.disky.skript.component;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Minimum Option Amount")
@Description("Change the minimum amount of options needed to validate this selection menu.")
@Since("2.0")
public class ExprSelectMin extends SimplePropertyExpression<SelectionMenu.Builder, Number> {
    static {
        register(
                ExprSelectMin.class,
                Number.class,
                "min[imum] (range|selected options)",
                "selectbuilder"
        );
    }

    @Override
    protected String getPropertyName() {
        return "minimum selected options";
    }

    @Nullable
    @Override
    public Number convert(SelectionMenu.Builder builder) {
        return builder.getMinValues();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        Number value = (Number) delta[0];
        for (SelectionMenu.Builder builder : getExpr().getAll(e)) {
            builder.setMinValues(value.intValue());
        }
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Number.class);
        }
        return new Class[0];
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }
}