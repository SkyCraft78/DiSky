package info.itsthesky.disky.skript.events;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;

public class BotLeave extends DiSkyEvent<GuildLeaveEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", BotLeave.class, EvtBotLeave.class,
                "bot leave [guild]")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtBotLeave.class, Guild.class, new Getter<Guild, EvtBotLeave>() {
            @Override
            public Guild get(EvtBotLeave event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtBotLeave.class, JDA.class, new Getter<JDA, EvtBotLeave>() {
            @Override
            public JDA get(EvtBotLeave event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtBotLeave extends SimpleDiSkyEvent<GuildLeaveEvent> {
        public EvtBotLeave(BotLeave event) { }
    }

}