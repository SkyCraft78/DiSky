package info.itsthesky.DiSky.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.DiSky.tools.Utils;
import org.bukkit.event.Event;

@Name("Type of Variable")
@Description("Get the Skript type of a Skript variable.")
@Since("1.9")
public class ExprVarTypeOf extends SimpleExpression<String> {
    static {
        Skript.registerExpression(ExprVarTypeOf.class, String.class, ExpressionType.SIMPLE,
                "["+Utils.getPrefixName()+"] var[iable] type of [var] %object%");
    }

    private Expression<Object> exprVar;

    @Override
    protected String[] get(Event e) {
        Object var = exprVar.getSingle(e);
        if (var == null) return new String[0];
        String clazz = var.getClass().getName();
        int size = clazz.length();
        String value = clazz.split("\\.")[size - 1]
                .replaceAll("Impl", "");
        return new String[] {
                value
        };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "type of variable " + exprVar.toString(e, debug);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprVar = (Expression<Object>) exprs[0];
        return true;
    }
}