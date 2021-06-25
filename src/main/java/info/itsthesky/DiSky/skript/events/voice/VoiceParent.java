package info.itsthesky.disky.skript.events.voice;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateParentEvent;

public class VoiceParent extends DiSkyEvent<VoiceChannelUpdateParentEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", VoiceParent.class, EvtVoiceParent.class,
                "[discord] voice [channel] parent (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtVoiceParent.class, Category.class, new Getter<Category, EvtVoiceParent>() {
            @Override
            public Category get(EvtVoiceParent event) {
                return event.getJDAEvent().getNewParent();
            }
        }, 1);

       EventValues.registerEventValue(EvtVoiceParent.class, Category.class, new Getter<Category, EvtVoiceParent>() {
            @Override
            public Category get(EvtVoiceParent event) {
                return event.getJDAEvent().getOldParent();
            }
        }, -1);

       EventValues.registerEventValue(EvtVoiceParent.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceParent>() {
            @Override
            public VoiceChannel get(EvtVoiceParent event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceParent.class, Guild.class, new Getter<Guild, EvtVoiceParent>() {
            @Override
            public Guild get(EvtVoiceParent event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceParent.class, JDA.class, new Getter<JDA, EvtVoiceParent>() {
            @Override
            public JDA get(EvtVoiceParent event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtVoiceParent extends SimpleDiSkyEvent<VoiceChannelUpdateParentEvent> {
        public EvtVoiceParent(VoiceParent event) { }
    }

}