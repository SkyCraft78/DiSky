package info.itsthesky.disky.skript.events.role;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.LogEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.role.update.RoleUpdateNameEvent;

public class RoleName extends DiSkyEvent<RoleUpdateNameEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", RoleName.class, EvtRoleName.class,
                "[discord] role name (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");

       EventValues.registerEventValue(EvtRoleName.class, String.class, new Getter<String, EvtRoleName>() {
            @Override
            public String get(EvtRoleName event) {
                return event.getJDAEvent().getNewValue();
            }
        }, 1);

       EventValues.registerEventValue(EvtRoleName.class, String.class, new Getter<String, EvtRoleName>() {
            @Override
            public String get(EvtRoleName event) {
                return event.getJDAEvent().getOldName();
            }
        }, -1);

       EventValues.registerEventValue(EvtRoleName.class, String.class, new Getter<String, EvtRoleName>() {
            @Override
            public String get(EvtRoleName event) {
                return event.getJDAEvent().getOldValue();
            }
        }, 0);

       EventValues.registerEventValue(EvtRoleName.class, Guild.class, new Getter<Guild, EvtRoleName>() {
            @Override
            public Guild get(EvtRoleName event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtRoleName.class, Role.class, new Getter<Role, EvtRoleName>() {
            @Override
            public Role get(EvtRoleName event) {
                return event.getJDAEvent().getRole();
            }
        }, 0);

       EventValues.registerEventValue(EvtRoleName.class, JDA.class, new Getter<JDA, EvtRoleName>() {
            @Override
            public JDA get(EvtRoleName event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtRoleName extends SimpleDiSkyEvent<RoleUpdateNameEvent> implements LogEvent {
        public EvtRoleName(RoleName event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}