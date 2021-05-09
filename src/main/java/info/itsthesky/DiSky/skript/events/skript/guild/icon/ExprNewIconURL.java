package info.itsthesky.disky.skript.events.skript.guild.icon;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("New Icon URL")
@Description("Get the new guild icon url in a guild icon update event.")
@Since("1.9")
public class ExprNewIconURL extends SimpleExpression<String> {

    public static String newIconURL;

    static {
        Skript.registerExpression(ExprNewIconURL.class, String.class, ExpressionType.SIMPLE,
                "[the] new guild icon url"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Nullable
    @Override
    protected String[] get(Event e) { return new String[]{newIconURL}; }
    @Override
    public Class<? extends String> getReturnType() { return String.class; }
    @Override
    public boolean isSingle() { return true; }
    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the new guild icon url";
    }
}
