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

@Name("Discord Command Executable In")
@Description("Get executable in of a discord command. (guild or direct message)")
@Since("1.7")
public class ExprCommandExecutableIn extends SimplePropertyExpression<CommandObject, String> {

    static {
        register(ExprCommandExecutableIn.class, String.class,
                "[discord] [command] executable (state|in) [value]",
                "discordcommand"
        );
    }

    @Nullable
    @Override
    public String convert(CommandObject entity) {
        return entity.getExecutableIn().get(0).isGuild() ? "guild" : "direct message";
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "command aliases";
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