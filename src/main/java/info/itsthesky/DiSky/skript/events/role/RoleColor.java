package info.itsthesky.disky.skript.events.role;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.LogEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.role.update.RoleUpdateColorEvent;

import java.awt.*;

public class RoleColor extends DiSkyEvent<RoleUpdateColorEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", RoleColor.class, EvtRoleColor.class,
                "[discord] role (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtRoleColor.class, Color.class, new Getter<Color, EvtRoleColor>() {
            @Override
            public Color get(EvtRoleColor event) {
                return event.getJDAEvent().getNewColor();
            }
        }, 1);

        EventValues.registerEventValue(EvtRoleColor.class, Color.class, new Getter<Color, EvtRoleColor>() {
            @Override
            public Color get(EvtRoleColor event) {
                return event.getJDAEvent().getOldColor();
            }
        }, -1);

        EventValues.registerEventValue(EvtRoleColor.class, Color.class, new Getter<Color, EvtRoleColor>() {
            @Override
            public Color get(EvtRoleColor event) {
                return event.getJDAEvent().getOldColor();
            }
        }, 0);

       EventValues.registerEventValue(EvtRoleColor.class, Guild.class, new Getter<Guild, EvtRoleColor>() {
            @Override
            public Guild get(EvtRoleColor event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtRoleColor.class, Role.class, new Getter<Role, EvtRoleColor>() {
            @Override
            public Role get(EvtRoleColor event) {
                return event.getJDAEvent().getRole();
            }
        }, 0);

       EventValues.registerEventValue(EvtRoleColor.class, JDA.class, new Getter<JDA, EvtRoleColor>() {
            @Override
            public JDA get(EvtRoleColor event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtRoleColor extends SimpleDiSkyEvent<RoleUpdateColorEvent> implements LogEvent {
        public EvtRoleColor(RoleColor event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}