package info.itsthesky.disky.skript.events.message;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.MessageEvent;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;

public class MessageDelete extends DiSkyEvent<GuildMessageDeleteEvent> {

    public static String content;
    public static Long id;

    static {
        DiSkyEvent.register("Inner Event Name", MessageDelete.class, EvtMessageDelete.class,
                "message delete")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


        EventValues.registerEventValue(EvtMessageDelete.class, TextChannel.class, new Getter<TextChannel, EvtMessageDelete>() {
            @Override
            public TextChannel get(EvtMessageDelete event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EvtMessageDelete.class, Guild.class, new Getter<Guild, EvtMessageDelete>() {
            @Override
            public Guild get(EvtMessageDelete event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtMessageDelete.class, JDA.class, new Getter<JDA, EvtMessageDelete>() {
            @Override
            public JDA get(EvtMessageDelete event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EvtMessageDelete.class, String.class, new Getter<String, EvtMessageDelete>() {
            @Override
            public String get(EvtMessageDelete event) {
                return content;
            }
        }, 0);

        EventValues.registerEventValue(EvtMessageDelete.class, Number.class, new Getter<Number, EvtMessageDelete>() {
            @Override
            public Number get(EvtMessageDelete event) {
                return id;
            }
        }, 0);

    }

    public static class EvtMessageDelete extends SimpleDiSkyEvent<GuildMessageDeleteEvent> implements MessageEvent {
        public EvtMessageDelete(MessageDelete event) { }

        @Override
        public MessageChannel getMessageChannel() {
            return getJDAEvent().getChannel();
        }
    }

}