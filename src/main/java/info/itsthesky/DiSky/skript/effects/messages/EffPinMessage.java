package info.itsthesky.DiSky.skript.effects.messages;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.DiSky.tools.DiSkyErrorHandler;
import info.itsthesky.DiSky.tools.Utils;
import net.dv8tion.jda.api.entities.Message;
import org.bukkit.event.Event;

@Name("Pin / Unpin Message")
@Description("Pin or unpin any message from a channel.")
@Examples("pin event-message")
@Since("1.6")
public class EffPinMessage extends Effect {

    static {
        Skript.registerEffect(EffPinMessage.class,
                "["+ Utils.getPrefixName() +"] pin [discord] [message] %message% [in channel]",
                "["+ Utils.getPrefixName() +"] unpin [discord] [message] %message% [in channel]"
        );
    }

    private Expression<Message> exprMessage;
    private int pattern;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprMessage = (Expression<Message>) exprs[0];
        pattern = matchedPattern;
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            Message message = exprMessage.getSingle(e);
            if (message == null) return;
            if (pattern == 0) {
                message.pin().queue(null, DiSkyErrorHandler::logException);
            } else {
                message.unpin().queue(null, DiSkyErrorHandler::logException);
            }
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "pin or unpin message " + exprMessage.toString(e, debug);
    }

}
