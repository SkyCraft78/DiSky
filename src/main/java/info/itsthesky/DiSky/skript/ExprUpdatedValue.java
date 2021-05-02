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

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author ItsTheSky
 */
public class ExprUpdatedValue extends SimpleExpression<Object> {
    static {
        Skript.registerExpression(ExprUpdatedValue.class, Object.class, ExpressionType.SIMPLE,
                "[the] new <.+>",
                "[the] old <.+>"
        );
    }

    public static HashMap<Class<? extends Event>, UpdatedValue<?>> maps = new HashMap<>();
    private UpdatedValue<?> value;
    private boolean isNew;

    @Override
    public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parser) {
        String value = parser.expr
                .replaceAll("new", "")
                .replaceAll("old", "")
                .replaceAll("the", "");
        String type = parser.expr.replaceAll(value, "");

        AtomicBoolean shouldContinue = new AtomicBoolean(true);
        maps.forEach((e, v) -> {
            if (!v.getName().equals(value)) shouldContinue.set(false);
        });
        if (!shouldContinue.get()) return false;

        AtomicReference<UpdatedValue<?>> values = new AtomicReference<>(null);
        maps.forEach((e, v) -> Arrays.asList(ScriptLoader.getCurrentEvents()).forEach(ev -> {
            if (e.equals(ev)) values.set(v);
        }));
        if (values.get() == null) {
            Skript.error("The "+ type +"" +value + " can't be used in a "+ScriptLoader.getCurrentEventName()+" Skript event.");
            return false;
        }
        this.isNew = matchedPattern != 0;
        this.value = values.get();
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
        return "new or old " + value.getName();
    }

    @Override
    public boolean isSingle() {
        return value.isSingle();
    }
}