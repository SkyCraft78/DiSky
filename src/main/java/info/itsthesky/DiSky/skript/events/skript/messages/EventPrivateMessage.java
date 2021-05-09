package info.itsthesky.disky.skript.events.skript.messages;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Name("Private Message")
@Description("Run when any message is sent to the bot's DM")
@Examples({"on private message receive:",
            "\treply with \"I'm not a human, you can't talk to me :c\""})
@Since("1.1")
public class EventPrivateMessage extends Event {

    static {
        Skript.registerEvent("Private Message", SimpleEvent.class, EventPrivateMessage.class, "[discord] (private|direct) message receive");

        EventValues.registerEventValue(EventPrivateMessage.class, User.class, new Getter<User, EventPrivateMessage>() {
            @Nullable
            @Override
            public User get(final @NotNull EventPrivateMessage event) {
                return event.getEvent().getAuthor();
            }
        }, 0);

        EventValues.registerEventValue(EventPrivateMessage.class, Message.class, new Getter<Message, EventPrivateMessage>() {
            @Nullable
            @Override
            public Message get(final @NotNull EventPrivateMessage event) {
                return event.getEvent().getMessage();
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final MessageReceivedEvent e;

    public EventPrivateMessage(
            final MessageReceivedEvent e
            ) {
        super(Utils.areEventAsync());
        this.e = e;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public MessageReceivedEvent getEvent() {
        return e;
    }
}