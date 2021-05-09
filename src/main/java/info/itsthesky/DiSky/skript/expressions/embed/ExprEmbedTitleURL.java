package info.itsthesky.disky.skript.expressions.embed;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky.DiSky;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Embed Title URL")
@Description("Set or clear the title url of an embed.")
@Examples({"set title url of {_embed} to \"The addon's developer link\"",
        "clear title url of {_embed}"})
@Since("1.4.3")
public class ExprEmbedTitleURL extends SimplePropertyExpression<EmbedBuilder, String> {

    static {
        register(ExprEmbedTitleURL.class, String.class,
                "[embed] title url",
                "embed"
        );
    }

    @Nullable
    @Override
    public String convert(EmbedBuilder embed) {
        return embed.isEmpty() ? null : embed.build().getUrl();
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "embed title url";
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
                    embed.setTitle(
                           (builded == null || builded.getTitle() == null) ? null : builded.getTitle(),
                            null
                    );
                }
                break;
            case SET:
                String value = delta[0].toString();
                for (EmbedBuilder embed : getExpr().getArray(e)) {
                    MessageEmbed builded = embed.isEmpty() ? null : embed.build();
                    try {
                        embed.setTitle(
                               (builded == null || builded.getTitle() == null) ? null : builded.getTitle(),
                                value
                        );
                    } catch (IllegalArgumentException ex) {
                        DiSky.getInstance().getLogger().warning("DiSky tried to load the URL '"+value+"' but it seems to be malformed!");
                    }
                }
                break;
        }
    }
}