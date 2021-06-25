package info.itsthesky.disky.skript.events.voice;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;

public class VoiceLeave extends DiSkyEvent<GuildVoiceLeaveEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", VoiceLeave.class, EvtVoiceLeave.class,
                "[(user|member)] voice leave")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


        EventValues.registerEventValue(EvtVoiceLeave.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceLeave>() {
            @Override
            public VoiceChannel get(EvtVoiceLeave event) {
                return event.getJDAEvent().getOldValue();
            }
        }, -1);

        EventValues.registerEventValue(EvtVoiceLeave.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceLeave>() {
            @Override
            public VoiceChannel get(EvtVoiceLeave event) {
                return event.getJDAEvent().getNewValue();
            }
        }, 1);

        EventValues.registerEventValue(EvtVoiceLeave.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceLeave>() {
            @Override
            public VoiceChannel get(EvtVoiceLeave event) {
                return event.getJDAEvent().getNewValue();
            }
        }, 0);

        EventValues.registerEventValue(EvtVoiceLeave.class, Member.class, new Getter<Member, EvtVoiceLeave>() {
            @Override
            public Member get(EvtVoiceLeave event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(EvtVoiceLeave.class, Guild.class, new Getter<Guild, EvtVoiceLeave>() {
            @Override
            public Guild get(EvtVoiceLeave event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtVoiceLeave.class, JDA.class, new Getter<JDA, EvtVoiceLeave>() {
            @Override
            public JDA get(EvtVoiceLeave event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtVoiceLeave extends SimpleDiSkyEvent<GuildVoiceLeaveEvent> {
        public EvtVoiceLeave(VoiceLeave event) { }
    }

}