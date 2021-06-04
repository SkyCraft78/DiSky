package info.itsthesky.disky.skript.events.skript;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.skript.events.util.MessageEvent;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventReplySection extends Event implements Cancellable, MessageEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    static {
        EventValues.registerEventValue(EventReplySection.class, Message.class, new Getter<Message, EventReplySection>() {
            @Nullable
            @Override
            public Message get(final @NotNull EventReplySection event) {
                return event.getEvent().getMessage();
            }
        }, 0);
    }

    private final MessageReceivedEvent event;

    public EventReplySection(
            final MessageReceivedEvent event
            ) {
        super(Utils.areEventAsync());
        this.event = event;
    }

    public MessageReceivedEvent getEvent() {
        return event;
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

    @Override
    public MessageChannel getChannel() {
        return event.getChannel();
    }
}