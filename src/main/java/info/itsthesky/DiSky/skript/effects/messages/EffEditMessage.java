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
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.ActionRow;
import net.dv8tion.jda.api.interactions.Component;
import net.dv8tion.jda.api.interactions.button.Button;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

@Name("Edit Message")
@Description("Edit any message from the bot with new message or embed.")
@Examples("reply with \":v: Custom message ...\" and store it in {_msg}\n" +
        "wait 3 second\n" +
        "edit message {_msg} to show \":x: ... has been edited\"")
@Since("1.2")
public class EffEditMessage extends Effect {

    static {
        Skript.registerEffect(EffEditMessage.class,
                "["+ Utils.getPrefixName() +"] edit [discord] [message] %message% (with|to show) [new (embed|string)] %embed/string/messagebuilder%");
    }

    private Expression<Message> exprMessage;
    private Expression<Object> exprNew;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprMessage = (Expression<Message>) exprs[0];
        exprNew = (Expression<Object>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            Message message = exprMessage.getSingle(e);
            Object newValue = exprNew.getSingle(e);
            if (message == null || newValue == null) return;

            List<Button> current = StaticData.actions.containsKey(message.getIdLong()) ? StaticData.actions.get(message.getIdLong()) : new ArrayList<>();
            
            if (newValue instanceof EmbedBuilder) {
                message.editMessage(((EmbedBuilder) newValue).build())
                        .setActionRows(ActionRow.of(current.toArray(new Component[0])))
                        .queue();
            } else if (newValue instanceof MessageBuilder) {
                message.editMessage(((MessageBuilder) newValue).build())
                        .setActionRows(ActionRow.of(current.toArray(new Component[0])))
                        .queue(null, DiSkyErrorHandler::logException);
            } else {
                message.editMessage(newValue.toString())
                        .setActionRows(ActionRow.of(current.toArray(new Component[0])))
                        .queue(null, DiSkyErrorHandler::logException);
            }
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "edit message " + exprMessage.toString(e, debug) + " with " + exprNew.toString(e, debug);
    }

}
