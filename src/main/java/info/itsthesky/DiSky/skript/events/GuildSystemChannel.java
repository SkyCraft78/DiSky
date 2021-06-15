package info.itsthesky.disky.skript.events;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateSystemChannelEvent;

public class GuildSystemChannel extends DiSkyEvent<GuildUpdateSystemChannelEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", GuildSystemChannel.class, EvtGuildSystemChannel.class,
                "[guild] system channel (change|update)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtGuildSystemChannel.class, TextChannel.class, new Getter<TextChannel, EvtGuildSystemChannel>() {
            @Override
            public TextChannel get(EvtGuildSystemChannel event) {
                return event.getJDAEvent().getNewSystemChannel();
            }
        }, 1);

       EventValues.registerEventValue(EvtGuildSystemChannel.class, TextChannel.class, new Getter<TextChannel, EvtGuildSystemChannel>() {
            @Override
            public TextChannel get(EvtGuildSystemChannel event) {
                return event.getJDAEvent().getOldSystemChannel();
            }
        }, -1);

       EventValues.registerEventValue(EvtGuildSystemChannel.class, Guild.class, new Getter<Guild, EvtGuildSystemChannel>() {
            @Override
            public Guild get(EvtGuildSystemChannel event) {
                return event.getJDAEvent().getEntity();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildSystemChannel.class, Guild.class, new Getter<Guild, EvtGuildSystemChannel>() {
            @Override
            public Guild get(EvtGuildSystemChannel event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildSystemChannel.class, JDA.class, new Getter<JDA, EvtGuildSystemChannel>() {
            @Override
            public JDA get(EvtGuildSystemChannel event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtGuildSystemChannel extends SimpleDiSkyEvent<GuildUpdateSystemChannelEvent> {
        public EvtGuildSystemChannel(GuildSystemChannel event) { }
    }

}