package info.itsthesky.disky.skript.events.text;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateParentEvent;

public class TextParent extends DiSkyEvent<TextChannelUpdateParentEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", TextParent.class, EvtTextParent.class,
                "[discord] text [channel] parent (update|change)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtTextParent.class, Category.class, new Getter<Category, EvtTextParent>() {
            @Override
            public Category get(EvtTextParent event) {
                return event.getJDAEvent().getNewParent();
            }
        }, 1);

       EventValues.registerEventValue(EvtTextParent.class, Category.class, new Getter<Category, EvtTextParent>() {
            @Override
            public Category get(EvtTextParent event) {
                return event.getJDAEvent().getOldParent();
            }
        }, -1);

       EventValues.registerEventValue(EvtTextParent.class, TextChannel.class, new Getter<TextChannel, EvtTextParent>() {
            @Override
            public TextChannel get(EvtTextParent event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextParent.class, Guild.class, new Getter<Guild, EvtTextParent>() {
            @Override
            public Guild get(EvtTextParent event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtTextParent.class, JDA.class, new Getter<JDA, EvtTextParent>() {
            @Override
            public JDA get(EvtTextParent event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtTextParent extends SimpleDiSkyEvent<TextChannelUpdateParentEvent> {
        public EvtTextParent(TextParent event) { }
    }

}