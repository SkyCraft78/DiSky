package info.itsthesky.disky.tools.waiter;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Listener for react section.
 * @author ItstheSky (12/04/2021)
 */
public class WaiterListener extends ListenerAdapter {

    public static List<WaitingEvent> events = new ArrayList<>();

    /**
     * The main event listener method.
     * Check every waiting event stored in the list, verify their predicate and execute their runnable.
     * Mainly used for SectionReact, but could be transformed for other section.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void onGenericEvent(GenericEvent event) {
        events.forEach((waiter) -> {
            if (!waiter.getClazz().equals(event.getClass())) return;
            if (waiter.getVerify().test(event)) waiter.getConsumer().accept(event);
        });
    }

    /* @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        events.forEach((waiter) -> {
            if (waiter.getVerify().test(event)) waiter.getConsumer().accept(event);
        });
    } */

    /**
     * This object store the event itself, the predicate and the runnable to execute.
     */
    public static class WaitingEvent<E extends GenericEvent> {

        private Consumer<E> consumer;
        private Predicate<E> verify;
        private Class<E> clazz;

        public WaitingEvent(Class<E> clazz, Predicate<E> verify, Consumer<E> consumer) {
            this.consumer = consumer;
            this.verify = verify;
            this.clazz = clazz;
        }

        public Class<E> getClazz() {
            return clazz;
        }

        public Consumer<E> getConsumer() {
            return consumer;
        }
        public void setConsumer(Consumer<E> consumer) {
            this.consumer = consumer;
        }
        public Predicate<E> getVerify() {
            return verify;
        }
    }

}
