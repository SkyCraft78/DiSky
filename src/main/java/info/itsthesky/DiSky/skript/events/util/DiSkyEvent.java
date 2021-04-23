package info.itsthesky.DiSky.skript.events.util;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.util.function.Consumer;

/**
 * @author ItsTheSky
 */
public class DiSkyEvent<E> extends ListenerAdapter {

    private final Consumer<E> launched;
    private final Class<E> clazz;

    public DiSkyEvent(Class<E> clazz, Consumer<E> launched) {
        this.clazz = clazz;
        this.launched = launched;
    }

    public Consumer<E> getConsumer() {
        return launched;
    }
    public Class<E> getClazz() {
        return clazz;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onGenericEvent(GenericEvent event) {
        if (getClazz().equals(event.getClass())) {
            E e = (E) event;
            getConsumer().accept(e);
        }
    }
}
