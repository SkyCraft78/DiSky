package info.itsthesky.disky.skript.expressions.scope.buttons;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky.tools.object.ButtonBuilder;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Button Style")
@Description("Change the style of a button. Check 'buttonstyle' type for more information.")
@Examples("set button style of button to danger")
@Since("1.12")
public class ExprButtonStyle extends SimplePropertyExpression<ButtonBuilder, ButtonStyle> {

    static {
        register(ExprButtonStyle.class, ButtonStyle.class,
                "[discord] [button] style",
                "button"
        );
    }

    @Nullable
    @Override
    public ButtonStyle convert(ButtonBuilder entity) {
        return entity.getColor();
    }

    @Override
    public Class<? extends ButtonStyle> getReturnType() {
        return ButtonStyle.class;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "button style";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(ButtonStyle.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (delta == null || delta.length == 0) return;
        if (!(delta[0] instanceof ButtonStyle)) return;
        ButtonStyle newState = (ButtonStyle) delta[0];
        if (mode == Changer.ChangeMode.SET) {
            for (ButtonBuilder entity : getExpr().getArray(e)) {
                entity.setColor(newState);
            }
        }
    }
}