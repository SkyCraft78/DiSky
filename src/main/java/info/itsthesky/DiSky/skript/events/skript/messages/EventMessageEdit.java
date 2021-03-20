package info.itsthesky.DiSky.skript.events.skript.messages;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.DiSky.tools.object.messages.Channel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Name("Message Edit")
@Description("Run when any message is edited. Cause of discord limitation, you can't get the old content.")
@Examples("on message edit:")
@Since("1.4")
public class EventMessageEdit extends Event {

    static {
        Skript.registerEvent("Message Edit", SimpleEvent.class, EventMessageEdit.class, "[discord] message edit");

        EventValues.registerEventValue(EventMessageEdit.class, User.class, new Getter<User, EventMessageEdit>() {
            @Nullable
            @Override
            public User get(final @NotNull EventMessageEdit event) {
                return event.getEvent().getMessage().getAuthor();
            }
        }, 0);

        EventValues.registerEventValue(EventMessageEdit.class, Guild.class, new Getter<Guild, EventMessageEdit>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventMessageEdit event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventMessageEdit.class, Channel.class, new Getter<Channel, EventMessageEdit>() {
            @Nullable
            @Override
            public Channel get(final @NotNull EventMessageEdit event) {
                return new Channel(event.getEvent().getChannel());
            }
        }, 0);

        EventValues.registerEventValue(EventMessageEdit.class, TextChannel.class, new Getter<TextChannel, EventMessageEdit>() {
            @Nullable
            @Override
            public TextChannel get(final @NotNull EventMessageEdit event) {
                return event.getEvent().getChannel();
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final GuildMessageUpdateEvent e;

    public EventMessageEdit(
            final GuildMessageUpdateEvent e
            ) {
        super(true);
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

    public GuildMessageUpdateEvent getEvent() {
        return e;
    }
}