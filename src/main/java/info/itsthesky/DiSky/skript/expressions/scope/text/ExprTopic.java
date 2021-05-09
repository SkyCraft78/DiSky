package info.itsthesky.disky.skript.expressions.scope.text;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Topic of a text channel (or builder)")
@Description("Get or set the topic of text channel or builder.")
@Examples("set topic of text channel builder to \"I love custom topic :heart:\"")
@Since("1.4")
public class ExprTopic extends SimplePropertyExpression<Object, String> {

    static {
        register(ExprTopic.class, String.class,
                "topic",
                "channel/textchannel/textchannelbuilder"
        );
    }

    @Nullable
    @Override
    public String convert(Object entity) {
        if (entity instanceof TextChannel) return ((TextChannel) entity).getTopic();
        if (entity instanceof GuildChannel) return ((GuildChannel) entity).getType().equals(ChannelType.TEXT) ? ((TextChannel) entity).getTopic() : null;
        return null;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "topic";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(String.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (delta == null || delta.length == 0) return;
        String topic = delta[0].toString();
        if (mode == Changer.ChangeMode.SET) {
            for (Object entity : getExpr().getArray(e)) {
                if (entity instanceof TextChannel) ((TextChannel) entity).getManager().setTopic(topic).queue();
                if ((entity instanceof GuildChannel) && ((GuildChannel) entity).getType().equals(ChannelType.TEXT)) ((TextChannel) entity).getManager().setTopic(topic).queue();
            }
        }
    }
}