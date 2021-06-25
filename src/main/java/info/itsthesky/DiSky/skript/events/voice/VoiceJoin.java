package info.itsthesky.disky.skript.events.voice;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;

public class VoiceJoin extends DiSkyEvent<GuildVoiceJoinEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", VoiceJoin.class, EvtVoiceJoin.class,
                "[(user|member)] voice join")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


        EventValues.registerEventValue(EvtVoiceJoin.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceJoin>() {
            @Override
            public VoiceChannel get(EvtVoiceJoin event) {
                return event.getJDAEvent().getNewValue();
            }
        }, 1);

        EventValues.registerEventValue(EvtVoiceJoin.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceJoin>() {
            @Override
            public VoiceChannel get(EvtVoiceJoin event) {
                return event.getJDAEvent().getChannelLeft();
            }
        }, -1);

        EventValues.registerEventValue(EvtVoiceJoin.class, VoiceChannel.class, new Getter<VoiceChannel, EvtVoiceJoin>() {
            @Override
            public VoiceChannel get(EvtVoiceJoin event) {
                return event.getJDAEvent().getChannelLeft();
            }
        }, 0);

        EventValues.registerEventValue(EvtVoiceJoin.class, Member.class, new Getter<Member, EvtVoiceJoin>() {
            @Override
            public Member get(EvtVoiceJoin event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(EvtVoiceJoin.class, Guild.class, new Getter<Guild, EvtVoiceJoin>() {
            @Override
            public Guild get(EvtVoiceJoin event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtVoiceJoin.class, JDA.class, new Getter<JDA, EvtVoiceJoin>() {
            @Override
            public JDA get(EvtVoiceJoin event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtVoiceJoin extends SimpleDiSkyEvent<GuildVoiceJoinEvent> {
        public EvtVoiceJoin(VoiceJoin event) { }
    }

}