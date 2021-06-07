package info.itsthesky.disky.skript.events.skript.messages;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.skript.effects.messages.EffReplyWith;
import info.itsthesky.disky.skript.events.util.MessageEvent;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.JDA;
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
public class EventPrivateMessage extends Event implements MessageEvent {

    static {
        Skript.registerEvent("Private Message", SimpleEvent.class, EventPrivateMessage.class, "[discord] (private|direct) message receive")
        .description("Run when any message is sent to the bot's DM")
        .examples("on private message receive:",
                "\treply with \"I'm not a human, you can't talk to me :c\"")
        .since("1.1");

        EventValues.registerEventValue(EventPrivateMessage.class, User.class, new Getter<User, EventPrivateMessage>() {
            @Nullable
            @Override
            public User get(final @NotNull EventPrivateMessage event) {
                return event.getEvent().getAuthor();
            }
        }, 0);

        EventValues.registerEventValue(EventPrivateMessage.class, JDA.class, new Getter<JDA, EventPrivateMessage>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventPrivateMessage event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventPrivateMessage.class, UpdatingMessage.class, new Getter<UpdatingMessage, EventPrivateMessage>() {
            @Nullable
            @Override
            public UpdatingMessage get(final @NotNull EventPrivateMessage event) {
                return UpdatingMessage.from(event.getEvent().getMessage());
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

    @Override
    public MessageChannel getChannel() {
        return getEvent().getChannel();
    }
}