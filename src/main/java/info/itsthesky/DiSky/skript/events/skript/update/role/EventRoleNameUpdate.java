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
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.role.update.RoleUpdateNameEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventRoleNameUpdate extends Event {

    private static final UpdatedValue<String> updatedValue;

    static {
        Skript.registerEvent("Role Name Update", SimpleEvent.class, EventRoleNameUpdate.class, "[discord] [guild] role name (change|update)")
                .description("Run when someone change the name of a role.")
                .examples("on role name update:")
                .since("1.12");

        BotManager.customListener.add(new DiSkyEvent<>(RoleUpdateNameEvent.class, e -> Utils.sync(() -> DiSky.getPluginManager().callEvent(new EventRoleNameUpdate(e)))));
        updatedValue = new UpdatedValue<>(String.class, EventRoleNameUpdate.class, "role name", true).register();

        EventValues.registerEventValue(EventRoleNameUpdate.class, Guild.class, new Getter<Guild, EventRoleNameUpdate>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventRoleNameUpdate event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventRoleNameUpdate.class, JDA.class, new Getter<JDA, EventRoleNameUpdate>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventRoleNameUpdate event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventRoleNameUpdate.class, Role.class, new Getter<Role, EventRoleNameUpdate>() {
            @Nullable
            @Override
            public Role get(final @NotNull EventRoleNameUpdate event) {
                return event.getEvent().getRole();
            }
        }, 0);

        EventValues.registerEventValue(EventRoleNameUpdate.class, User.class, new Getter<User, EventRoleNameUpdate>() {
            @Nullable
            @Override
            public User get(final @NotNull EventRoleNameUpdate event) {
                return event.author;
            }
        }, 0);

        EventValues.registerEventValue(EventRoleNameUpdate.class, Member.class, new Getter<Member, EventRoleNameUpdate>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventRoleNameUpdate event) {
                return event.authorM;
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final RoleUpdateNameEvent e;
    private final User author;
    private final Member authorM;

    public EventRoleNameUpdate(
            final RoleUpdateNameEvent e
            ) {
        super(Utils.areEventAsync());
        updatedValue.setNewObject(e.getNewName());
        updatedValue.setOldObject(e.getOldName());
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

    public RoleUpdateNameEvent getEvent() {
        return e;
    }
}