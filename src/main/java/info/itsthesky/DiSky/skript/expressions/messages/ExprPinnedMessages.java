package info.itsthesky.disky.skript.expressions.messages;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky.tools.MultiplyPropertyExpression;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Pinned Messages")
@Description("Get all pinned message of a specific text channel.")
@Examples("set {_msg::*} to pinned message of event-channel")
@Since("1.0")
public class ExprPinnedMessages extends MultiplyPropertyExpression<Object, UpdatingMessage> {

    static {
        register(ExprPinnedMessages.class, UpdatingMessage.class,
                "pin[ed] (message[s]|msg)",
                "channel/textchannel"
        );
    }

    @Nullable
    @Override
    public UpdatingMessage[] convert(Object entity) {
        if (entity instanceof GuildChannel && ((GuildChannel) entity).getType().equals(ChannelType.TEXT)) return UpdatingMessage.convert(((TextChannel) entity).retrievePinnedMessages().complete().toArray(new Message[0]));
        if (entity instanceof TextChannel) return UpdatingMessage.convert(((TextChannel) entity).retrievePinnedMessages().complete().toArray(new Message[0]));
        return null;
    }

    @Override
    public Class<? extends UpdatingMessage> getReturnType() {
        return UpdatingMessage.class;
    }

    @Override
    protected String getPropertyName() {
        return "pinned messages";
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