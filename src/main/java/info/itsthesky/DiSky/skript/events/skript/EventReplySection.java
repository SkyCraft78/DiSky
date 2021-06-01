package info.itsthesky.disky.skript.events.skript;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.entities.Message;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventReplySection extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    static {
        EventValues.registerEventValue(EventReplySection.class, Message.class, new Getter<Message, EventReplySection>() {
            @Nullable
            @Override
            public Message get(final @NotNull EventReplySection event) {
                return event.getMessage();
            }
        }, 0);
    }

    private final Message message;

    public EventReplySection(
            final Message message
            ) {
        super(Utils.areEventAsync());
        this.message = message;

        Expression e = new ExpressionBuilder("3 * sin(y) - 2 / (x - 2)")
                .variables("x", "y")
                .build()
                .setVariable("x", 2.3)
                .setVariable("y", 3.14);
        double result = e.evaluate();
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