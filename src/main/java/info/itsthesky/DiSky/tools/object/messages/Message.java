package info.itsthesky.disky.tools.object.messages;

import net.dv8tion.jda.api.EmbedBuilder;

public class Message {

    private String content;
    private EmbedBuilder embed;
    private MessageType type;

    public Message(String content) {
        this.type = MessageType.TEXT;
        this.content = content;
    }

    public Message(EmbedBuilder embed) {
        this.type = MessageType.EMBED;
        this.embed = embed;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public EmbedBuilder getEmbed() {
        return embed;
    }

    public void setEmbed(EmbedBuilder embed) {
        this.embed = embed;
    }

    public MessageType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", embed=" + embed +
                ", type=" + type +
                '}';
    }
}
