package info.itsthesky.disky.skript.expressions.scope.buttons;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky.tools.object.ButtonBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Link State of Button")
@Description("Get or set the link state of a button. If it's a link, the design will change ro primary & will add a small link icon on the right.")
@Examples("set link state of button to true")
@Since("1.4")
public class ExpLinkState extends SimplePropertyExpression<ButtonBuilder, Boolean> {

    static {
        register(ExpLinkState.class, Boolean.class,
                "link state",
                "button"
        );
    }

    @Nullable
    @Override
    public Boolean convert(ButtonBuilder entity) {
        return entity.isLink();
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    protected String getPropertyName() {
        return "link state";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Boolean.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (delta == null || delta.length == 0) return;
        if (!(delta[0] instanceof Boolean)) return;
        boolean newState = (Boolean) delta[0];
        if (mode == Changer.ChangeMode.SET) {
            for (ButtonBuilder button : getExpr().getArray(e)) {
                button.setLink(newState);
            }
        }
    }
}