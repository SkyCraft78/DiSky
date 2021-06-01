package info.itsthesky.disky.skript.expressions.scope.buttons;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky.tools.object.ButtonBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Button Emoji")
@Description("Change the emote of a button. It DOESN'T work with a CUSTOM emote!")
@Examples("set button emote of button to \"⚠️\"")
@Since("1.12")
public class ExprButtonEmote extends SimplePropertyExpression<ButtonBuilder, String> {

    static {
        register(ExprButtonEmote.class, String.class,
                "[discord] [button] (emote|emoji)",
                "button"
        );
    }

    @Nullable
    @Override
    public String convert(ButtonBuilder entity) {
        if (entity.getEmoji() == null) return null;
        return entity.getEmoji().getAsMention();
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "button emote";
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
        String newState = (String) delta[0];
        if (mode == Changer.ChangeMode.SET) {
            for (ButtonBuilder entity : getExpr().getArray(e)) {
                entity.setEmoji(Emoji.ofUnicode(newState));
            }
        }
    }
}