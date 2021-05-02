package info.itsthesky.DiSky.skript;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.DiSky.tools.UpdatedValue;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author ItsTheSky
 */
public class ExprUpdatedValue extends SimpleExpression<Object> {
    static {
        Skript.registerExpression(ExprUpdatedValue.class, Object.class, ExpressionType.SIMPLE,
                "[the] new %*classinfo%",
                "[the] old %*classinfo%"
        );
    }

    public static List<UpdatedValue<?>> values = new ArrayList<>();
    private UpdatedValue<?> value;
    private boolean isNew;

    @Override
    public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parser) {
        String cValue = parser.expr
                .replaceAll("new", "")
                .replaceAll("old", "")
                .replaceAll("the", "");
        String type = parser.expr.replaceAll(cValue, "");

        UpdatedValue<?> value = null;
        for (UpdatedValue<?> v : values) {
            if (v.getEventClass().equals(ScriptLoader.getCurrentEvents()[0])) {
                if (value.getClassInfo().equalsIgnoreCase(type)) {
                    value = v;
                }
            }
        }

        if (value == null) return false;

        this.isNew = matchedPattern != 0;
        this.value = value;
        return true;
    }

    @Override
    @Nullable
    protected Object[] get(final Event e) {
        if (isNew) {
            return value.isSingle() ? (value.getNewObject() == null ? new Object[0] : new Object[] {value.getNewObject()}) : (value.getNewObjectList() == null ? new Object[0] : value.getNewObjectList());
        } else {
            return value.isSingle() ? (value.getOldObject() == null ? new Object[0] : new Object[] {value.getOldObject()}) : (value.getOldObjectList() == null ? new Object[0] : value.getOldObjectList());
        }
    }

    @Override
    public Class<?> getReturnType() {
        return value.getClazz();
    }

    @Override
    public String toString(final @Nullable Event e, final boolean debug) {
        return isNew ? "new " : "old " + value.getClassInfo();
    }

    @Override
    public boolean isSingle() {
        return value.isSingle();
    }

}