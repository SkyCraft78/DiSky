package info.itsthesky.disky.skript.expressions.scope.buttons;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky.tools.object.ButtonBuilder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Button Content")
@Description("Change the content of a button builder. Use the 'button emote' for managing emote!")
@Examples("set button content of button to \"Hello World :D\"")
@Since("1.12")
public class ExprButtonContent extends SimplePropertyExpression<ButtonBuilder, String> {

    static {
        register(ExprButtonContent.class, String.class,
                "[discord] [button] (content|text)",
                "button"
        );
    }

    @Nullable
    @Override
    public String convert(ButtonBuilder entity) {
        return entity.getContent();
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "button content";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(String.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (delta == null || delta.length == 0) return;
        if (!(delta[0] instanceof String)) return;
        String newState = (String) delta[0];
        if (mode == Changer.ChangeMode.SET) {
            for (ButtonBuilder entity : getExpr().getArray(e)) {
                entity.setContent(newState);
            }
        }
    }
}