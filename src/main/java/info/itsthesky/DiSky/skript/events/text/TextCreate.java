package info.itsthesky.disky.skript.events.text;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.LogEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;

public class TextCreate extends DiSkyEvent<TextChannelCreateEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", TextCreate.class, EvtTextCreate.class,
                "text [channel] create")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtTextCreate.class, Guild.class, new Getter<Guild, EvtTextCreate>() {
            @Override
            public Guild get(EvtTextCreate event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextCreate.class, TextChannel.class, new Getter<TextChannel, EvtTextCreate>() {
            @Override
            public TextChannel get(EvtTextCreate event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextCreate.class, JDA.class, new Getter<JDA, EvtTextCreate>() {
            @Override
            public JDA get(EvtTextCreate event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtTextCreate extends SimpleDiSkyEvent<TextChannelCreateEvent> implements LogEvent {
        public EvtTextCreate(TextCreate event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}