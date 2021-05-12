package info.itsthesky.disky.skript.events.skript.role;

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
import net.dv8tion.jda.api.events.role.update.RoleUpdateMentionableEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateNameEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventRoleMentionUpdate extends Event {

    private static final UpdatedValue<Boolean> updatedValue;

    static {
        Skript.registerEvent("Role Mentionable Update", SimpleEvent.class, EventRoleMentionUpdate.class, "[discord] [guild] role mentionable (change|update)")
                .description("Run when someone change the mention state of a role.", "Updated value possible:", "new [discord] role mention state", "old [discord] role mention state")
                .examples("on role mentionable update:")
                .since("1.12");

        BotManager.customListener.add(new DiSkyEvent<>(RoleUpdateMentionableEvent.class, e -> Utils.sync(() -> DiSky.getPluginManager().callEvent(new EventRoleMentionUpdate(e)))));
        updatedValue = new UpdatedValue<>(Boolean.class, EventRoleMentionUpdate.class, "[discord] role mention state", true).register();

        EventValues.registerEventValue(EventRoleMentionUpdate.class, Guild.class, new Getter<Guild, EventRoleMentionUpdate>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventRoleMentionUpdate event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventRoleMentionUpdate.class, JDA.class, new Getter<JDA, EventRoleMentionUpdate>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventRoleMentionUpdate event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventRoleMentionUpdate.class, Role.class, new Getter<Role, EventRoleMentionUpdate>() {
            @Nullable
            @Override
            public Role get(final @NotNull EventRoleMentionUpdate event) {
                return event.getEvent().getRole();
            }
        }, 0);

        EventValues.registerEventValue(EventRoleMentionUpdate.class, User.class, new Getter<User, EventRoleMentionUpdate>() {
            @Nullable
            @Override
            public User get(final @NotNull EventRoleMentionUpdate event) {
                return event.author;
            }
        }, 0);

        EventValues.registerEventValue(EventRoleMentionUpdate.class, Member.class, new Getter<Member, EventRoleMentionUpdate>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventRoleMentionUpdate event) {
                return event.authorM;
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final RoleUpdateMentionableEvent e;
    private final User author;
    private final Member authorM;

    public EventRoleMentionUpdate(
            final RoleUpdateMentionableEvent e
            ) {
        super(Utils.areEventAsync());
        updatedValue.setNewObject(e.getNewValue());
        updatedValue.setOldObject(e.getOldValue());
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

    public RoleUpdateMentionableEvent getEvent() {
        return e;
    }
}