package info.itsthesky.disky.skript.expressions.embed.scope;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Last Embed")
@Description("This expression returns the last generated embed using the embed builder.")
@Since("1.0")
public class ExprLastEmbed extends SimpleExpression<EmbedBuilder> {

    static {
        Skript.registerExpression(ExprLastEmbed.class, EmbedBuilder.class, ExpressionType.SIMPLE,
                "[the] [last] [(made|created|generated)] embed"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Nullable
    @Override
    protected EmbedBuilder[] get(Event e) {
        return new EmbedBuilder[]{ScopeEmbed.lastEmbed};
    }

    @Override
    public Class<? extends EmbedBuilder> getReturnType() {
        return EmbedBuilder.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the last generated embed";
    }
}
