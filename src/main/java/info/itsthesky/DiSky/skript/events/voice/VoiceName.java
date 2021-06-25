package info.itsthesky.disky.skript.events.voice;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateNameEvent;

public class VoiceName extends DiSkyEvent<VoiceChannelUpdateNameEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", VoiceName.class, EvtVoiceName.class,
                "[discord] voice [channel] name (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtVoiceName.class, String.class, new Getter<String, EvtVoiceName>() {
            @Override
            public String get(EvtVoiceName event) {
                return event.getJDAEvent().getNewName();
            }
        }, 1);

       EventValues.registerEventValue(EvtVoiceName.class, String.class, new Getter<String, EvtVoiceName>() {
            @Override
            public String get(EvtVoiceName event) {
                return event.getJDAEvent().getOldName();
            }
        }, -1);

       EventValues.registerEventValue(EvtVoiceName.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceName>() {
            @Override
            public VoiceChannel get(EvtVoiceName event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceName.class, Guild.class, new Getter<Guild, EvtVoiceName>() {
            @Override
            public Guild get(EvtVoiceName event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceName.class, JDA.class, new Getter<JDA, EvtVoiceName>() {
            @Override
            public JDA get(EvtVoiceName event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtVoiceName extends SimpleDiSkyEvent<VoiceChannelUpdateNameEvent> {
        public EvtVoiceName(VoiceName event) { }
    }

}