package info.itsthesky.disky.skript.events.bot;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.ReadyEvent;

/* public class BotLoad extends DiSkyEvent<ReadyEvent> {

    static {
        DiSkyEvent.register("Bot Load", ReadyEvent.class, BotLoad.class,
                "[discord] bot (load|start)")
                .setName("Bot Load");


        EventValues.registerEventValue(EvtBotLoad.class, JDA.class, new Getter<JDA, EvtBotLoad>() {
            @Override
            public JDA get(EvtBotLoad event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtBotLoad extends SimpleDiSkyEvent<ReadyEvent> {
        public EvtBotLoad(BotLoad event) { }
    }

} */