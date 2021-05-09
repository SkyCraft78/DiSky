package info.itsthesky.disky.tools.object.messages;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;


public class Channel {

    private MessageChannel channel;

    public Channel(MessageChannel channel) {
        this.channel = channel;
    }

    public MessageChannel getChannel() {
        return channel;
    }
    public TextChannel getTextChannel() {
        return channel.getJDA().getTextChannelById(channel.getId());
    }
}
