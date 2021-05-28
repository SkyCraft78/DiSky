package info.itsthesky.disky.skript.scope.buttons;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.object.ButtonBuilder;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Last Button Builder")
@Description("Return the last used button builder.")
@Since("1.12")
public class ExprLastButtonBuilder extends SimpleExpression<ButtonBuilder> {

    static {
        Skript.registerExpression(ExprLastButtonBuilder.class, ButtonBuilder.class, ExpressionType.SIMPLE,
                "[the] [last] [(generated|created)] button [builder]"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Nullable
    @Override
    protected ButtonBuilder[] get(Event e) {
        return new ButtonBuilder[]{ScopeButton.lastBuilder};
    }

    @Override
    public Class<? extends ButtonBuilder> getReturnType() {
        return ButtonBuilder.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the last button builder";
    }
}
