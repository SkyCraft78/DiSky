package info.itsthesky.disky.skript.scope.component;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Last Selection Menu Builder")
@Description("Return the last used selection menu builder.")
@Since("2.0")
public class ExprLastSelectBuilder extends SimpleExpression<SelectionMenu.Builder> {

    static {
        Skript.registerExpression(ExprLastSelectBuilder.class, SelectionMenu.Builder.class, ExpressionType.SIMPLE,
                "[the] [last] [(generated|created)] (dropdown|select[s]|selection menu) [builder]"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Nullable
    @Override
    protected SelectionMenu.Builder[] get(Event e) {
        return new SelectionMenu.Builder[]{ScopeSelection.lastBuilder};
    }

    @Override
    public Class<? extends SelectionMenu.Builder> getReturnType() {
        return SelectionMenu.Builder.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the last selection menu builder";
    }
}
