package info.itsthesky.disky.skript.events.skript.members;

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
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Member Join Guild")
@Description("Fired when a user join any guild where the bot is in.")
@Examples({"on member join guild:",
            "\tsend message \"**Welcome to the server, `%name of event-user%`!**\" to channel with id \"750449611302371469\""})
@Since("1.0, 1.7 (event-invite)")
public class EventMemberJoin extends Event {

    static {
        // [seen by [bot] [(named|with name)]%string%]
        Skript.registerEvent("Member Join", SimpleEvent.class, EventMemberJoin.class, "[discord] (user|member) join (guild|server)")
        .description("Fired when a user join any guild where the bot is in.")
        .examples("on member join guild:",
                "\tsend message \"**Welcome to the server, `%name of event-user%`!**\" to channel with id \"750449611302371469\"")
        .since("1.0, 1.7 (event-invite");

        EventValues.registerEventValue(EventMemberJoin.class, User.class, new Getter<User, EventMemberJoin>() {
            @Nullable
            @Override
            public User get(final @NotNull EventMemberJoin event) {
                return event.getEvent().getUser();
            }
        }, 0);

        EventValues.registerEventValue(EventMemberJoin.class, Member.class, new Getter<Member, EventMemberJoin>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventMemberJoin event) {
                return event.getEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(EventMemberJoin.class, Guild.class, new Getter<Guild, EventMemberJoin>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventMemberJoin event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventMemberJoin.class, Invite.class, new Getter<Invite, EventMemberJoin>() {
            @Nullable
            @Override
            public Invite get(final @NotNull EventMemberJoin event) {
                return event.getInvite();
            }
        }, 0);

        EventValues.registerEventValue(EventMemberJoin.class, JDA.class, new Getter<JDA, EventMemberJoin>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventMemberJoin event) {
                return event.getEvent().getJDA();
            }
        }, 0);

    }

    private final GuildMemberJoinEvent event;
    private final Invite invite;

    public EventMemberJoin(
            final GuildMemberJoinEvent event,
            final Invite invite
    ) {
        super(Utils.areEventAsync());
        this.event = event;
        this.invite = invite;
    }

    public GuildMemberJoinEvent getEvent() {
        return event;
    }

    public Invite getInvite() {
        return invite;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    private static final HandlerList HANDLERS = new HandlerList();
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}