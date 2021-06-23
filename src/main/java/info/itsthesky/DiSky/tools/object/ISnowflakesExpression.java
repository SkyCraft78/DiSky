package info.itsthesky.disky.tools.object;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.entities.ISnowflake;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ISnowflakesExpression<E extends ISnowflake> extends SimpleExpression<E> {

    private Expression<String> exprInput;
    private Class<E> clazz;

    @SuppressWarnings("unchecked")
    void register(final Class<E> entityClazz, final String entityName) {
        clazz = entityClazz;

    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprInput = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends E> getReturnType() {
        return clazz;
    }

    public abstract E getEntity(Event e, @NotNull String input);

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    protected E[] get(Event e) {
        if (exprInput == null || exprInput.getSingle(e) == null) return (E[]) new Object[0];
        if (!Utils.isNumeric(exprInput.getSingle(e))) return (E[]) new Object[0];
        return (E[]) new ISnowflake[] {getEntity(e, exprInput.getSingle(e))};
    }

    @Override
    @SuppressWarnings("unchecked")
    public String toString(@Nullable Event e, boolean debug) {
        return clazz.getSimpleName() + " with id " + exprInput.toString(e, debug);
    }
}
