package info.itsthesky.disky.skript.effects.messages;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.effects.Delay;
import ch.njol.skript.lang.*;
import ch.njol.skript.timings.SkriptTimings;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.tools.events.InteractionEvent;
import info.itsthesky.disky.skript.expressions.messages.ExprLastMessage;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.events.LogEvent;
import info.itsthesky.disky.tools.events.MessageEvent;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.ButtonBuilder;
import info.itsthesky.disky.tools.object.ButtonRow;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Name("Reply with Message")
@Description("Reply with a message to channel-based events (work with private message too!). You can get the sent message using 'and store it in {_var}' pattern! Personal message only works with INTERACTION (slash commands / webhooks) and will make the message only visible for the user who done the interaction.")
@Examples({"reply with \"Hello World :globe_with_meridians:\"",
        "reply with personal message \"Only you can see that message :eyes:\"",
        "reply with \"Hello World !\" and store it in {_msg}"})
@Since("1.0")
public class EffReplyWith extends Effect {

    static {
        Skript.registerEffect(EffReplyWith.class,
                "["+ Utils.getPrefixName() +"] reply with [(personal|hidden)] [the] [message] %string/message/messagebuilder/embed% [with [(component|row)[s]] %-buttonrows/selectbuilder%] [and store it in %-object%]");
    }

    private Expression<Object> exprMessage;
    private Expression<Object> exprComponents;
    private boolean ephemeral = false;
    private Variable<?> variable;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprMessage = (Expression<Object>) exprs[0];
        exprComponents = (Expression<Object>) exprs[1];
        Expression<?> var = exprs[2];
        if (var != null && !(var instanceof Variable)) {
            Skript.error("Cannot store the message in a non-variable expression");
            return false;
        } else {
            variable = (Variable<?>) var;
        }
        ephemeral = parseResult.expr.contains("reply with personal") || parseResult.expr.contains("reply with hidden");

        Utils.setHasDelayBefore(Kleenean.TRUE);

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
    protected @Nullable TriggerItem walk(Event e) {
        Object content = exprMessage.getSingle(e);
        Object[] components = Utils.verifyVars(e, exprComponents);
        if (content == null) return null;
        debug(e, true);

        Delay.addDelayedEvent(e); // Mark this event as delayed
        Object _localVars = null;
        if (DiSky.SkriptUtils.MANAGE_LOCALES)
            _localVars = Variables.removeLocals(e); // Back up local variables
        Object localVars = _localVars;

        if (!Skript.getInstance().isEnabled()) // See https://github.com/SkriptLang/Skript/issues/3702
            return null;

        DiSkyErrorHandler.executeHandleCode(e, event -> {
            /* Message cast */
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
            if (e instanceof MessageEvent)
                channel = ((MessageEvent) e).getMessageChannel();

            if (channel == null) return;
            MessageAction action = channel
                    .sendMessage(toSend.build());
            action = Utils.parseComponents(action, components);
            action.queue(m -> {
                // Re-set local variables
                if (DiSky.SkriptUtils.MANAGE_LOCALES && localVars != null)
                    Variables.setLocalVariables(event, localVars);

                ExprLastMessage.lastMessage = UpdatingMessage.from(m);
                if (variable != null) {
                    variable.change(event, new Object[] {UpdatingMessage.from(m)}, Changer.ChangeMode.SET);
                }

                if (getNext() != null) {
                    Bukkit.getScheduler().runTask(Skript.getInstance(), () -> { // Walk to next item synchronously
                        Object timing = null;
                        if (SkriptTimings.enabled()) { // getTrigger call is not free, do it only if we must
                            Trigger trigger = getTrigger();
                            if (trigger != null) {
                                timing = SkriptTimings.start(trigger.getDebugLabel());
                            }
                        }

                        TriggerItem.walk(getNext(), event);

                        if (DiSky.SkriptUtils.MANAGE_LOCALES)
                            Variables.removeLocals(event); // Clean up local vars, we may be exiting now

                        SkriptTimings.stop(timing); // Stop timing if it was even started
                    });
                } else {
                    if (DiSky.SkriptUtils.MANAGE_LOCALES)
                        Variables.removeLocals(event);
                }
            });
        });
        return getNext();
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "reply with message " + exprMessage.toString(e, debug) + variable != null ? " and store it in " + variable.toString(e, debug) : "";
    }

    @Override
    protected void execute(Event e) { }
}