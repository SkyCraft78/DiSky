package info.itsthesky.disky.skript.events.skript.messages.MessageEdit;

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

@Name("Old Message Content")
@Description("Get the old content of a message in a message edit event.")
@Since("1.4.1")
public class ExprOldContent extends SimpleExpression<String> {

    public static String oldContent;

    static {
        Skript.registerExpression(ExprOldContent.class, String.class, ExpressionType.SIMPLE,
                "[the] old [message] content "
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Nullable
    @Override
    protected String[] get(Event e) { return new String[]{oldContent}; }
    @Override
    public Class<? extends String> getReturnType() { return String.class; }
    @Override
    public boolean isSingle() { return true; }
    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the old message content";
    }
}
