package info.itsthesky.disky.skript.scope.messagebuilder;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.MessageBuilder;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Last Message Scope Builder")
@Description("Return the last used message builder.")
@Since("2.0")
public class ExprLastMessageBuilder extends SimpleExpression<MessageBuilder> {

    static {
        Skript.registerExpression(ExprLastMessageBuilder.class, MessageBuilder.class, ExpressionType.SIMPLE,
                "[the] [last] [(generated|created)] (message|msg) builder"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Nullable
    @Override
    protected MessageBuilder[] get(Event e) {
        return new MessageBuilder[]{ScopeMessageBuilder.lastBuilder};
    }

    @Override
    public Class<? extends MessageBuilder> getReturnType() {
        return MessageBuilder.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the last message builder";
    }
}
