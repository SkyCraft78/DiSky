package info.itsthesky.disky.skript.expressions.bot;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky.managers.BotManager;
import net.dv8tion.jda.api.JDA;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Default Bot Prefix")
@Description("Get, set or clear the default prefix of a bot. This prefix will be added to every prefixes in commands.")
@Examples("set default prefix of event-bot to \".\"")
@Since("1.9")
public class ExprBotPrefix extends SimplePropertyExpression<Object, String> {

    static {
        register(ExprBotPrefix.class, String.class,
                "[bot] [default] prefix",
                "string/bot"
        );
    }

    @Nullable
    @Override
    public String convert(Object entity) {
        if (entity instanceof JDA) return BotManager.prefixes.get(entity);
        return BotManager.prefixes.get(BotManager.getBot(entity.toString(), false));
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "default bot prefix";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(String.class);
        } else if (mode == Changer.ChangeMode.RESET) {
            return CollectionUtils.array();
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        @Nullable String prefix = delta[0] == null ? null : delta[0].toString();
        Object entity = getExpr().getSingle(e);
        switch (mode) {
            case SET:
                if (prefix == null) return;
                if (entity instanceof JDA) BotManager.setDefaultPrefixes((JDA) entity, prefix);
                if (entity instanceof String) BotManager.setDefaultPrefixes(BotManager.getBot(entity.toString(), false), prefix);
                break;
            case RESET:
                if (!(prefix == null)) return;
                if (entity instanceof JDA) BotManager.setDefaultPrefixes((JDA) entity, null);
                if (entity instanceof String) BotManager.setDefaultPrefixes(BotManager.getBot(entity.toString(), false), null);
        }
    }
}