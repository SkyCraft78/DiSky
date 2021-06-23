package info.itsthesky.disky.tools.events;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Made by Blitz, minor edit by Sky for DiSky
 */
public class SimpleDiSkyEvent<D extends net.dv8tion.jda.api.events.Event> extends BukkitEvent {

    private D JDAEvent;
    private Map<Class<?>, Object> valueMap = new HashMap<>();

    public D getJDAEvent() {
        return JDAEvent;
    }

    public void setJDAEvent(D JDAEvent) {
        this.JDAEvent = JDAEvent;
    }

    public Map<Class<?>, Object> getValueMap() {
        return valueMap;
    }

    public void setValueMap(Map<Class<?>, Object> valueMap) {
        this.valueMap = valueMap;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(Class<T> clazz) {
        return (T) valueMap.get(clazz);
    }

}