package info.itsthesky.disky.skript.effects.messages;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.entities.Message;
import org.bukkit.event.Event;

@Name("Clear Message Reactions")
@Description("Clear every reactions of a specific message.")
@Examples("clear reactions from event-message")
@Since("1.10")
public class EffClearReactions extends Effect {

    static {
        Skript.registerEffect(EffClearReactions.class,
                "["+ Utils.getPrefixName() +"] (clear|remove|delete) [all] reactions from %message%");
    }

    private Expression<Message> exprMessage;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.exprMessage = (Expression<Message>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            Message message = exprMessage.getSingle(e);
            if (message == null) return;
            message.clearReactions().queue(null, DiSkyErrorHandler::logException);
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "clear all reactions from message " + exprMessage.toString(e, debug);
    }

}
