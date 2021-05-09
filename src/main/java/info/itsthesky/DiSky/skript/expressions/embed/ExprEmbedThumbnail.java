package info.itsthesky.disky.skript.expressions.embed;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Embed Thumbnail")
@Description("Set or clear the thumbnail of an embed.")
@Examples({"set thumbnail of {_embed} to \"https://d1fmx1rbmqrxrr.cloudfront.net/cnet/optim/i/edit/2019/04/eso1644bsmall__w770.jpg\"",
        "clear thumbnail of {_embed}"})
@Since("1.0")
public class ExprEmbedThumbnail extends SimplePropertyExpression<EmbedBuilder, String> {

    static {
        register(ExprEmbedThumbnail.class, String.class,
                "[embed] (thumbnail|tb)",
                "embed"
        );
    }

    @Nullable
    @Override
    public String convert(EmbedBuilder embed) {
        return embed.isEmpty() ? null : embed.build().getThumbnail().getUrl();
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "embed thumbnail";
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
        switch (mode) {
            case RESET:
                for (EmbedBuilder embed : getExpr().getArray(e)) {
                    embed.setThumbnail(null);
                }
                break;
            case SET:
                for (EmbedBuilder embed : getExpr().getArray(e)) {
                    embed.setThumbnail((String) delta[0]);
                }
                break;
            default:
                return;
        }
    }
}