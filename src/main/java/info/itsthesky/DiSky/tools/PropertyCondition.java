package info.itsthesky.disky.tools;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAPIException;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Checker;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

/**
 * @author SkriptLang team
 */
public abstract class PropertyCondition<T> extends Condition implements Checker<T> {
    private Expression<? extends T> expr;

    public PropertyCondition() {
    }

    public static void register(Class<? extends Condition> c, String property, String type) {
        register(c, PropertyCondition.PropertyType.BE, property, type);
    }

    public static void register(Class<? extends Condition> c, PropertyCondition.PropertyType propertyType, String property, String type) {
        if (type.contains("%")) {
            throw new SkriptAPIException("The type argument must not contain any '%'s");
        } else {
            switch(propertyType) {
            case BE:
                Skript.registerCondition(c, "%" + type + "% (is|are) " + property, "%" + type + "% (isn't|is not|aren't|are not) " + property);
                break;
            case CAN:
                Skript.registerCondition(c, "%" + type + "% can " + property, "%" + type + "% (can't|cannot|can not) " + property);
                break;
            case HAVE:
                Skript.registerCondition(c, "%" + type + "% (has|have) " + property, "%" + type + "% (doesn't|does not|do not|don't) have " + property);
                break;
            default:
                assert false;
            }

        }
    }

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        this.expr = (Expression<? extends T>) exprs[0];
        this.setNegated(matchedPattern == 1);
        return true;
    }

    public final boolean check(Event e) {
        return this.expr.check(e, this, this.isNegated());
    }

    public abstract boolean check(T var1);

    protected abstract String getPropertyName();

    protected PropertyCondition.PropertyType getPropertyType() {
        return PropertyCondition.PropertyType.BE;
    }

    protected final void setExpr(Expression<? extends T> expr) {
        this.expr = expr;
    }

    public String toString(@Nullable Event e, boolean debug) {
        return toString(this, this.getPropertyType(), e, debug, this.expr, this.getPropertyName());
    }

    public static String toString(Condition condition, PropertyCondition.PropertyType propertyType, @Nullable Event e, boolean debug, Expression<?> expr, String property) {
        switch(propertyType) {
        case BE:
            return expr.toString(e, debug) + (expr.isSingle() ? " is " : " are ") + (condition.isNegated() ? "not " : "") + property;
        case CAN:
            return expr.toString(e, debug) + (condition.isNegated() ? " can't " : " can ") + property;
        case HAVE:
            if (expr.isSingle()) {
                return expr.toString(e, debug) + (condition.isNegated() ? " doesn't have " : " has ") + property;
            }

            return expr.toString(e, debug) + (condition.isNegated() ? " don't have " : " have ") + property;
        default:
            assert false;

            throw new AssertionError();
        }
    }

    public enum PropertyType {
        BE,
        CAN,
        HAVE;
        PropertyType() { }
    }
}
