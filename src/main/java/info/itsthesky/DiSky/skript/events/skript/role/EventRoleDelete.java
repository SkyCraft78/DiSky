package info.itsthesky.disky.skript.events.skript.role;

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
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Name("On Role Delete")
@Description("Run when any role is deleted in a guild")
@Examples("on role delete:")
@Since("1.9")
public class EventRoleDelete extends Event {

    static {
        Skript.registerEvent("Member Role Create", SimpleEvent.class, EventRoleDelete.class, "[discord] [guild] role delete")
        .description("Run when any role is deleted in a guild")
        .examples("on role delete:")
        .since("1.9");

        EventValues.registerEventValue(EventRoleDelete.class, Guild.class, new Getter<Guild, EventRoleDelete>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventRoleDelete event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventRoleDelete.class, JDA.class, new Getter<JDA, EventRoleDelete>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventRoleDelete event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventRoleDelete.class, Role.class, new Getter<Role, EventRoleDelete>() {
            @Nullable
            @Override
            public Role get(final @NotNull EventRoleDelete event) {
                return event.getEvent().getRole();
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final RoleDeleteEvent e;

    public EventRoleDelete(
            final RoleDeleteEvent e
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

    public RoleDeleteEvent getEvent() {
        return e;
    }
}