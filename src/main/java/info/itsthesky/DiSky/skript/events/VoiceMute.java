package info.itsthesky.disky.skript.events;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMuteEvent;

public class VoiceMute extends DiSkyEvent<GuildVoiceMuteEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", VoiceMute.class, EvtVoiceMute.class,
                "[voice] member mute")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtVoiceMute.class, Member.class, new Getter<Member, EvtVoiceMute>() {
            @Override
            public Member get(EvtVoiceMute event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceMute.class, Guild.class, new Getter<Guild, EvtVoiceMute>() {
            @Override
            public Guild get(EvtVoiceMute event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtVoiceMute.class, JDA.class, new Getter<JDA, EvtVoiceMute>() {
            @Override
            public JDA get(EvtVoiceMute event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtVoiceMute extends SimpleDiSkyEvent<GuildVoiceMuteEvent> {
        public EvtVoiceMute(VoiceMute event) { }
    }

}