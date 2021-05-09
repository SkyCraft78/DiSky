package info.itsthesky.disky.skript.scope.role;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.object.RoleBuilder;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Last Role Builder")
@Description("Return the last used role builder.")
@Since("1.4")
public class ExprLastRoleBuilder extends SimpleExpression<RoleBuilder> {

    static {
        Skript.registerExpression(ExprLastRoleBuilder.class, RoleBuilder.class, ExpressionType.SIMPLE,
                "[the] [last] [(generated|created)] role [builder]"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Nullable
    @Override
    protected RoleBuilder[] get(Event e) {
        return new RoleBuilder[]{ScopeRole.lastBuilder};
    }

    @Override
    public Class<? extends RoleBuilder> getReturnType() {
        return RoleBuilder.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the last role builder";
    }
}
