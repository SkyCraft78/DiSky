package info.itsthesky.DiSky.tools;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

/**
 * Simple class to create custom "new" and "old" expression value.
 * Useful in update event.
 * @param <T> The object of the value
 * @author ItsTheSky
 */
public class OldUpdatedValue<T> extends SimpleExpression<T> {

    private Class<T> clazz;
    private String name;
    private T newObject;
    private T oldObject;
    private T[] newObjectList;
    private T[] oldObjectList;
    private boolean isSingle;
    private boolean isNew;
    private Class<? extends Event> eventClass;

    public void setNewObject(T object) {
        this.newObject = object;
    }
    public void setOldObject(T object) {
        this.oldObject = object;
    }
    public void setNewObjectList(T[] objects) {
        this.newObjectList = objects;
    }
    public void setOldObjectList(T[] objects) {
        this.oldObjectList = objects;
    }


    public OldUpdatedValue() {
        System.out.println("empty");
    }

    @SuppressWarnings("unchecked")
    public OldUpdatedValue(Class<T> valueClass, Class<? extends Event> eventClass, String valueName, boolean isSingle) {
        this.clazz = valueClass;
        this.isSingle = isSingle;
        this.name = valueName;
        this.eventClass = eventClass;
        Skript.registerExpression(this.getClass(), valueClass, ExpressionType.SIMPLE,
                "["+ Utils.getPrefixName() +"] " + "new " + valueName,
                "["+ Utils.getPrefixName() +"] " + "old " + valueName
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!ScriptLoader.getCurrentEventName().equalsIgnoreCase("on guild name update")) return false;
        this.isNew = matchedPattern != 0;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected T[] get(Event e) {
        if (isNew) {
            return isSingle ? (newObject == null ? (T[]) new Object[0] : (T[]) new Object[] {newObject}) : (newObjectList == null ? (T[]) new Object[0] : newObjectList);
        } else {
            return isSingle ? (oldObject == null ? (T[]) new Object[0] : (T[]) new Object[] {oldObject}) : (oldObjectList == null ? (T[]) new Object[0] : oldObjectList);
        }
    }

    @Override
    public boolean isSingle() {
        return isSingle;
    }

    @Override
    public Class<? extends T> getReturnType() {
        return clazz;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return isNew ? "new " + name : "old " + name;
    }

}
