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
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Member Leave Guild")
@Description("Fired when a user leave any guild where the bot is in.")
@Examples({"on member leave guild:",
            "\tsend message \"**We are sorry to let you go, `%name of event-user%` !**\" to channel with id \"750449611302371469\""})
@Since("1.0")
public class EventMemberLeave extends Event {

    static {
        // [seen by [bot] [(named|with name)]%string%]
        Skript.registerEvent("Member Leave", SimpleEvent.class, EventMemberLeave.class, "[discord] (user|member) leave (guild|server)")
        .description("Fired when a user leave any guild where the bot is in.")
        .examples("on member leave guild:",
                "\tsend message \"**We are sorry to let you go, `%name of event-user%` !**\" to channel with id \"750449611302371469\"")
        .since("1.0");

        EventValues.registerEventValue(EventMemberLeave.class, User.class, new Getter<User, EventMemberLeave>() {
            @Nullable
            @Override
            public User get(final @NotNull EventMemberLeave event) {
                return event.getUser();
            }
        }, 0);

        EventValues.registerEventValue(EventMemberLeave.class, Guild.class, new Getter<Guild, EventMemberLeave>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventMemberLeave event) {
                return event.getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventMemberLeave.class, Member.class, new Getter<Member, EventMemberLeave>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventMemberLeave event) {
                return event.getMember();
            }
        }, 0);

        EventValues.registerEventValue(EventMemberLeave.class, JDA.class, new Getter<JDA, EventMemberLeave>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventMemberLeave event) {
                return event.getJda();
            }
        }, 0);

    }

    public Guild getGuild() {
        return guild;
    }
    public User getUser() {
        return user;
    }
    public Member getMember() { return member; }

    private static final HandlerList HANDLERS = new HandlerList();

    public JDA getJda() {
        return jda;
    }

    private final Guild guild;
    private final User user;
    private final Member member;
    private final JDA jda;

    public EventMemberLeave(
            final Member member,
            final Guild guild,
            final JDA jda
            ) {
        super(Utils.areEventAsync());
        this.guild = guild;
        this.user = member.getUser();
        this.member = member;
        this.jda = jda;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}