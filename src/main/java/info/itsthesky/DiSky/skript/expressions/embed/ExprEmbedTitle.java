package info.itsthesky.disky.skript.expressions.embed;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky.DiSky;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Title of Track / Embed")
@Description("Get the title of a track and an embed, and set it for embed.")
@Examples({"set embed title of {_embed} to \"The addon's developer link\"",
        "clear embed title of {_embed}",
        "set {_title} to title of last played track"
})
@Since("1.0")
public class ExprEmbedTitle extends SimplePropertyExpression<Object, String> {

    static {
        register(ExprEmbedTitle.class, String.class,
                "[(embed|track)] title",
                "embed/track"
        );
    }

    @Nullable
    @Override
    public String convert(Object entity) {
        if (entity instanceof EmbedBuilder) {
            return ((EmbedBuilder) entity).isEmpty() ? null : ((EmbedBuilder) entity).build().getTitle();
        } else if (entity instanceof AudioTrack) {
            return ((AudioTrack) entity).getInfo().title;
        }
        return null;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "embed title";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.RESET || mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(String.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (delta == null || delta[0] == null) return;
        switch (mode) {
            case RESET:
                for (Object entity : getExpr().getArray(e)) {
                    if (entity instanceof AudioTrack) return;
                    EmbedBuilder embed = (EmbedBuilder) entity;
                    MessageEmbed builded = embed.isEmpty() ? null : embed.build();
                    embed.setTitle(
                            null,
                            (builded == null || builded.getTitle() == null) ? null : builded.getUrl()
                    );
                }
                break;
            case SET:
                String value = delta[0].toString();
                if (value.length() > 256) {
                    DiSky.getInstance().getLogger()
                            .warning("The embed's title cannot be bigger than 256 characters. The one you're trying to set is '"+value.length()+"' length!");
                    return;
                }
                for (Object entity : getExpr().getArray(e)) {
                    if (entity instanceof AudioTrack) return;
                    EmbedBuilder embed = (EmbedBuilder) entity;
                    MessageEmbed builded = embed.isEmpty() ? null : embed.build();
                    embed.setTitle(
                            value,
                            (builded == null || builded.getTitle() == null) ? null : builded.getUrl()
                    );
                }
                break;
        }
    }
}