package info.itsthesky.disky.tools.events;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.config.Config;
import ch.njol.skript.lang.*;
import ch.njol.skript.log.SkriptLogger;
import info.itsthesky.disky.managers.BotManager;
import info.itsthesky.disky.tools.Utils;
import org.bukkit.event.Event;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Made by Blitz, minor edit by Sky for DiSky
 */
public abstract class DiSkyEvent<D extends net.dv8tion.jda.api.events.Event> extends SelfRegisteringSkriptEvent {

    /**
     * The ending appended to patterns if no custom ending is specified
     */
    public static final String APPENDED_ENDING = "[seen by %-string%]";
    private Map<Class<?>, Object> valueMap = new HashMap<>();
    private String stringRepresentation;
    private Trigger trigger;
    private EventListener<D> listener;
    private String bot;
    private Class<? extends org.bukkit.event.Event> bukkitClass;
    private Class<D> jdaClass;
    private String originalName;
    private Class<? extends Event>[] originalEvents;
    private Constructor<?> constructor;

    /**
     * @param name     The name of the event used for ScriptLoader#setCurrentEvents
     * @param type     The class holding the event
     * @param clazz    The class holding the Bukkit event
     * @param patterns The patterns for the event
     */
    public static Registration register(String name, Class type, Class clazz, String... patterns) {
        return register(name, APPENDED_ENDING, type, clazz, patterns);
    }

    /**
     * @param name     The name of the event used for ScriptLoader#setCurrentEvents
     * @param ending   The ending applied for checking the bot (which can be grabbed via BaseEvent.APPENDED_ENDING)
     * @param type     The class holding the event
     * @param clazz    The class holding the Bukkit event
     * @param patterns The patterns for the event
     */
    @SuppressWarnings("unchecked")
    public static <T extends SimpleDiSkyEvent<?>> Registration register(String name, String ending, Class type, Class<T> clazz, String... patterns) {
        for (int i = 0; i < patterns.length; i++) {
            patterns[i] += " " + ending;
        }
        Skript.registerEvent(name, type, clazz, patterns);
        return new Registration(clazz, patterns);
    }


    @Override
     @SuppressWarnings("unchecked")
    public boolean init(Literal<?>[] exprs, int matchedPattern, SkriptParser.ParseResult parser) {
        bot = (String) (exprs[0] == null ? null : exprs[0].getSingle());

        bukkitClass = (Class<? extends org.bukkit.event.Event>) Arrays.stream(this.getClass().getDeclaredClasses())
                .filter(innerClass -> innerClass.getSuperclass() == SimpleDiSkyEvent.class)
                .findFirst()
                .orElse(null);

        if (bukkitClass == null) {
            throw new RuntimeException(this.getClass().getCanonicalName() + " doesn't have an inner SimpleDiSkyEvent " +
                    "class to be instantiated. Report this at https://github.com/SkyCraft78/DiSky/issues!");
        }

        try {
            jdaClass = (Class<D>) ((ParameterizedType) bukkitClass.getGenericSuperclass()).getActualTypeArguments()[0];
        } catch (ClassCastException e) {
            throw new RuntimeException(this.getClass().getCanonicalName() + "'s inner class doesn't use the same JDA" +
                    " event as it's parent class in it's SimpleDiSkyEvent. Report this at https://github.com/SkyCraft78/DiSky/issues!");
        }

        try {
            constructor = bukkitClass.getDeclaredConstructor(this.getClass());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        stringRepresentation = ScriptLoader.replaceOptions(SkriptLogger.getNode().getKey()) + ":";
        originalName = ScriptLoader.getCurrentEventName();
        originalEvents = ScriptLoader.getCurrentEvents();

        String name = null;
        for (SkriptEventInfo<?> event : Skript.getEvents()) {
            if (bukkitClass.equals(event.c)) {
                name = event.getName();
            }
        }

        ScriptLoader.setCurrentEvent(name == null ? "DiSky event" : name, bukkitClass);
        return true;
    }

    @Override
    public void afterParse(Config config) {
        ScriptLoader.setCurrentEvent(originalName, originalEvents);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void register(Trigger t) {
        trigger = t;
        listener = new EventListener<>(jdaClass, JDAEvent -> {
            if (check(JDAEvent)) {

                /* !? */
                SimpleDiSkyEvent<D> eventWorkaround = null;
                SimpleDiSkyEvent<D> event;
                try {
                    eventWorkaround = (SimpleDiSkyEvent<D>) constructor.newInstance(DiSkyEvent.this);
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                event = eventWorkaround;

                event.setJDAEvent(JDAEvent);
                Utils.async(() -> {
                    for (DiSkyEvent<?>.Value value : getValues()) {
                        valueMap.put(value.clazz, value.getter.get(JDAEvent));
                    }
                    event.setValueMap(valueMap);
                    Utils.sync(() -> {
                        if (getTrigger() != null) {
                            getTrigger().execute(event);
                        }
                    });

                });

            }
        });
        EventListener.addListener(listener);
    }

    @Override
    public void unregister(final Trigger t) {
        listener.enabled = false;
        EventListener.removeListener(listener);
        listener = null;
        trigger = null;
    }

    @Override
    public void unregisterAll() {
        unregister(trigger);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return stringRepresentation;
    }

    public String getBot() {
        return bot;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    /**
     * Used to check whether or not the event is valid for the trigger to run.
     *
     * @param event The JDA event to be checked
     */
    public boolean check(D event) {
        return bot == null || bot.equalsIgnoreCase(BotManager.getNameByJDA(event.getJDA()));
    }

    public Class<? extends Event> getBukkitClass() {
        return bukkitClass;
    }

    public Class<D> getJDAClass() {
        return jdaClass;
    }

    @SuppressWarnings("unchecked")
    public Value[] getValues() {
        return new DiSkyEvent.Value[]{};
    }

    public class Value {

        private final DiSkyGetter<D, Object> getter;
        private final Class<?> clazz;

        public Value(Class<?> clazz, DiSkyGetter<D, Object> getter) {
            this.getter = getter;
            this.clazz = clazz;
        }

    }

}