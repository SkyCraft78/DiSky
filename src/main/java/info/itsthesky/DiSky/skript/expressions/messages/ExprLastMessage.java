package info.itsthesky.disky.skript.expressions.messages;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.Message;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Last Message")
@Description("This expression returns the last message sent by the reply or send effect.")
@Since("1.2")
public class ExprLastMessage extends SimpleExpression<UpdatingMessage> {

    public static UpdatingMessage lastMessage;

    static {
        Skript.registerExpression(ExprLastMessage.class, UpdatingMessage.class, ExpressionType.SIMPLE,
                "[the] last [sent] message"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Nullable
    @Override
    protected UpdatingMessage[] get(Event e) {
        return new UpdatingMessage[]{this.lastMessage};
    }

    @Override
    public Class<? extends UpdatingMessage> getReturnType() {
        return UpdatingMessage.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the last send message";
    }
}
