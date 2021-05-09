package info.itsthesky.disky.skript.events.skript.nickname;

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
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Name("Nickname Change")
@Description("Run when any member change his nickname")
@Examples("on member nickname change:")
@Since("1.3")
public class EventNickChange extends Event {

    static {
        Skript.registerEvent("Nickname Change", SimpleEvent.class, EventNickChange.class, "[discord] [member] nick[name] change");

        EventValues.registerEventValue(EventNickChange.class, Member.class, new Getter<Member, EventNickChange>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventNickChange event) {
                return event.getEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(EventNickChange.class, User.class, new Getter<User, EventNickChange>() {
            @Nullable
            @Override
            public User get(final @NotNull EventNickChange event) {
                return event.getEvent().getUser();
            }
        }, 0);

        EventValues.registerEventValue(EventNickChange.class, Guild.class, new Getter<Guild, EventNickChange>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventNickChange event) {
                return event.getEvent().getGuild();
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();
    private final GuildMemberUpdateNicknameEvent e;

    public EventNickChange(
            final GuildMemberUpdateNicknameEvent e
            ) {
        super(Utils.areEventAsync());
        this.e = e;

        String oldNick = e.getOldNickname();
        if (oldNick == null) oldNick = e.getMember().getUser().getName();
        String newNick = e.getNewNickname();
        if (newNick == null) newNick = e.getMember().getEffectiveName();
        ExprOldNickname.oldNickname = oldNick;
        ExprNewNickname.newNickname = newNick;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    public GuildMemberUpdateNicknameEvent getEvent() {
        return e;
    }
}