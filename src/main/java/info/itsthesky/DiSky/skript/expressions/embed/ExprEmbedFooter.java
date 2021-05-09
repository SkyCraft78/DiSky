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

@Name("Embed Footer")
@Description("Set or clear the footer of an embed")
@Examples({"set footer of {_embed} to \"Bot made by AUTHOR\"",
        "clear footer of {_embed}"})
@Since("1.0")
public class ExprEmbedFooter extends SimplePropertyExpression<EmbedBuilder, String> {

    static {
        register(ExprEmbedFooter.class, String.class,
                "[embed] footer",
                "embed"
        );
    }

    @Nullable
    @Override
    public String convert(EmbedBuilder embed) {
        return embed.isEmpty() ? null : embed.build().getFooter().getText();
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "embed footer";
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
                            null,
                            (builded == null || builded.getFooter() == null) ? null : builded.getFooter().getIconUrl()
                    );
                }
                break;
            case SET:
                String value = delta[0].toString();
                if (value.length() > 2048) {
                    DiSky.getInstance().getLogger()
                            .warning("The embed's footer cannot be bigger than 2048 characters. The one you're trying to set is '"+value.length()+"' length!");
                    return;
                }
                for (EmbedBuilder embed : getExpr().getArray(e)) {
                    MessageEmbed builded = embed.isEmpty() ? null : embed.build();
                    embed.setFooter(
                            value,
                            (builded == null || builded.getFooter() == null) ? null : builded.getFooter().getIconUrl()
                    );
                }
                break;
        }
    }
}