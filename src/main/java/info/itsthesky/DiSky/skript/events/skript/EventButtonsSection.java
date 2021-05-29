package info.itsthesky.disky.skript.events.skript;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.entities.Message;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventButtonsSection extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    static {
        EventValues.registerEventValue(EventButtonsSection.class, Message.class, new Getter<Message, EventButtonsSection>() {
            @Nullable
            @Override
            public Message get(final @NotNull EventButtonsSection event) {
                return event.getMessage();
            }
        }, 0);
    }

    private final Message message;

    public EventButtonsSection(
            final Message message
            ) {
        super(Utils.areEventAsync());
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private boolean isCancelled = false;

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        isCancelled = cancel;
    }
}