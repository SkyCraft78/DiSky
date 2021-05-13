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
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Name("Message Receive")
@Description("Run when any message is sent and the bot is able to see it.")
@Examples({"on message receive:",
            "\tsend message (discord id of event-user) to event-channel with event-bot"})
@Since("1.0")
public class EventMessageReceive extends Event {

    static {
        // [seen by [bot] [(named|with name)]%string%]
        Skript.registerEvent("Message Receive", SimpleEvent.class, EventMessageReceive.class, "[discord] message receive")
        .description("Run when any message is sent and the bot is able to see it.")
        .examples("on message receive:",
                "\tsend message (discord id of event-user) to event-channel with event-bot")
        .since("1.0");

        EventValues.registerEventValue(EventMessageReceive.class, TextChannel.class, new Getter<TextChannel, EventMessageReceive>() {
            @Nullable
            @Override
            public TextChannel get(final @NotNull EventMessageReceive event) {
                return event.getEvent().getTextChannel();
            }
        }, 0);


        EventValues.registerEventValue(EventMessageReceive.class, GuildChannel.class, new Getter<GuildChannel, EventMessageReceive>() {
            @Nullable
            @Override
            public GuildChannel get(final @NotNull EventMessageReceive event) {
                return Utils.getChannel(event.getEvent().getTextChannel(), event.getEvent().getGuild());
            }
        }, 0);

        EventValues.registerEventValue(EventMessageReceive.class, Message.class, new Getter<Message, EventMessageReceive>() {
            @Nullable
            @Override
            public Message get(final @NotNull EventMessageReceive event) {
                return event.getEvent().getMessage();
            }
        }, 0);

        EventValues.registerEventValue(EventMessageReceive.class, User.class, new Getter<User, EventMessageReceive>() {
            @Nullable
            @Override
            public User get(final @NotNull EventMessageReceive event) {
                return event.getEvent().getMember() == null ? null : event.getEvent().getMember().getUser();
            }
        }, 0);

        EventValues.registerEventValue(EventMessageReceive.class, JDA.class, new Getter<JDA, EventMessageReceive>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventMessageReceive event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventMessageReceive.class, Guild.class, new Getter<Guild, EventMessageReceive>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventMessageReceive event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventMessageReceive.class, Member.class, new Getter<Member, EventMessageReceive>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventMessageReceive event) {
                return event.getEvent().getMember();
            }
        }, 0);


    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final MessageReceivedEvent event;

    public EventMessageReceive(
            final MessageReceivedEvent e
            ) {
        super(Utils.areEventAsync());
        this.event = e;
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
}