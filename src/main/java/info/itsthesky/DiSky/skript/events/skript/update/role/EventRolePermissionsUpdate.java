package info.itsthesky.disky.skript.events.skript.update.role;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.managers.BotManager;
import info.itsthesky.disky.skript.events.util.DiSkyEvent;
import info.itsthesky.disky.tools.UpdatedValue;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.role.update.RoleUpdatePermissionsEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventRolePermissionsUpdate extends Event {

    private static final UpdatedValue<Permission> updatedValue;

    static {
        Skript.registerEvent("Role Permissions Update", SimpleEvent.class, EventRolePermissionsUpdate.class, "[discord] [guild] role perm(s|issions) (change|update)")
                .description("Run when someone change the permissions of a role.", "Possible updated values:", "new role perms", "old role perms")
                .examples("on role perms update:")
                .since("1.12");

        BotManager.customListener.add(new DiSkyEvent<>(RoleUpdatePermissionsEvent.class, e -> Utils.sync(() -> DiSky.getPluginManager().callEvent(new EventRolePermissionsUpdate(e)))));
        updatedValue = new UpdatedValue<>(Permission.class, EventRolePermissionsUpdate.class, "role perms", false).register();

        EventValues.registerEventValue(EventRolePermissionsUpdate.class, Guild.class, new Getter<Guild, EventRolePermissionsUpdate>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventRolePermissionsUpdate event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventRolePermissionsUpdate.class, JDA.class, new Getter<JDA, EventRolePermissionsUpdate>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventRolePermissionsUpdate event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventRolePermissionsUpdate.class, Role.class, new Getter<Role, EventRolePermissionsUpdate>() {
            @Nullable
            @Override
            public Role get(final @NotNull EventRolePermissionsUpdate event) {
                return event.getEvent().getRole();
            }
        }, 0);

        EventValues.registerEventValue(EventRolePermissionsUpdate.class, User.class, new Getter<User, EventRolePermissionsUpdate>() {
            @Nullable
            @Override
            public User get(final @NotNull EventRolePermissionsUpdate event) {
                return event.author;
            }
        }, 0);

        EventValues.registerEventValue(EventRolePermissionsUpdate.class, Member.class, new Getter<Member, EventRolePermissionsUpdate>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventRolePermissionsUpdate event) {
                return event.authorM;
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final RoleUpdatePermissionsEvent e;
    private final User author;
    private final Member authorM;

    public EventRolePermissionsUpdate(
            final RoleUpdatePermissionsEvent e
            ) {
        super(Utils.areEventAsync());
        updatedValue.setNewObjectList(e.getNewValue().toArray(new Permission[0]));
        updatedValue.setOldObjectList(e.getOldValue().toArray(new Permission[0]));
        author = e.getGuild().retrieveAuditLogs().complete().get(0).getUser();
        authorM = e.getGuild().getMember(author);
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

    public RoleUpdatePermissionsEvent getEvent() {
        return e;
    }
}