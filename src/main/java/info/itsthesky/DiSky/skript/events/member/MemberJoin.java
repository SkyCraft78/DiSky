package info.itsthesky.disky.skript.events.member;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

public class MemberJoin extends DiSkyEvent<GuildMemberJoinEvent> {

    public static Invite usedInvite;

    static {
        DiSkyEvent.register("Inner Event Name", MemberJoin.class, EvtMemberJoin.class,
                "member join [guild]")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtMemberJoin.class, Member.class, new Getter<Member, EvtMemberJoin>() {
            @Override
            public Member get(EvtMemberJoin event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(EvtMemberJoin.class, Invite.class, new Getter<Invite, EvtMemberJoin>() {
            @Override
            public Invite get(EvtMemberJoin event) {
                return usedInvite;
            }
        }, 0);

        EventValues.registerEventValue(EvtMemberJoin.class, User.class, new Getter<User, EvtMemberJoin>() {
            @Override
            public User get(EvtMemberJoin event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

       EventValues.registerEventValue(EvtMemberJoin.class, Guild.class, new Getter<Guild, EvtMemberJoin>() {
            @Override
            public Guild get(EvtMemberJoin event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtMemberJoin.class, JDA.class, new Getter<JDA, EvtMemberJoin>() {
            @Override
            public JDA get(EvtMemberJoin event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtMemberJoin extends SimpleDiSkyEvent<GuildMemberJoinEvent> {
        public EvtMemberJoin(MemberJoin event) { }
    }

}