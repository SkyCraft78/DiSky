package info.itsthesky.disky.skript.events.voice;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateBitrateEvent;

public class VoiceBitrate extends DiSkyEvent<VoiceChannelUpdateBitrateEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", VoiceBitrate.class, EvtVoiceBitrate.class,
                "[discord] voice [channel] bitrate (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtVoiceBitrate.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceBitrate>() {
            @Override
            public VoiceChannel get(EvtVoiceBitrate event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceBitrate.class, Guild.class, new Getter<Guild, EvtVoiceBitrate>() {
            @Override
            public Guild get(EvtVoiceBitrate event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceBitrate.class, JDA.class, new Getter<JDA, EvtVoiceBitrate>() {
            @Override
            public JDA get(EvtVoiceBitrate event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtVoiceBitrate extends SimpleDiSkyEvent<VoiceChannelUpdateBitrateEvent> {
        public EvtVoiceBitrate(VoiceBitrate event) { }
    }

}