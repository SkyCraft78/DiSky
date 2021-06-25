package info.itsthesky.disky.skript.events.user;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.user.update.UserUpdateDiscriminatorEvent;

public class UserTag extends DiSkyEvent<UserUpdateDiscriminatorEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", UserTag.class, EvtUserTag.class,
                "[discord] user (tag|discriminator) (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


        EventValues.registerEventValue(EvtUserTag.class, String.class, new Getter<String, EvtUserTag>() {
            @Override
            public String get(EvtUserTag event) {
                return event.getJDAEvent().getOldDiscriminator();
            }
        }, -1);

        EventValues.registerEventValue(EvtUserTag.class, String.class, new Getter<String, EvtUserTag>() {
            @Override
            public String get(EvtUserTag event) {
                return event.getJDAEvent().getOldDiscriminator();
            }
        }, 0);

       EventValues.registerEventValue(EvtUserTag.class, String.class, new Getter<String, EvtUserTag>() {
            @Override
            public String get(EvtUserTag event) {
                return event.getJDAEvent().getNewDiscriminator();
            }
        }, 1);

       EventValues.registerEventValue(EvtUserTag.class, User.class, new Getter<User, EvtUserTag>() {
            @Override
            public User get(EvtUserTag event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

       EventValues.registerEventValue(EvtUserTag.class, JDA.class, new Getter<JDA, EvtUserTag>() {
            @Override
            public JDA get(EvtUserTag event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtUserTag extends SimpleDiSkyEvent<UserUpdateDiscriminatorEvent> {
        public EvtUserTag(UserTag event) { }
    }

}