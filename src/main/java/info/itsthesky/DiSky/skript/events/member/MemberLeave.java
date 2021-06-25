package info.itsthesky.disky.skript.events.member;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;

public class MemberLeave extends DiSkyEvent<GuildMemberRemoveEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", MemberLeave.class, EvtMemberLeave.class,
                "member leave [guild]")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtMemberLeave.class, Member.class, new Getter<Member, EvtMemberLeave>() {
            @Override
            public Member get(EvtMemberLeave event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

       EventValues.registerEventValue(EvtMemberLeave.class, User.class, new Getter<User, EvtMemberLeave>() {
            @Override
            public User get(EvtMemberLeave event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

       EventValues.registerEventValue(EvtMemberLeave.class, Guild.class, new Getter<Guild, EvtMemberLeave>() {
            @Override
            public Guild get(EvtMemberLeave event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtMemberLeave.class, JDA.class, new Getter<JDA, EvtMemberLeave>() {
            @Override
            public JDA get(EvtMemberLeave event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtMemberLeave extends SimpleDiSkyEvent<GuildMemberRemoveEvent> {
        public EvtMemberLeave(MemberLeave event) { }
    }

}