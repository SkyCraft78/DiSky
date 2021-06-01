package info.itsthesky.disky.skript.effects.messages;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky.tools.AsyncEffect;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.Message;
import org.bukkit.event.Event;

@Name("Clear Message Reactions")
@Description("Clear every reactions of a specific message.")
@Examples("remove all reactions from event-message")
@Since("1.10")
public class EffClearReactions extends AsyncEffect {

    static {
        Skript.registerEffect(EffClearReactions.class,
                "["+ Utils.getPrefixName() +"] (clear|remove|delete) [all] reactions from %message%");
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
            message.getMessage().clearReactions().queue(null, DiSkyErrorHandler::logException);
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "clear all reactions from message " + exprMessage.toString(e, debug);
    }

}
