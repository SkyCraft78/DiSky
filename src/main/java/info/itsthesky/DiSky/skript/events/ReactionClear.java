package info.itsthesky.disky.skript.events;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.MessageEvent;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveAllEvent;

public class ReactionClear extends DiSkyEvent<GuildMessageReactionRemoveAllEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", ReactionClear.class, EvtReactionClear.class,
                "reaction (remove all|clear)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtReactionClear.class, TextChannel.class, new Getter<TextChannel, EvtReactionClear>() {
            @Override
            public TextChannel get(EvtReactionClear event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtReactionClear.class, Guild.class, new Getter<Guild, EvtReactionClear>() {
            @Override
            public Guild get(EvtReactionClear event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtReactionClear.class, JDA.class, new Getter<JDA, EvtReactionClear>() {
            @Override
            public JDA get(EvtReactionClear event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EvtReactionClear.class, UpdatingMessage.class, new Getter<UpdatingMessage, EvtReactionClear>() {
            @Override
            public UpdatingMessage get(EvtReactionClear event) {
                return UpdatingMessage.from(event.getJDAEvent().getMessageId());
            }
        }, 0);

    }

    public static class EvtReactionClear extends SimpleDiSkyEvent<GuildMessageReactionRemoveAllEvent> implements MessageEvent {
        public EvtReactionClear(ReactionClear event) { }

        @Override
        public MessageChannel getMessageChannel() {
            return getJDAEvent().getChannel();
        }
    }

}