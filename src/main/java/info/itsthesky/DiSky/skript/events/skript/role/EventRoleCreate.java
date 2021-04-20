package info.itsthesky.DiSky.skript.events.skript.role;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.DiSky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Name("On Role Create")
@Description("Run when any role is created in a guild")
@Examples("on role create:")
@Since("1.9")
public class EventRoleCreate extends Event {

    static {
        Skript.registerEvent("Member Role Create", SimpleEvent.class, EventRoleCreate.class, "[discord] [guild] role create");

        EventValues.registerEventValue(EventRoleCreate.class, Guild.class, new Getter<Guild, EventRoleCreate>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventRoleCreate event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventRoleCreate.class, JDA.class, new Getter<JDA, EventRoleCreate>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventRoleCreate event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventRoleCreate.class, Role.class, new Getter<Role, EventRoleCreate>() {
            @Nullable
            @Override
            public Role get(final @NotNull EventRoleCreate event) {
                return event.getEvent().getRole();
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final RoleCreateEvent e;

    public EventRoleCreate(
            final RoleCreateEvent e
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

    public RoleCreateEvent getEvent() {
        return e;
    }
}