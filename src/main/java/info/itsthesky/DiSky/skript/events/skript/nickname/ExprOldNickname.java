package info.itsthesky.disky.skript.events.skript.nickname;

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

@Name("Old Nickname")
@Description("Get the old nickname of a Member nickname change event")
@Since("1.3")
public class ExprOldNickname extends SimpleExpression<String> {

    public static String oldNickname;

    static {
        Skript.registerExpression(ExprOldNickname.class, String.class, ExpressionType.SIMPLE,
                "[the] old nick[name]"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Nullable
    @Override
    protected String[] get(Event e) {
        return new String[]{oldNickname};
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the old nickname";
    }
}
