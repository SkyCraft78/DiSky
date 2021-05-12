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
import net.dv8tion.jda.api.events.role.update.RoleUpdateColorEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class EventRoleColorUpdate extends Event {

    private static final UpdatedValue<Color> updatedValue;

    static {
        Skript.registerEvent("Role Color Update", SimpleEvent.class, EventRoleColorUpdate.class, "[discord] [guild] role color (change|update)")
                .description("Run when someone change the color of a role.")
                .examples("on role color update:")
                .since("1.12");

        BotManager.customListener.add(new DiSkyEvent<>(RoleUpdateColorEvent.class, e -> Utils.sync(() -> DiSky.getPluginManager().callEvent(new EventRoleColorUpdate(e)))));
        updatedValue = new UpdatedValue<>(Color.class, EventRoleColorUpdate.class, "role color", true).register();

        EventValues.registerEventValue(EventRoleColorUpdate.class, Guild.class, new Getter<Guild, EventRoleColorUpdate>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventRoleColorUpdate event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventRoleColorUpdate.class, JDA.class, new Getter<JDA, EventRoleColorUpdate>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventRoleColorUpdate event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventRoleColorUpdate.class, Role.class, new Getter<Role, EventRoleColorUpdate>() {
            @Nullable
            @Override
            public Role get(final @NotNull EventRoleColorUpdate event) {
                return event.getEvent().getRole();
            }
        }, 0);

        EventValues.registerEventValue(EventRoleColorUpdate.class, User.class, new Getter<User, EventRoleColorUpdate>() {
            @Nullable
            @Override
            public User get(final @NotNull EventRoleColorUpdate event) {
                return event.author;
            }
        }, 0);

        EventValues.registerEventValue(EventRoleColorUpdate.class, Member.class, new Getter<Member, EventRoleColorUpdate>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventRoleColorUpdate event) {
                return event.authorM;
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final RoleUpdateColorEvent e;
    private final User author;
    private final Member authorM;

    public EventRoleColorUpdate(
            final RoleUpdateColorEvent e
            ) {
        super(Utils.areEventAsync());
        updatedValue.setNewObject(e.getNewColor());
        updatedValue.setOldObject(e.getOldColor());
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

    public RoleUpdateColorEvent getEvent() {
        return e;
    }
}