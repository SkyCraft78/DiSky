package info.itsthesky.disky.skript.events.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.Utils;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Name("On DiSky Exception")
@Description("Fired when DiSky receive an internal error (throwable or exception), use 'last disky error' to get it, or use event-string.")
@Examples("on disky exception:")
@Since("1.8")
public class EventDiSkyException extends Event {

    static {
        Skript.registerEvent("DiSky Exception", SimpleEvent.class, EventDiSkyException.class, "[discord] [guild] disky (exception|error|fatal|throwable)");

        EventValues.registerEventValue(EventDiSkyException.class, String.class, new Getter<String, EventDiSkyException>() {
            @Nullable
            @Override
            public String get(final @NotNull EventDiSkyException event) {
                return event.getMessage();
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();
    private final String message;

    public EventDiSkyException(
            final String message
            ) {
        super(Utils.areEventAsync());
        this.message = message;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public String getMessage() {
        return message;
    }
}