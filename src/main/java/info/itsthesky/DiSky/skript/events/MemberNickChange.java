package info.itsthesky.disky.skript.events;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;

public class MemberNickChange extends DiSkyEvent<GuildMemberUpdateNicknameEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", MemberNickChange.class, EvtMemberNickChange.class,
                "member nick[name] (change|update)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtMemberNickChange.class, Member.class, new Getter<Member, EvtMemberNickChange>() {
            @Override
            public Member get(EvtMemberNickChange event) {
                return event.getJDAEvent().getEntity();
            }
        }, 0);

       EventValues.registerEventValue(EvtMemberNickChange.class, Member.class, new Getter<Member, EvtMemberNickChange>() {
            @Override
            public Member get(EvtMemberNickChange event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

       EventValues.registerEventValue(EvtMemberNickChange.class, User.class, new Getter<User, EvtMemberNickChange>() {
            @Override
            public User get(EvtMemberNickChange event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

       EventValues.registerEventValue(EvtMemberNickChange.class, Guild.class, new Getter<Guild, EvtMemberNickChange>() {
            @Override
            public Guild get(EvtMemberNickChange event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtMemberNickChange.class, JDA.class, new Getter<JDA, EvtMemberNickChange>() {
            @Override
            public JDA get(EvtMemberNickChange event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EvtMemberNickChange.class, String.class, new Getter<String, EvtMemberNickChange>() {
            @Override
            public String get(EvtMemberNickChange event) {
                return event.getJDAEvent().getNewNickname();
            }
        }, 1);

        EventValues.registerEventValue(EvtMemberNickChange.class, String.class, new Getter<String, EvtMemberNickChange>() {
            @Override
            public String get(EvtMemberNickChange event) {
                return event.getJDAEvent().getNewNickname();
            }
        }, 0);

        EventValues.registerEventValue(EvtMemberNickChange.class, String.class, new Getter<String, EvtMemberNickChange>() {
            @Override
            public String get(EvtMemberNickChange event) {
                return event.getJDAEvent().getOldNickname();
            }
        }, -1);

    }

    public static class EvtMemberNickChange extends SimpleDiSkyEvent<GuildMemberUpdateNicknameEvent> {
        public EvtMemberNickChange(MemberNickChange event) { }
    }

}