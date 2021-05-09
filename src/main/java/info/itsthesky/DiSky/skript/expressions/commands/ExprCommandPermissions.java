package info.itsthesky.disky.skript.expressions.commands;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky.skript.commands.CommandObject;
import info.itsthesky.disky.tools.MultiplyPropertyExpression;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Discord Command Permissions")
@Description("Get permissions of a discord command.")
@Since("1.7")
public class ExprCommandPermissions extends MultiplyPropertyExpression<CommandObject, String> {

    static {
        register(ExprCommandPermissions.class, String.class,
                "[discord] [command] permission[s]",
                "discordcommand"
        );
    }

    @Nullable
    @Override
    public String[] convert(CommandObject entity) {
        return entity.getPerms().toArray(new String[0]);
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "command permissions";
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