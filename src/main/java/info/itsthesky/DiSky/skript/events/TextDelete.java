package info.itsthesky.disky.skript.events;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.LogEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;

public class TextDelete extends DiSkyEvent<TextChannelDeleteEvent> {

    static {
        DiSkyEvent.register("Text Channel Delete", TextDelete.class, EvtTextDelete.class,
                "text [channel] delete")
                .setName("Text Channel Delete")
                .setDesc("Fired when a text channel is deleted")
                .setExample("on text channel delete:");

        EventValues.registerEventValue(EvtTextDelete.class, TextChannel.class, new Getter<TextChannel, EvtTextDelete>() {
            @Override
            public TextChannel get(EvtTextDelete event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EvtTextDelete.class, Guild.class, new Getter<Guild, EvtTextDelete>() {
            @Override
            public Guild get(EvtTextDelete event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtTextDelete.class, JDA.class, new Getter<JDA, EvtTextDelete>() {
            @Override
            public JDA get(EvtTextDelete event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);
    }

    public static class EvtTextDelete extends SimpleDiSkyEvent<TextChannelDeleteEvent> implements LogEvent {
        public EvtTextDelete(TextDelete event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}