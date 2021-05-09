package info.itsthesky.disky.skript.scope.textchannels;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.object.TextChannelBuilder;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Last Channel Builder")
@Description("Return the last used channels builder.")
@Since("1.4")
public class ExprLastChannelBuilder extends SimpleExpression<TextChannelBuilder> {

    static {
        Skript.registerExpression(ExprLastChannelBuilder.class, TextChannelBuilder.class, ExpressionType.SIMPLE,
                "[the] [last] [(generated|created)] text channel [builder]"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Nullable
    @Override
    protected TextChannelBuilder[] get(Event e) {
        return new TextChannelBuilder[]{ScopeTextChannel.lastBuilder};
    }

    @Override
    public Class<? extends TextChannelBuilder> getReturnType() {
        return TextChannelBuilder.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the last text channel builder";
    }
}
