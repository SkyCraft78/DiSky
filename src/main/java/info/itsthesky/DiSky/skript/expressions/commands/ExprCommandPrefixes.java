package info.itsthesky.disky.skript.expressions.commands;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky.skript.commands.CommandEvent;
import info.itsthesky.disky.skript.commands.CommandObject;
import info.itsthesky.disky.tools.MultiplyPropertyExpression;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Name("Discord Command Prefixes")
@Description("Get prefixes of a discord command.")
@Since("1.7")
public class ExprCommandPrefixes extends MultiplyPropertyExpression<CommandObject, String> {

    static {
        register(ExprCommandPrefixes.class, String.class,
                "[discord] [command] prefixes",
                "discordcommand"
        );
    }

    @Nullable
    @Override
    public String[] convert(CommandObject entity) {
        List<String> list = new ArrayList<>();
        for (Expression<String> expr : entity.getPrefixes()) {
            list.add(expr.getSingle(CommandEvent.lastEvent));
        }
        return list.toArray(new String[0]);
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "command prefixes";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {

    }
}