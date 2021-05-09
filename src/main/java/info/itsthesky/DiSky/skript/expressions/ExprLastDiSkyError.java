package info.itsthesky.disky.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Last DiSky error")
@Description("Get the last disky error.")
@Examples("last disky error")
@Since("1.8")
public class ExprLastDiSkyError extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprLastDiSkyError.class, String.class, ExpressionType.SIMPLE,
                "[the] last disky error"
        );
    }

    public static String lastError;

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Nullable
    @Override
    protected String[] get(Event e) {
        return lastError == null ? new String[0] : new String[] {lastError};
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the last disky error";
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }
}