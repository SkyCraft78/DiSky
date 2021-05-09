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
        Skript.registerEvent("Member Leave", SimpleEvent.class, EventMemberLeave.class, "[discord] (user|member) leave (guild|server)");

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

        EventValues.registerEventValue(EventMemberJoin.class, Member.class, new Getter<Member, EventMemberJoin>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventMemberJoin event) {
                return event.getEvent().getMember();
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

    private final Guild guild;
    private final User user;
    private final Member member;

    public EventMemberLeave(
            final Member member,
            final Guild guild
            ) {
        super(Utils.areEventAsync());
        this.guild = guild;
        this.user = member.getUser();
        this.member = member;
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