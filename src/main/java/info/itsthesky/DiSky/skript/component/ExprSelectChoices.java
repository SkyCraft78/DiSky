package info.itsthesky.disky.skript.component;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky.tools.MultiplyPropertyExpression;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Selection Menu Choices")
@Description("Manage every choice present in a selection menu.")
@Since("2.0")
public class ExprSelectChoices extends MultiplyPropertyExpression<SelectionMenu.Builder, SelectOption> {

    static {
        register(ExprSelectChoices.class,
                SelectOption.class,
                "choice[s]",
                "selectbuilder"
        );
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        SelectOption choice = (SelectOption) delta[0];
        if (mode == Changer.ChangeMode.ADD) {
            for (SelectionMenu.Builder menu : getExpr().getAll(e)) {
                menu.addOptions(choice);
            }
        }
    }

    @Override
    protected String getPropertyName() {
        return "choices";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode.equals(Changer.ChangeMode.ADD))
            return CollectionUtils.array(SelectOption.class);
        return new Class[0];
    }

    @Nullable
    @Override
    public SelectOption[] convert(SelectionMenu.Builder menu) {
        return menu.getOptions().toArray(new SelectOption[0]);
    }

    @Override
    public Class<? extends SelectOption> getReturnType() {
        return SelectOption.class;
    }
}
