package info.itsthesky.DiSky.skript.expressions.messages;

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
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.Emote;
import org.bukkit.event.Event;

@Name("Used Emotes")
@Description("Return all emotes used in a message.")
@Examples("set {_users::*} to used emotes of event-message")
@Since("1.13.1")
public class ExprMessageEmotes extends SimpleExpression<Emote> {

    static {
        Skript.registerExpression(ExprMessageEmotes.class, Emote.class, ExpressionType.SIMPLE,
                "["+ Utils.getPrefixName() +"] [the] (emotes|emojis) (in|of) [the] [message] %message%"
        );
    }

    private Expression<UpdatingMessage> exprMessage;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprMessage = (Expression<UpdatingMessage>) exprs[0];
        return true;
    }

    @Override
    protected Emote[] get(Event e) {
        UpdatingMessage message = exprMessage.getSingle(e);
        if (message == null) return new Emote[0];
        return message.getMessage().getEmotes().toArray(new Emote[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends Emote> getReturnType() {
        return Emote.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "used emotes of message " + exprMessage.toString(e, debug);
    }

}