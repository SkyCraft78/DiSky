package info.itsthesky.disky.skript.events.text;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdatePositionEvent;

public class TextPosition extends DiSkyEvent<TextChannelUpdatePositionEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", TextPosition.class, EvtTextPosition.class,
                "[discord] text [channel] position (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtTextPosition.class, TextChannel.class, new Getter<TextChannel, EvtTextPosition>() {
            @Override
            public TextChannel get(EvtTextPosition event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextPosition.class, Guild.class, new Getter<Guild, EvtTextPosition>() {
            @Override
            public Guild get(EvtTextPosition event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextPosition.class, JDA.class, new Getter<JDA, EvtTextPosition>() {
            @Override
            public JDA get(EvtTextPosition event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtTextPosition extends SimpleDiSkyEvent<TextChannelUpdatePositionEvent> {
        public EvtTextPosition(TextPosition event) { }
    }

}