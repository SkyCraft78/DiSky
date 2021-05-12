package info.itsthesky.disky.skript;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.UpdatedValue;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

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
                .replaceAll("new ", "")
                .replaceAll("old ", "")
                .replaceAll("the ", "");

        boolean shouldContinue = false;
        if (maps.get(ScriptLoader.getCurrentEvents()[0]) != null) {
            if (value.matches(maps.get(ScriptLoader.getCurrentEvents()[0]).getName())) {
                shouldContinue = true;
            }
        }
        if (!shouldContinue) {
            // TODO Idk if it's better to warn the user or pass that expression
            //Skript.error("Cannot use an updated value in a " + ScriptLoader.getCurrentEventName() + " event", ErrorQuality.SEMANTIC_ERROR);
            return false;
        }

        this.value = maps.get(ScriptLoader.getCurrentEvents()[0]);
        this.isNew = matchedPattern != 0;
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