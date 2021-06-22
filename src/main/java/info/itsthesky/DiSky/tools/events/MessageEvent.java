package info.itsthesky.disky.tools.events;

import net.dv8tion.jda.api.entities.MessageChannel;

/**
 * Mean the event is related to a message event with a {@link MessageChannel}
 */
public interface MessageEvent {

    MessageChannel getMessageChannel();

}
