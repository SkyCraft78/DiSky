package info.itsthesky.disky.skript.events.user;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.user.update.UserUpdateAvatarEvent;

public class UserAvatar extends DiSkyEvent<UserUpdateAvatarEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", UserAvatar.class, EvtUserAvatar.class,
                "[discord] user avatar (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");

        EventValues.registerEventValue(EvtUserAvatar.class, String.class, new Getter<String, EvtUserAvatar>() {
            @Override
            public String get(EvtUserAvatar event) {
                return event.getJDAEvent().getOldAvatarUrl();
            }
        }, -1);

        EventValues.registerEventValue(EvtUserAvatar.class, String.class, new Getter<String, EvtUserAvatar>() {
            @Override
            public String get(EvtUserAvatar event) {
                return event.getJDAEvent().getOldAvatarUrl();
            }
        }, 0);

       EventValues.registerEventValue(EvtUserAvatar.class, String.class, new Getter<String, EvtUserAvatar>() {
            @Override
            public String get(EvtUserAvatar event) {
                return event.getJDAEvent().getNewAvatarUrl();
            }
        }, 1);

       EventValues.registerEventValue(EvtUserAvatar.class, User.class, new Getter<User, EvtUserAvatar>() {
            @Override
            public User get(EvtUserAvatar event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

       EventValues.registerEventValue(EvtUserAvatar.class, JDA.class, new Getter<JDA, EvtUserAvatar>() {
            @Override
            public JDA get(EvtUserAvatar event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtUserAvatar extends SimpleDiSkyEvent<UserUpdateAvatarEvent> {
        public EvtUserAvatar(UserAvatar event) { }
    }

}