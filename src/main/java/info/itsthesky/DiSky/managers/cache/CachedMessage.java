package info.itsthesky.disky.managers.cache;

import net.dv8tion.jda.api.entities.Message;

/**
 * Made by ItsTheSky the 20/03/2021 <br>
 * CachedMessage object, to work with getter above.
 */
public class CachedMessage {
    private final String content;
    private final String authorID;
    private final String messageID;

    public CachedMessage(Message message) {
        this.authorID = message.getAuthor().getId();
        this.content = message.getContentRaw();
        this.messageID = message.getId();
    }

    public String getContent() {
        return content;
    }

    public String getAuthorID() {
        return authorID;
    }

    public String getMessageID() {
        return messageID;
    }
}
