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
import info.itsthesky.DiSky.tools.object.Emote;
import info.itsthesky.DiSky.tools.object.messages.Channel;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Name("On Member Role Add")
@Description("Run when any role is added to a member.")
@Examples("on member role add:")
@Since("1.9")
public class EventRoleAdd extends Event {

    static {
        Skript.registerEvent("Member Role Add", SimpleEvent.class, EventRoleAdd.class, "[discord] [member] role add");

        EventValues.registerEventValue(EventRoleAdd.class, User.class, new Getter<User, EventRoleAdd>() {
            @Nullable
            @Override
            public User get(final @NotNull EventRoleAdd event) {
                return event.getEvent().getUser();
            }
        }, 0);

        EventValues.registerEventValue(EventRoleAdd.class, Member.class, new Getter<Member, EventRoleAdd>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventRoleAdd event) {
                return event.getEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(EventRoleAdd.class, Guild.class, new Getter<Guild, EventRoleAdd>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventRoleAdd event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventRoleAdd.class, JDA.class, new Getter<JDA, EventRoleAdd>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventRoleAdd event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventRoleAdd.class, Role[].class, new Getter<Role[], EventRoleAdd>() {
            @Nullable
            @Override
            public Role[] get(final @NotNull EventRoleAdd event) {
                return event.getEvent().getRoles().toArray(new Role[0]);
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final GuildMemberRoleAddEvent e;

    public EventRoleAdd(
            final GuildMemberRoleAddEvent e
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

    public GuildMemberRoleAddEvent getEvent() {
        return e;
    }
}