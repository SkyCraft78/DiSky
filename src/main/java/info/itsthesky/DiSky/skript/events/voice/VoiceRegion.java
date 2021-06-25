package info.itsthesky.disky.skript.events.voice;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateRegionEvent;

public class VoiceRegion extends DiSkyEvent<VoiceChannelUpdateRegionEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", VoiceRegion.class, EvtVoiceRegion.class,
                "[discord] voice [channel] region (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtVoiceRegion.class, String.class, new Getter<String, EvtVoiceRegion>() {
            @Override
            public String get(EvtVoiceRegion event) {
                return event.getJDAEvent().getOldRegion().getName();
            }
        }, -1);

       EventValues.registerEventValue(EvtVoiceRegion.class, String.class, new Getter<String, EvtVoiceRegion>() {
            @Override
            public String get(EvtVoiceRegion event) {
                return event.getJDAEvent().getNewRegion().getName();
            }
        }, 1);

       EventValues.registerEventValue(EvtVoiceRegion.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceRegion>() {
            @Override
            public VoiceChannel get(EvtVoiceRegion event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceRegion.class, Guild.class, new Getter<Guild, EvtVoiceRegion>() {
            @Override
            public Guild get(EvtVoiceRegion event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceRegion.class, JDA.class, new Getter<JDA, EvtVoiceRegion>() {
            @Override
            public JDA get(EvtVoiceRegion event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtVoiceRegion extends SimpleDiSkyEvent<VoiceChannelUpdateRegionEvent> {
        public EvtVoiceRegion(VoiceRegion event) { }
    }

}