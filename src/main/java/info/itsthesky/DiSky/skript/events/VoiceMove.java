package info.itsthesky.disky.skript.events;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;

public class VoiceMove extends DiSkyEvent<GuildVoiceMoveEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", VoiceMove.class, EvtVoiceMove.class,
                "[voice] member move")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtVoiceMove.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceMove>() {
            @Override
            public VoiceChannel get(EvtVoiceMove event) {
                return event.getJDAEvent().getNewValue();
            }
        }, 1);

       EventValues.registerEventValue(EvtVoiceMove.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceMove>() {
            @Override
            public VoiceChannel get(EvtVoiceMove event) {
                return event.getJDAEvent().getChannelLeft();
            }
        }, -1);

       EventValues.registerEventValue(EvtVoiceMove.class, Member.class, new Getter<Member, EvtVoiceMove>() {
            @Override
            public Member get(EvtVoiceMove event) {
                return event.getJDAEvent().getEntity();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceMove.class, Guild.class, new Getter<Guild, EvtVoiceMove>() {
            @Override
            public Guild get(EvtVoiceMove event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceMove.class, JDA.class, new Getter<JDA, EvtVoiceMove>() {
            @Override
            public JDA get(EvtVoiceMove event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtVoiceMove extends SimpleDiSkyEvent<GuildVoiceMoveEvent> {
        public EvtVoiceMove(VoiceMove event) { }
    }

}