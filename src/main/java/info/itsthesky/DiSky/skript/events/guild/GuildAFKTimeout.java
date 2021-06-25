package info.itsthesky.disky.skript.events.guild;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.LogEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateAfkTimeoutEvent;

public class GuildAFKTimeout extends DiSkyEvent<GuildUpdateAfkTimeoutEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", GuildAFKTimeout.class, EvtGuildAFKTimeout.class,
                "[guild] afk timeout (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtGuildAFKTimeout.class, Number.class, new Getter<Number, EvtGuildAFKTimeout>() {
            @Override
            public Number get(EvtGuildAFKTimeout event) {
                return event.getJDAEvent().getNewValue().getSeconds();
            }
        }, 1);

       EventValues.registerEventValue(EvtGuildAFKTimeout.class, Number.class, new Getter<Number, EvtGuildAFKTimeout>() {
            @Override
            public Number get(EvtGuildAFKTimeout event) {
                return event.getJDAEvent().getOldValue().getSeconds();
            }
        }, -1);

       EventValues.registerEventValue(EvtGuildAFKTimeout.class, Guild.class, new Getter<Guild, EvtGuildAFKTimeout>() {
            @Override
            public Guild get(EvtGuildAFKTimeout event) {
                return event.getJDAEvent().getEntity();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildAFKTimeout.class, Guild.class, new Getter<Guild, EvtGuildAFKTimeout>() {
            @Override
            public Guild get(EvtGuildAFKTimeout event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildAFKTimeout.class, JDA.class, new Getter<JDA, EvtGuildAFKTimeout>() {
            @Override
            public JDA get(EvtGuildAFKTimeout event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtGuildAFKTimeout extends SimpleDiSkyEvent<GuildUpdateAfkTimeoutEvent> implements LogEvent {
        public EvtGuildAFKTimeout(GuildAFKTimeout event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}