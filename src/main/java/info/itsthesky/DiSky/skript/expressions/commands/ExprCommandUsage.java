package info.itsthesky.disky.skript.expressions.commands;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky.skript.commands.CommandObject;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Discord Command Usage")
@Description("Get the usage of a discord command.")
@Since("1.7")
public class ExprCommandUsage extends SimplePropertyExpression<CommandObject, String> {

    static {
        register(ExprCommandUsage.class, String.class,
                "[discord] [command] usage",
                "discordcommand"
        );
    }

    @Nullable
    @Override
    public String convert(CommandObject entity) {
        return entity.getUsage();
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "command usage";
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