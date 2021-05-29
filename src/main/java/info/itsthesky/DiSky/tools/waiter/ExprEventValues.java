package info.itsthesky.disky.tools.waiter;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.skript.events.skript.EventButtonClick;
import info.itsthesky.disky.skript.events.skript.EventButtonsSection;
import info.itsthesky.disky.skript.events.skript.EventReactSection;
import info.itsthesky.disky.skript.events.skript.EventReplySection;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author ItsTheSky
 * */
public class ExprEventValues extends SimpleExpression<Object> {
    static {
        Skript.registerExpression(ExprEventValues.class, Object.class, ExpressionType.SIMPLE,
                "event-%*classinfo%");
    }

    public static HashMap<Class<? extends Event>, List<EventValue<?>>> eventValues = new HashMap<>();

    private EventValue<?> value;

    @Override
    public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parser) {
        if (!ScriptLoader.isCurrentEvent(EventReactSection.class) &&
                !ScriptLoader.isCurrentEvent(EventReplySection.class) &&
                !ScriptLoader.isCurrentEvent(EventButtonsSection.class))
            return false;

        String cInfo = parser.expr.replaceAll("event-", "");

        List<EventValue<?>> eValues = eventValues.get(ScriptLoader.getCurrentEvents()[0]);
        if (eValues == null || eValues.isEmpty()) return false;
        EventValue<?> value = null;
        for (EventValue<?> vs : eValues) if (vs.getcInfo().equals(cInfo)) value = vs;
        if (value == null) return false;
        this.value = value;
        return true;
    }

    @Override
    @Nullable
    protected Object[] get(final Event e) {
        return new Object[] {value.getObject()};
    }

    @Override
    public Class<?> getReturnType() {
        return value.getaClass();
    }

    @Override
    public String toString(final @Nullable Event e, final boolean debug) {
        return "event-" + value.getcInfo();
    }

    @Override
    public boolean isSingle() {
        return true;
    }

}