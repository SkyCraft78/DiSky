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
import info.itsthesky.disky.tools.object.ButtonBuilder;
import info.itsthesky.disky.tools.object.Emote;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.ActionRow;
import net.dv8tion.jda.api.interactions.Component;
import net.dv8tion.jda.api.interactions.button.Button;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Name("Add Buttons")
@Description("Add a specific button, on message.")
@Examples("add last button to event-message")
@Since("1.12")
public class EffAddButtons extends Effect {

    static {
        Skript.registerEffect(EffAddButtons.class,
                "["+ Utils.getPrefixName() +"] (add|append) [button] %buttons% to [(message|buttons of)] %message%");
    }

    private Expression<ButtonBuilder> exprButton;
    private Expression<Message> exprMessage;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.exprButton = (Expression<ButtonBuilder>) exprs[0];
        this.exprMessage = (Expression<Message>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            Message message = exprMessage.getSingle(e);
            ButtonBuilder[] buttons = exprButton.getAll(e);
            if (message == null || buttons.length == 0) return;

            List<Button> current = StaticData.actions.containsKey(message.getIdLong()) ? StaticData.actions.get(message.getIdLong()) : new ArrayList<>();

            for (ButtonBuilder builder : buttons) {
                if (builder.build() == null) continue;
                current.add(builder.build());
            }
            StaticData.actions.put(message.getIdLong(), current);
            message
                    .editMessage(new MessageBuilder(message).build())
                    .setActionRows(ActionRow.of(current.toArray(new Component[0])))
                    .queue(null, DiSkyErrorHandler::logException);
            
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "add buttons " + exprButton.toString(e, debug) + " to message " + exprMessage.toString(e, debug);
    }

}
