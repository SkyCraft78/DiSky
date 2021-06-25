package info.itsthesky.disky.skript.events.text;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateNewsEvent;

public class TextNews extends DiSkyEvent<TextChannelUpdateNewsEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", TextNews.class, EvtTextNews.class,
                "[discord] text [channel] news (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtTextNews.class, TextChannel.class, new Getter<TextChannel, EvtTextNews>() {
            @Override
            public TextChannel get(EvtTextNews event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextNews.class, Guild.class, new Getter<Guild, EvtTextNews>() {
            @Override
            public Guild get(EvtTextNews event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextNews.class, JDA.class, new Getter<JDA, EvtTextNews>() {
            @Override
            public JDA get(EvtTextNews event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtTextNews extends SimpleDiSkyEvent<TextChannelUpdateNewsEvent> {
        public EvtTextNews(TextNews event) { }
    }

}