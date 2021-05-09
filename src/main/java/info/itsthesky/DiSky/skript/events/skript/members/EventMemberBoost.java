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
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Name("Member boost change")
@Description("Run when a member boost or stop to boost a guild")
@Examples("on member boost:")
@Since("1.3")
public class EventMemberBoost extends Event {

    static {
        Skript.registerEvent("Member boost change", SimpleEvent.class, EventMemberBoost.class, "[discord] [member] boost [(change|update)]");

        EventValues.registerEventValue(EventMemberBoost.class, User.class, new Getter<User, EventMemberBoost>() {
            @Nullable
            @Override
            public User get(final @NotNull EventMemberBoost event) {
                return event.getEvent().getUser();
            }
        }, 0);

        EventValues.registerEventValue(EventMemberBoost.class, Member.class, new Getter<Member, EventMemberBoost>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventMemberBoost event) {
                return event.getEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(EventMemberBoost.class, Guild.class, new Getter<Guild, EventMemberBoost>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventMemberBoost event) {
                return event.getEvent().getGuild();
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final GuildMemberUpdateBoostTimeEvent e;

    public EventMemberBoost(
            final GuildMemberUpdateBoostTimeEvent e
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

    public GuildMemberUpdateBoostTimeEvent getEvent() {
        return e;
    }
}