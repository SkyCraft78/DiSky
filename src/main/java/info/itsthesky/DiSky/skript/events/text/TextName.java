package info.itsthesky.disky.skript.events.text;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateNSFWEvent;

public class TextName extends DiSkyEvent<TextChannelUpdateNSFWEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", TextName.class, EvtTextName.class,
                "[discord] text [channel] name (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtTextName.class, TextChannel.class, new Getter<TextChannel, EvtTextName>() {
            @Override
            public TextChannel get(EvtTextName event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextName.class, Guild.class, new Getter<Guild, EvtTextName>() {
            @Override
            public Guild get(EvtTextName event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextName.class, JDA.class, new Getter<JDA, EvtTextName>() {
            @Override
            public JDA get(EvtTextName event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtTextName extends SimpleDiSkyEvent<TextChannelUpdateNSFWEvent> {
        public EvtTextName(TextName event) { }
    }

}