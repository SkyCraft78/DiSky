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
import info.itsthesky.disky.tools.StaticData;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.ActionRow;
import org.bukkit.event.Event;

@Name("Clear Message Buttons")
@Description("Clear every buttons of a specific message.")
@Examples("clear buttons from event-message")
@Since("1.12")
public class EffClearButtons extends Effect {

    static {
        Skript.registerEffect(EffClearButtons.class,
                "["+ Utils.getPrefixName() +"] (clear|remove|delete) [all] buttons from %message%");
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
            message.editMessage(new MessageBuilder(message).build())
                    .setActionRows(new ActionRow[0])
                    .queue(null, DiSkyErrorHandler::logException);
            StaticData.actions.remove(message.getIdLong());
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "clear all buttons from message " + exprMessage.toString(e, debug);
    }

}
