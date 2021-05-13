package info.itsthesky.disky.skript.events.skript.guild;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Name("Guild Unban")
@Description("Run when a member is unbanned from a guild")
@Examples("on guild unban:")
@Since("1.4")
public class EventGuildUnban extends Event {

    static {
        Skript.registerEvent("Guild Unban", SimpleEvent.class, EventGuildUnban.class, "[discord] guild [member] unban")
        .description("Run when a member is unbanned from a guild")
        .examples("on guild unban:")
        .since("1.4");

        EventValues.registerEventValue(EventGuildUnban.class, User.class, new Getter<User, EventGuildUnban>() {
            @Nullable
            @Override
            public User get(final @NotNull EventGuildUnban event) {
                return event.getEvent().getUser();
            }
        }, 0);

        EventValues.registerEventValue(EventGuildUnban.class, Guild.class, new Getter<Guild, EventGuildUnban>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventGuildUnban event) {
                return event.getEvent().getGuild();
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final GuildUnbanEvent e;

    public EventGuildUnban(
            final GuildUnbanEvent e
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

    public GuildUnbanEvent getEvent() {
        return e;
    }
}