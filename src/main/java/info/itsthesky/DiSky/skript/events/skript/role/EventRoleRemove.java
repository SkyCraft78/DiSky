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
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Name("On Member Role Remove")
@Description("Run when any role is removed from a member.")
@Examples("on member role remove:")
@Since("1.9")
public class EventRoleRemove extends Event {

    static {
        Skript.registerEvent("Member Role Remove", SimpleEvent.class, EventRoleRemove.class, "[discord] [member] role remove");

        EventValues.registerEventValue(EventRoleRemove.class, User.class, new Getter<User, EventRoleRemove>() {
            @Nullable
            @Override
            public User get(final @NotNull EventRoleRemove event) {
                return event.getEvent().getUser();
            }
        }, 0);

        EventValues.registerEventValue(EventRoleRemove.class, Member.class, new Getter<Member, EventRoleRemove>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventRoleRemove event) {
                return event.getEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(EventRoleRemove.class, Guild.class, new Getter<Guild, EventRoleRemove>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventRoleRemove event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventRoleRemove.class, JDA.class, new Getter<JDA, EventRoleRemove>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventRoleRemove event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventRoleRemove.class, Role[].class, new Getter<Role[], EventRoleRemove>() {
            @Nullable
            @Override
            public Role[] get(final @NotNull EventRoleRemove event) {
                return event.getEvent().getRoles().toArray(new Role[0]);
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final GuildMemberRoleRemoveEvent e;

    public EventRoleRemove(
            final GuildMemberRoleRemoveEvent e
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

    public GuildMemberRoleRemoveEvent getEvent() {
        return e;
    }
}