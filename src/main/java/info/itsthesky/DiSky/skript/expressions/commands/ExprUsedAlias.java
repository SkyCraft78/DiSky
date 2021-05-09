package info.itsthesky.disky.skript.expressions.commands;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.skript.commands.CommandEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Used Alias")
@Description("Get the used alias in a discord command trigger.)")
@Since("1.11")
public class ExprUsedAlias extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprUsedAlias.class, String.class, ExpressionType.SIMPLE,
                "(registered|all) [discord] (command[s]|cmd[s])"
        );
    }


    @Nullable
    @Override
    protected String[] get(Event e) {
        return new String[] {CommandEvent.lastEvent.getUsedAlias()};
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the used alias";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!ScriptLoader.isCurrentEvent(CommandEvent.class)) {
            Skript.error("The 'used alias' expression cannot be used outside a discord command trigger!");
            return false;
        }
        return true;
    }
}