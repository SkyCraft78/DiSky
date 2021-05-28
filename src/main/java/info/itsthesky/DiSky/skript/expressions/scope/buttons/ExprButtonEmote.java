package info.itsthesky.disky.skript.expressions.scope.buttons;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky.tools.object.ButtonBuilder;
import info.itsthesky.disky.tools.object.Emote;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Button Emote")
@Description("Change the emote of a button. It DOESN'T work with a CUSTOM emote!")
@Examples("set button emote of button to reaction \":x:\"")
@Since("1.12")
public class ExprButtonEmote extends SimplePropertyExpression<ButtonBuilder, Emote> {

    static {
        register(ExprButtonEmote.class, Emote.class,
                "[discord] [button] (emote|emoji)",
                "button"
        );
    }

    @Nullable
    @Override
    public Emote convert(ButtonBuilder entity) {
        return entity.getEmote();
    }

    @Override
    public Class<? extends Emote> getReturnType() {
        return Emote.class;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "button emote";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Emote.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (delta == null || delta.length == 0) return;
        if (!(delta[0] instanceof Emote)) return;
        Emote newState = (Emote) delta[0];
        if (mode == Changer.ChangeMode.SET) {
            for (ButtonBuilder entity : getExpr().getArray(e)) {
                entity.setEmote(newState);
            }
        }
    }
}