package info.itsthesky.disky.skript.effects.messages;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.async.WaiterEffect;
import info.itsthesky.disky.tools.events.InteractionEvent;
import info.itsthesky.disky.tools.events.MessageEvent;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;
import org.bukkit.event.Event;

import java.util.Arrays;

@Name("Reply with Message")
@Description("Reply with a message to channel-based events (work with private message too!). You can get the sent message using 'and store it in {_var}' pattern! Personal message only works with INTERACTION (slash commands / webhooks) and will make the message only visible for the user who done the interaction.")
@Examples({"reply with \"Hello World :globe_with_meridians:\"",
        "reply with personal message \"Only you can see that message :eyes:\"",
        "reply with \"Hello World !\" and store it in {_msg}"})
@Since("1.0")
public class EffReplyWith extends WaiterEffect<UpdatingMessage> {

    static {
        Skript.registerEffect(EffReplyWith.class,
                "["+ Utils.getPrefixName() +"] reply with [(personal|hidden)] [the] [message] %string/message/messagebuilder/embed% [with [(component|row)[s]] %-buttonrows/selectbuilder%] [and store it in %-object%]");
    }

    private Expression<Object> exprMessage;
    private Expression<Object> exprComponents;
    private boolean ephemeral = false;

    @SuppressWarnings("unchecked")
    @Override
    public boolean initEffect(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprMessage = (Expression<Object>) exprs[0];
        exprComponents = (Expression<Object>) exprs[1];
        Expression<?> var = exprs[2];
        if (var != null && !(var instanceof Variable)) {
            Skript.error("Cannot store the message in a non-variable expression");
            return false;
        } else {
            setChangedVariable((Variable<UpdatingMessage>) var);
        }
        ephemeral = parseResult.expr.contains("reply with personal") || parseResult.expr.contains("reply with hidden");

        if (
                !(Arrays.asList(ScriptLoader.getCurrentEvents()[0].getInterfaces()).contains(MessageEvent.class)) &&
                        !(Arrays.asList(ScriptLoader.getCurrentEvents()[0].getInterfaces()).contains(InteractionEvent.class))
        ) {
            Skript.error("The reply effect cannot be used in a non channel-related event!");
            return false;
        }

        return true;
    }

    @Override
    public void runEffect(Event event) {
        Object content = exprMessage.getSingle(event);
        Object[] components = Utils.verifyVars(event, exprComponents);
        if (content == null) return;

        MessageBuilder toSend;
        switch (content.getClass().getSimpleName()) {
            case "EmbedBuilder":
                toSend = new MessageBuilder().setEmbeds(((EmbedBuilder) content).build());
                break;
            case "String":
                toSend = new MessageBuilder(content.toString());
                break;
            case "MessageBuilder":
                toSend = (MessageBuilder) content;
                break;
            case "Message":
                toSend = new MessageBuilder((Message) content);
                break;
            default:
                Skript.error("[DiSky] Cannot parse or cast the message in the send effect!");
                return;
        }

        if (event instanceof InteractionEvent) {
            GenericInteractionCreateEvent ev = ((InteractionEvent) event).getInteractionEvent();
            ReplyAction action = ev.reply(toSend.build());
            action = Utils.parseComponents(action, components);
            action.setEphemeral(ephemeral).queue();
            return;
        }

        MessageChannel channel = null;
        if (event instanceof MessageEvent)
            channel = ((MessageEvent) event).getMessageChannel();

        if (channel == null) return;
        MessageAction action = channel
                .sendMessage(toSend.build());
        action = Utils.parseComponents(action, components);

        Utils.handleRestAction(
                action,
                msg -> restart(msg == null ? null : UpdatingMessage.from(msg)),
                null
        );
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "reply with message " + exprMessage.toString(e, debug) + changedVariable != null ? " and store it in " + changedVariable.toString(e, debug) : "";
    }
}