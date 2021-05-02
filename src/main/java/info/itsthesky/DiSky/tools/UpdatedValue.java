package info.itsthesky.DiSky.tools;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.registrations.Classes;
import info.itsthesky.DiSky.skript.ExprUpdatedValue;
import org.bukkit.event.Event;

public class UpdatedValue<T> {

    private final Class<T> clazz;
    private final String classInfo;
    private T newObject;
    private T oldObject;
    private T[] newObjectList;
    private T[] oldObjectList;
    private final boolean isSingle;
    private Class<? extends Event> eventClass;

    @SuppressWarnings("unchecked")
    public UpdatedValue(Class<? extends Event> eventClass, String classInfo, boolean isSingle) {
        this.clazz = (Class<T>) Classes.getClass(classInfo);
        this.isSingle = isSingle;
        this.classInfo = classInfo;
        this.eventClass = eventClass;
    }

    public UpdatedValue<T> register() {
        ExprUpdatedValue.values.add(this);
        return this;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public String getClassInfo() {
        return classInfo;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public T getNewObject() {
        return newObject;
    }

    public void setNewObject(T newObject) {
        this.newObject = newObject;
    }

    public T getOldObject() {
        return oldObject;
    }

    public void setOldObject(T oldObject) {
        this.oldObject = oldObject;
    }

    public T[] getNewObjectList() {
        return newObjectList;
    }

    public void setNewObjectList(T[] newObjectList) {
        this.newObjectList = newObjectList;
    }

    public T[] getOldObjectList() {
        return oldObjectList;
    }

    public void setOldObjectList(T[] oldObjectList) {
        this.oldObjectList = oldObjectList;
    }

    public Class<? extends Event> getEventClass() {
        return eventClass;
    }

    public void setEventClass(Class<? extends Event> eventClass) {
        this.eventClass = eventClass;
    }
}
