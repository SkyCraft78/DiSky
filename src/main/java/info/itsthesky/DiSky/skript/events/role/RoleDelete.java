package info.itsthesky.disky.skript.events.role;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.LogEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;

public class RoleDelete extends DiSkyEvent<RoleDeleteEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", RoleDelete.class, EvtRoleDelete.class,
                "[guild] role delete]")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtRoleDelete.class, Role.class, new Getter<Role, EvtRoleDelete>() {
            @Override
            public Role get(EvtRoleDelete event) {
                return event.getJDAEvent().getRole();
            }
        }, 0);

       EventValues.registerEventValue(EvtRoleDelete.class, Guild.class, new Getter<Guild, EvtRoleDelete>() {
            @Override
            public Guild get(EvtRoleDelete event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtRoleDelete.class, JDA.class, new Getter<JDA, EvtRoleDelete>() {
            @Override
            public JDA get(EvtRoleDelete event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtRoleDelete extends SimpleDiSkyEvent<RoleDeleteEvent> implements LogEvent {
        public EvtRoleDelete(RoleDelete event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}