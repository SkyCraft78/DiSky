package info.itsthesky.disky.skript.events;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceDeafenEvent;

public class VoiceDeafen extends DiSkyEvent<GuildVoiceDeafenEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", VoiceDeafen.class, EvtVoiceDeafen.class,
                "[voice] member deafen")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtVoiceDeafen.class, Member.class, new Getter<Member, EvtVoiceDeafen>() {
            @Override
            public Member get(EvtVoiceDeafen event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceDeafen.class, Guild.class, new Getter<Guild, EvtVoiceDeafen>() {
            @Override
            public Guild get(EvtVoiceDeafen event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceDeafen.class, JDA.class, new Getter<JDA, EvtVoiceDeafen>() {
            @Override
            public JDA get(EvtVoiceDeafen event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtVoiceDeafen extends SimpleDiSkyEvent<GuildVoiceDeafenEvent> {
        public EvtVoiceDeafen(VoiceDeafen event) { }
    }

}