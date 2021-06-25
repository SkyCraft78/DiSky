package info.itsthesky.disky.skript.events.user;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;

public class UserStatus extends DiSkyEvent<UserUpdateOnlineStatusEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", UserStatus.class, EvtUserStatus.class,
                "[discord] user [online] status (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtUserStatus.class, Guild.class, new Getter<Guild, EvtUserStatus>() {
            @Override
            public Guild get(EvtUserStatus event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtUserStatus.class, Member.class, new Getter<Member, EvtUserStatus>() {
            @Override
            public Member get(EvtUserStatus event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

       EventValues.registerEventValue(EvtUserStatus.class, OnlineStatus.class, new Getter<OnlineStatus, EvtUserStatus>() {
            @Override
            public OnlineStatus get(EvtUserStatus event) {
                return event.getJDAEvent().getOldOnlineStatus();
            }
        }, -1);

        EventValues.registerEventValue(EvtUserStatus.class, OnlineStatus.class, new Getter<OnlineStatus, EvtUserStatus>() {
            @Override
            public OnlineStatus get(EvtUserStatus event) {
                return event.getJDAEvent().getNewOnlineStatus();
            }
        }, 1);

        EventValues.registerEventValue(EvtUserStatus.class, OnlineStatus.class, new Getter<OnlineStatus, EvtUserStatus>() {
            @Override
            public OnlineStatus get(EvtUserStatus event) {
                return event.getJDAEvent().getNewOnlineStatus();
            }
        }, 0);

       EventValues.registerEventValue(EvtUserStatus.class, User.class, new Getter<User, EvtUserStatus>() {
            @Override
            public User get(EvtUserStatus event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

       EventValues.registerEventValue(EvtUserStatus.class, JDA.class, new Getter<JDA, EvtUserStatus>() {
            @Override
            public JDA get(EvtUserStatus event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtUserStatus extends SimpleDiSkyEvent<UserUpdateOnlineStatusEvent> {
        public EvtUserStatus(UserStatus event) { }
    }

}