package info.itsthesky.DiSky.skript.events.skript.guild;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Name("Guild Ban")
@Description("Run when a member is banned from a guild")
@Examples("on guild ban:")
@Since("1.4")
public class EventGuildBan extends Event {

    static {
        Skript.registerEvent("Guild Ban", SimpleEvent.class, EventGuildBan.class, "[discord] guild [member] ban");

        EventValues.registerEventValue(EventGuildBan.class, User.class, new Getter<User, EventGuildBan>() {
            @Nullable
            @Override
            public User get(final @NotNull EventGuildBan event) {
                return event.getEvent().getUser();
            }
        }, 0);

        EventValues.registerEventValue(EventGuildBan.class, Guild.class, new Getter<Guild, EventGuildBan>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventGuildBan event) {
                return event.getEvent().getGuild();
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final GuildBanEvent e;

    public EventGuildBan(
            final GuildBanEvent e
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

    public GuildBanEvent getEvent() {
        return e;
    }
}