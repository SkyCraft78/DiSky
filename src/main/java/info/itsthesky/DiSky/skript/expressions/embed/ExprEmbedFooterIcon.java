package info.itsthesky.disky.skript.expressions.embed;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Embed Footer Icon")
@Description("Set or clear the footer icon of an embed")
@Examples({"set footer icon of {_embed} to \"https://www.youtube.com/watch?v=ynTrFZbrAl8\"",
        "clear footer icon of {_embed}"})
@Since("1.4.3")
public class ExprEmbedFooterIcon extends SimplePropertyExpression<EmbedBuilder, String> {

    static {
        register(ExprEmbedFooterIcon.class, String.class,
                "[embed] footer icon",
                "embed"
        );
    }

    @Nullable
    @Override
    public String convert(EmbedBuilder embed) {
        return embed.isEmpty() ? null : embed.build().getFooter().getIconUrl();
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "embed footer url";
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
                    embed.setFooter(
                            (builded == null || builded.getFooter() == null) ? null : builded.getFooter().getText(),
                            null
                    );
                }
                break;
            case SET:
                String value = delta[0].toString();
                for (EmbedBuilder embed : getExpr().getArray(e)) {
                    MessageEmbed builded = embed.isEmpty() ? null : embed.build();
                    embed.setFooter(
                            (builded == null || builded.getFooter() == null) ? null : builded.getFooter().getText(),
                            value
                    );
                }
                break;
        }
    }
}