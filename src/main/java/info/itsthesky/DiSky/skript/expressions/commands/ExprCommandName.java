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

@Name("Discord Command Name")
@Description("Get the name of a discord command.")
@Since("1.7")
public class ExprCommandName extends SimplePropertyExpression<CommandObject, String> {

    static {
        register(ExprCommandName.class, String.class,
                "[discord] [command] name",
                "discordcommand"
        );
    }

    @Nullable
    @Override
    public String convert(CommandObject entity) {
        return entity.getName();
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "command name";
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