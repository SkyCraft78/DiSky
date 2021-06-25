package info.itsthesky.disky.skript.events.bot;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;

public class BotJoin extends DiSkyEvent<GuildJoinEvent> {

    public static Invite usedInvite;

    static {
        DiSkyEvent.register("Inner Event Name", BotJoin.class, EvtBotJoin.class,
                "bot join [guild]")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");

       EventValues.registerEventValue(EvtBotJoin.class, Guild.class, new Getter<Guild, EvtBotJoin>() {
            @Override
            public Guild get(EvtBotJoin event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtBotJoin.class, JDA.class, new Getter<JDA, EvtBotJoin>() {
            @Override
            public JDA get(EvtBotJoin event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EvtBotJoin.class, Invite.class, new Getter<Invite, EvtBotJoin>() {
            @Override
            public Invite get(EvtBotJoin event) {
                return usedInvite;
            }
        }, 0);

    }

    public static class EvtBotJoin extends SimpleDiSkyEvent<GuildJoinEvent> {
        public EvtBotJoin(BotJoin event) { }
    }

}