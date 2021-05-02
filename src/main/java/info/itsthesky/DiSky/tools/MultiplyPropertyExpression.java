package info.itsthesky.DiSky.tools;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

/**
 * Way to make SimplePropertyExpression return multiple objects
 * Copyright (C) 2021 @Olyno
 */
public abstract class MultiplyPropertyExpression<F, T> extends SimpleExpression<T> {

    private Expression<? extends F> expr;

    protected static <T> void register(final Class<? extends Expression<T>> c, final Class<T> type, final String property, final String fromType) {
        Skript.registerExpression(c, type, ExpressionType.SIMPLE,
                "[all] [the] " + property + " of %" + fromType + "%",
                "[all] [the] %" + fromType + "%'[s] " + property
        );
    }

    public abstract Class<? extends T> getReturnType();

    protected abstract String getPropertyName();

    protected abstract T[] convert(F t);

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(final Expression<?>[] expr, final int matchedPattern, final Kleenean isDelayed, final SkriptParser.ParseResult parseResult) {
        this.expr = (Expression<? extends F>) expr[0];
        return true;
    }

    @Override
    protected T[] get(Event e) {
        return convert(expr.getSingle(e));
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public String toString(final @Nullable Event e, final boolean debug) {
        return "the " + getPropertyName() + " of " + expr.toString(e, debug);
    }

    public Expression<? extends F> getExpr() {
        return expr;
    }
}
