package info.itsthesky.disky.skript.expressions.messages;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky.tools.MultiplyPropertyExpression;
import net.dv8tion.jda.api.entities.Message;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Message Attachments")
@Description("Retrieve every attachments a message have.")
@Examples("set {_a::*} to attachments of event-message")
@Since("1.7")
public class ExprMessageAttachments extends MultiplyPropertyExpression<Message, Message.Attachment> {

    static {
        register(ExprMessageAttachments.class, Message.Attachment.class,
                "[message] attachments",
                "message"
        );
    }

    @Nullable
    @Override
    public Message.Attachment[] convert(Message entity) {
        return entity.getAttachments().toArray(new Message.Attachment[0]);
    }

    @Override
    public Class<? extends Message.Attachment> getReturnType() {
        return Message.Attachment.class;
    }

    @Override
    protected String getPropertyName() {
        return "message attachments";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {

    }
}