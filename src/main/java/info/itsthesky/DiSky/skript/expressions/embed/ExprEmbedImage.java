package info.itsthesky.disky.skript.expressions.embed;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;

@Name("Embed Image")
@Description("Set or clear the image of an embed.")
@Examples({"set image of {_embed} to \"https://d1fmx1rbmqrxrr.cloudfront.net/cnet/optim/i/edit/2019/04/eso1644bsmall__w770.jpg\"",
        "clear image of {_embed}"})
@Since("1.0")
public class ExprEmbedImage extends SimplePropertyExpression<EmbedBuilder, String> {

    static {
        register(ExprEmbedImage.class, String.class,
                "[embed] (image|img)",
                "embed"
        );
    }

    @Nullable
    @Override
    public String convert(EmbedBuilder embed) {
        return embed.isEmpty() ? null : embed.build().getImage().getUrl();
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "embed image";
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
                    embed.setImage(null);
                }
                break;
            case SET:
                for (EmbedBuilder embed : getExpr().getArray(e)) {
                    if (delta[0] instanceof String) {
                        embed.setImage(delta[0].toString());
                    }
                }
                break;
        }
    }
}