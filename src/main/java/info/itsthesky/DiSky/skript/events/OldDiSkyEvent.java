/*package info.itsthesky.DiSky.skript.events;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public abstract class DiSkyEvent<E> extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public DiSkyEvent() {
        super(true);
    }

    public <T> void registerEventValue(final T object, final Class<E> eventClass, final Class<T> valueClass, final Function<E, T> function) {
        EventValues.registerEventValue(eventClass, valueClass, new Getter<T, E>() {
            @Nullable
            @Override
            public T get(final @NotNull E event) {
                return function.apply(event);
            }
        }, 0);
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    public E getEvent() {
        return event;
    }
}*/