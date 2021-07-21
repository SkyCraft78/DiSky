package info.itsthesky.disky.skript.component;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.async.AsyncEffect;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.StaticData;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import org.bukkit.event.Event;

import java.util.Collections;

@Name("Clear Message Buttons")
@Description("Clear every buttons of a specific message.")
@Examples("clear buttons from event-message")
@Since("1.12")
public class EffClearButtons extends AsyncEffect {

    static {
        Skript.registerEffect(EffClearButtons.class,
                "["+ Utils.getPrefixName() +"] (clear|remove|delete) [all] buttons from [the] [message] %message%");
    }

    private Expression<UpdatingMessage> exprMessage;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.exprMessage = (Expression<UpdatingMessage>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            UpdatingMessage message = exprMessage.getSingle(e);
            if (message == null) return;
            message.getMessage().editMessage(message.getMessage())
                    .setActionRows(Collections.emptyList())
                    .queue(null, DiSkyErrorHandler::logException);
            UpdatingMessage.put(message.getID(), message.getMessage());
            StaticData.actionRows.remove(message.getMessage().getIdLong());
            if (!(exprMessage instanceof Variable)) return;
            Utils.setSkriptVariable((Variable<?>) exprMessage, message, e);
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "clear all buttons from message " + exprMessage.toString(e, debug);
    }

}
