package info.itsthesky.disky.skript.events.voice;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.LogEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateBitrateEvent;

public class BitrateEvent extends DiSkyEvent<VoiceChannelUpdateBitrateEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", BitrateEvent.class, EvtBitrateEvent.class,
                "[voice] bitrate (change|update)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");

        EventValues.registerEventValue(EvtBitrateEvent.class, Object.class, new Getter<Object, EvtBitrateEvent>() {
            @Override
            public Object get(EvtBitrateEvent event) {
                return event.getJDAEvent().getEntity();
            }
        }, 0);

        EventValues.registerEventValue(EvtBitrateEvent.class, Object.class, new Getter<Object, EvtBitrateEvent>() {
            @Override
            public Object get(EvtBitrateEvent event) {
                return event.getJDAEvent().getOldValue();
            }
        }, 0);

        EventValues.registerEventValue(EvtBitrateEvent.class, Object.class, new Getter<Object, EvtBitrateEvent>() {
            @Override
            public Object get(EvtBitrateEvent event) {
                return event.getJDAEvent().getNewValue();
            }
        }, 0);

        EventValues.registerEventValue(EvtBitrateEvent.class, Guild.class, new Getter<Guild, EvtBitrateEvent>() {
            @Override
            public Guild get(EvtBitrateEvent event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtBitrateEvent.class, VoiceChannel.class, new Getter<VoiceChannel, EvtBitrateEvent>() {
            @Override
            public VoiceChannel get(EvtBitrateEvent event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EvtBitrateEvent.class, JDA.class, new Getter<JDA, EvtBitrateEvent>() {
            @Override
            public JDA get(EvtBitrateEvent event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtBitrateEvent extends SimpleDiSkyEvent<VoiceChannelUpdateBitrateEvent> implements LogEvent {
        public EvtBitrateEvent(BitrateEvent event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}