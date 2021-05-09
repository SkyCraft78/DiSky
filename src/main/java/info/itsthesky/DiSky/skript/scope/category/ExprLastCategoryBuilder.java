package info.itsthesky.disky.skript.scope.category;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.object.CategoryBuilder;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Last Category Builder")
@Description("Return the last used category builder.")
@Since("1.4.1")
public class ExprLastCategoryBuilder extends SimpleExpression<CategoryBuilder> {

    static {
        Skript.registerExpression(ExprLastCategoryBuilder.class, CategoryBuilder.class, ExpressionType.SIMPLE,
                "[the] [last] [(generated|created)] category [builder]"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Nullable
    @Override
    protected CategoryBuilder[] get(Event e) {
        return new CategoryBuilder[]{ScopeCategory.lastBuilder};
    }

    @Override
    public Class<? extends CategoryBuilder> getReturnType() {
        return CategoryBuilder.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the last category builder";
    }
}
