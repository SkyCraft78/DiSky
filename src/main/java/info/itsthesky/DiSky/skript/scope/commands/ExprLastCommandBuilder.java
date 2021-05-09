package info.itsthesky.disky.skript.scope.commands;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.EffectSection;
import info.itsthesky.disky.tools.object.SlashCommand;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Last Command Builder")
@Description("Return the last used command builder.")
@Since("1.5")
public class ExprLastCommandBuilder extends SimpleExpression<SlashCommand> {

    static {
        Skript.registerExpression(ExprLastCommandBuilder.class, SlashCommand.class, ExpressionType.SIMPLE,
                "[the] [last] [(generated|created)] [slash] command [builder]"
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!parseResult.expr.equals("command")) return true;
        return EffectSection.isCurrentSection(ScopeCommand.class);
    }

    @Nullable
    @Override
    protected SlashCommand[] get(Event e) {
        return new SlashCommand[]{ScopeCommand.lastBuilder};
    }

    @Override
    public Class<? extends SlashCommand> getReturnType() {
        return SlashCommand.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the last slash command builder";
    }
}
