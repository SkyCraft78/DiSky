package info.itsthesky.disky.skript.effects.messages;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.AsyncEffect;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.StaticData;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

@Name("Edit Message")
@Description("Edit any message from the bot with new message or embed.")
@Examples("reply with \":v: Message ...\" and store it in {_msg}\n" +
        "wait 3 second\n" +
        "edit message {_msg} to show \":x: ... has been edited\"")
@Since("1.2")
public class EffEditMessage extends AsyncEffect {

    static {
        Skript.registerEffect(EffEditMessage.class,
                "["+ Utils.getPrefixName() +"] edit [discord] [message] %message% (with|to show) [new (embed|string)] %embed/string/messagebuilder% [(keeping buttons|and keep buttons)]");
    }

    private Expression<UpdatingMessage> exprMessage;
    private Expression<Object> exprNew;
    private boolean keepButtons;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprMessage = (Expression<UpdatingMessage>) exprs[0];
        exprNew = (Expression<Object>) exprs[1];
        keepButtons = parseResult.expr.contains("keeping buttons") || parseResult.expr.contains("and keep buttons");
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            UpdatingMessage message = exprMessage.getSingle(e);
            Object newValue = exprNew.getSingle(e);
            if (message == null || newValue == null) return;

            MessageAction action;
            MessageBuilder toSend = null;
            /* Message cast */
            switch (newValue.getClass().getSimpleName()) {
                case "EmbedBuilder":
                    toSend = new MessageBuilder().setEmbed(((EmbedBuilder) newValue).build());
                    break;
                case "String":
                    toSend = new MessageBuilder(newValue.toString());
                    break;
                case "MessageBuilder":
                    toSend = (MessageBuilder) newValue;
                    break;
                case "Message":
                    toSend = new MessageBuilder((Message) newValue);
                    break;
            }
            if (toSend == null) {
                Skript.error("[DiSky] Cannot parse or cast the message in the edit message effect!");
                return;
            }
            if (keepButtons) {
                List<ActionRow> rows = new ArrayList<>(StaticData.actionRows.get(message.getMessage().getIdLong()) == null ? new ArrayList<>() : StaticData.actionRows.get(message.getMessage().getIdLong()));
                message.getMessage().editMessage(toSend.build()).setActionRows(rows).queue();
            } else {
                message.getMessage().editMessage(toSend.build()).queue();
            }
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "edit message " + exprMessage.toString(e, debug) + " with " + exprNew.toString(e, debug);
    }

}
