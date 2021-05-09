package info.itsthesky.disky.skript.expressions.embed;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.util.Date;
import ch.njol.util.coll.CollectionUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.time.Instant;

@Name("Embed TimeStamp")
@Description("Set or clear the timestamp of an embed.")
@Examples({"set timestamp of embed to now",
        "clear timestamp of embed"})
@Since("1.1")
public class ExprEmbedTimeStamp extends SimplePropertyExpression<EmbedBuilder, Date> {

    static {
        register(ExprEmbedTimeStamp.class, Date.class,
                "[embed] timestamp",
                "embed"
        );
    }

    @Nullable
    @Override
    public Date convert(EmbedBuilder embed) {
        return embed.isEmpty() ? null : new Date(embed.build().getTimestamp().getNano());
    }

    @Override
    public Class<? extends Date> getReturnType() {
        return Date.class;
    }

    @Override
    protected String getPropertyName() {
        return "embed timestamp";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.RESET || mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Date.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (delta == null || delta.length == 0) return;

        switch (mode) {
            case RESET:
                for (EmbedBuilder embed : getExpr().getArray(e)) {
                    embed.setDescription(null);
                }
                break;
            case SET:
                Date date = (Date) delta[0];
                for (EmbedBuilder embed : getExpr().getArray(e)) {
                    embed.setTimestamp(Instant.ofEpochMilli(date.getTimestamp()));
                }
                break;
            default:
                return;
        }
    }
}