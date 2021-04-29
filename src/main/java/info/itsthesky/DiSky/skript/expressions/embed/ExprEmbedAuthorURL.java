package info.itsthesky.DiSky.skript.expressions.embed;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Embed Author URL")
@Description("Set or clear the author url of an embed")
@Examples({"set author url of {_embed} to \"https://www.youtube.com/watch?v=ynTrFZbrAl8\"",
        "clear author url of {_embed}"})
@Since("1.4.3")
public class ExprEmbedAuthorURL extends SimplePropertyExpression<EmbedBuilder, String> {

    static {
        register(ExprEmbedAuthorURL.class, String.class,
                "[embed] author url",
                "embed"
        );
    }

    @Nullable
    @Override
    public String convert(EmbedBuilder embed) {
        return embed.isEmpty() ? null : embed.build().getAuthor().getUrl();
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "embed author url";
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
                for (EmbedBuilder embed : getExpr().getArray(e)) {
                    MessageEmbed builded = embed.isEmpty() ? null : embed.build();
                    embed.setAuthor(
                            (builded == null || builded.getAuthor() == null) ? null : builded.getAuthor().getName(),
                            null,
                            (builded == null || builded.getAuthor() == null) ? null : builded.getAuthor().getIconUrl()
                    );
                }
                break;
            case SET:
                String value = delta[0].toString();
                for (EmbedBuilder embed : getExpr().getArray(e)) {
                    MessageEmbed builded = embed.isEmpty() ? null : embed.build();
                    embed.setAuthor(
                            (builded == null || builded.getAuthor() == null) ? null : builded.getAuthor().getName(),
                            value,
                            (builded == null || builded.getAuthor() == null) ? null : builded.getAuthor().getIconUrl()
                    );
                }
                break;
        }
    }
}