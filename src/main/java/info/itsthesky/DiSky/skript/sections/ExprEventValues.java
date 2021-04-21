package info.itsthesky.DiSky.skript.sections;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Utils;
import ch.njol.util.Kleenean;
import ch.njol.util.StringUtils;
import info.itsthesky.DiSky.skript.commands.Argument;
import info.itsthesky.DiSky.skript.commands.CommandEvent;
import info.itsthesky.DiSky.skript.commands.CommandFactory;
import info.itsthesky.DiSky.skript.events.skript.EventReactSection;
import info.itsthesky.DiSky.tools.StaticData;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * @author Sky
 * */
public class ExprEventValues extends SimpleExpression<Object> {
    static {
        Skript.registerExpression(ExprEventValues.class, Object.class, ExpressionType.SIMPLE,
                "event-%*classinfo%");
    }

    public static HashMap<String, EventValue<?>> values = new HashMap<>();
    private String cInfo;
    private EventValue<?> value;

    @Override
    public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parser) {
        if (!ScriptLoader.isCurrentEvent(EventReactSection.class))
            return false;
        String cInfo = parser.expr.replaceAll("event-", "");
        if (!values.containsKey(cInfo)) {
            Skript.error("There's no event-" + cInfo + " in a react section event");
            return false;
        }
        value = values.get(cInfo);
        this.cInfo = cInfo;
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