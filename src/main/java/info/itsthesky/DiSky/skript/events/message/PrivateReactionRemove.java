package info.itsthesky.disky.skript.events.message;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.MessageEvent;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionRemoveEvent;

public class PrivateReactionRemove extends DiSkyEvent<PrivateMessageReactionRemoveEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", PrivateReactionRemove.class, EvtPrivateReactionRemove.class,
                "private reaction remove")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");

       EventValues.registerEventValue(EvtPrivateReactionRemove.class, MessageReaction.class, new Getter<MessageReaction, EvtPrivateReactionRemove>() {
            @Override
            public MessageReaction get(EvtPrivateReactionRemove event) {
                return event.getJDAEvent().getReaction();
            }
        }, 0);

       EventValues.registerEventValue(EvtPrivateReactionRemove.class, info.itsthesky.disky.tools.object.Emote.class, new Getter<info.itsthesky.disky.tools.object.Emote, EvtPrivateReactionRemove>() {
            @Override
            public info.itsthesky.disky.tools.object.Emote get(EvtPrivateReactionRemove event) {
                return info.itsthesky.disky.tools.object.Emote.fromReaction(event.getJDAEvent().getReactionEmote());
            }
        }, 0);

       EventValues.registerEventValue(EvtPrivateReactionRemove.class, User.class, new Getter<User, EvtPrivateReactionRemove>() {
            @Override
            public User get(EvtPrivateReactionRemove event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

       EventValues.registerEventValue(EvtPrivateReactionRemove.class, JDA.class, new Getter<JDA, EvtPrivateReactionRemove>() {
            @Override
            public JDA get(EvtPrivateReactionRemove event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtPrivateReactionRemove extends SimpleDiSkyEvent<PrivateMessageReactionRemoveEvent> implements MessageEvent {
        public EvtPrivateReactionRemove(PrivateReactionRemove event) { }

        @Override
        public MessageChannel getMessageChannel() {
            return getJDAEvent().getChannel();
        }
    }

}