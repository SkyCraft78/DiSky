package info.itsthesky.disky.skript.scope.bot;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.skript.scope.buttons.ScopeButton;
import info.itsthesky.disky.tools.object.BotBuilder;
import info.itsthesky.disky.tools.object.ButtonBuilder;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Last Bot Builder")
@Description("Return the last used bot builder.")
@Since("1.14")
public class ExprLastBotBuilder extends SimpleExpression<BotBuilder> {

    static {
        Skript.registerExpression(ExprLastBotBuilder.class, BotBuilder.class, ExpressionType.SIMPLE,
                "[the] [last] [(generated|created)] bot [builder]"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Nullable
    @Override
    protected BotBuilder[] get(Event e) {
        return new BotBuilder[]{ScopeBotBuilder.lastBuilder};
    }

    @Override
    public Class<? extends BotBuilder> getReturnType() {
        return BotBuilder.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the last bot builder";
    }
}
