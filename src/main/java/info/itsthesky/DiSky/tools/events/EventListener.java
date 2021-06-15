package info.itsthesky.disky.tools.events;

import info.itsthesky.disky.managers.BotManager;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Made by Blitz, minor edit by Sky for DiSky
 */
public class EventListener<T> extends ListenerAdapter {

    public final static ArrayList<EventListener<?>> listeners = new ArrayList<>();
    public boolean enabled = true;
    private final Class<T> clazz;
    private final Consumer<T> consumer;

    public EventListener(Class<T> paramClass, Consumer<T> consumer) {
        this.clazz = paramClass;
        this.consumer = consumer;
    }

    public static void addListener(EventListener<?> listener) {
        removeListener(listener);
        listeners.add(listener);
        BotManager.getBotsJDA().forEach(jda -> jda.addEventListener(listener));
    }

    public static void removeListener(EventListener<?> listener) {
        listeners.remove(listener);
        BotManager.getBotsJDA().forEach(jda -> jda.removeEventListener(listener));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onGenericEvent(net.dv8tion.jda.api.events.GenericEvent event) {
        if (enabled && clazz.isInstance(event)) {
            consumer.accept((T) event);
        }
    }

}