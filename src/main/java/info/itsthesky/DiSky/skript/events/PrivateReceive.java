package info.itsthesky.disky.skript.events;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.MessageEvent;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

public class PrivateReceive extends DiSkyEvent<PrivateMessageReceivedEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", PrivateReceive.class, EvtPrivateReceive.class,
                "(direct|private) message [receive]")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");

       EventValues.registerEventValue(EvtPrivateReceive.class, User.class, new Getter<User, EvtPrivateReceive>() {
            @Override
            public User get(EvtPrivateReceive event) {
                return event.getJDAEvent().getAuthor();
            }
        }, 0);

       EventValues.registerEventValue(EvtPrivateReceive.class, UpdatingMessage.class, new Getter<UpdatingMessage, EvtPrivateReceive>() {
            @Override
            public UpdatingMessage get(EvtPrivateReceive event) {
                return UpdatingMessage.from(event.getJDAEvent().getMessage());
            }
        }, 0);

       EventValues.registerEventValue(EvtPrivateReceive.class, String.class, new Getter<String, EvtPrivateReceive>() {
            @Override
            public String get(EvtPrivateReceive event) {
                return event.getJDAEvent().getMessageId();
            }
        }, 0);

       EventValues.registerEventValue(EvtPrivateReceive.class, PrivateChannel.class, new Getter<PrivateChannel, EvtPrivateReceive>() {
            @Override
            public PrivateChannel get(EvtPrivateReceive event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtPrivateReceive.class, JDA.class, new Getter<JDA, EvtPrivateReceive>() {
            @Override
            public JDA get(EvtPrivateReceive event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtPrivateReceive extends SimpleDiSkyEvent<PrivateMessageReceivedEvent> implements MessageEvent {
        public EvtPrivateReceive(PrivateReceive event) { }

        @Override
        public MessageChannel getMessageChannel() {
            return getJDAEvent().getChannel();
        }
    }

}