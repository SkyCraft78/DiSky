package info.itsthesky.DiSky.skript.events.skript.reaction;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.DiSky.tools.Utils;
import info.itsthesky.DiSky.tools.object.Emote;
import info.itsthesky.DiSky.tools.object.messages.Channel;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Name("Reaction Add")
@Description("Run when any message receive a reaction. Use event-string to get the message id.")
@Examples("on reaction add:")
@Since("1.3")
public class EventReactionAdd extends Event {

    static {
        Skript.registerEvent("Reaction Add", SimpleEvent.class, EventReactionAdd.class, "[discord] [guild] reaction add");

        EventValues.registerEventValue(EventReactionAdd.class, Message.class, new Getter<Message, EventReactionAdd>() {
            @Nullable
            @Override
            public Message get(final @NotNull EventReactionAdd event) {
                return event.getEvent().getChannel()
                        .retrieveMessageById(event.getEvent().getMessageId()).complete();
            }
        }, 0);

        EventValues.registerEventValue(EventReactionAdd.class, User.class, new Getter<User, EventReactionAdd>() {
            @Nullable
            @Override
            public User get(final @NotNull EventReactionAdd event) {
                return event.getEvent().getUser();
            }
        }, 0);

        EventValues.registerEventValue(EventReactionAdd.class, Member.class, new Getter<Member, EventReactionAdd>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventReactionAdd event) {
                return event.getEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(EventReactionAdd.class, Channel.class, new Getter<Channel, EventReactionAdd>() {
            @Nullable
            @Override
            public Channel get(final @NotNull EventReactionAdd event) {
                return new Channel(event.getEvent().getChannel());
            }
        }, 0);

        EventValues.registerEventValue(EventReactionAdd.class, JDA.class, new Getter<JDA, EventReactionAdd>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventReactionAdd event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventReactionAdd.class, TextChannel.class, new Getter<TextChannel, EventReactionAdd>() {
            @Nullable
            @Override
            public TextChannel get(final @NotNull EventReactionAdd event) {
                return new Channel(event.getEvent().getChannel()).getTextChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventReactionAdd.class, Guild.class, new Getter<Guild, EventReactionAdd>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventReactionAdd event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventReactionAdd.class, Emote.class, new Getter<Emote, EventReactionAdd>() {
            @Nullable
            @Override
            public Emote get(final @NotNull EventReactionAdd event) {
                return event.getEvent().getReactionEmote().isEmote() ? new Emote(event.getEvent().getReactionEmote().getEmote()) : new Emote(event.getEvent().getReactionEmote().getName());
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final GuildMessageReactionAddEvent e;

    public EventReactionAdd(
            final GuildMessageReactionAddEvent e
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

    public GuildMessageReactionAddEvent getEvent() {
        return e;
    }
}