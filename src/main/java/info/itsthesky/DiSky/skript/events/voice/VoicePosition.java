package info.itsthesky.disky.skript.events.voice;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdatePositionEvent;

public class VoicePosition extends DiSkyEvent<VoiceChannelUpdatePositionEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", VoicePosition.class, EvtVoicePosition.class,
                "[discord] voice [channel] position (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtVoicePosition.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoicePosition>() {
            @Override
            public VoiceChannel get(EvtVoicePosition event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoicePosition.class, Guild.class, new Getter<Guild, EvtVoicePosition>() {
            @Override
            public Guild get(EvtVoicePosition event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoicePosition.class, JDA.class, new Getter<JDA, EvtVoicePosition>() {
            @Override
            public JDA get(EvtVoicePosition event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtVoicePosition extends SimpleDiSkyEvent<VoiceChannelUpdatePositionEvent> {
        public EvtVoicePosition(VoicePosition event) { }
    }

}