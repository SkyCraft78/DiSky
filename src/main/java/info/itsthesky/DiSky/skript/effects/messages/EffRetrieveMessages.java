package info.itsthesky.disky.skript.effects.messages;

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
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Name("Retrieve Messages")
@Description("Retrieve a specific amount of message in a text channel, and store them in a list variable.")
@Examples("retrieve last 100 messages from event-channel and store them in {_msg::*}")
@Since("2.0")
public class EffRetrieveMessages extends Effect {

    static {
        Skript.registerEffect(EffRetrieveMessages.class,
                "["+ Utils.getPrefixName() +"] retrieve [the] [last] %number% (messages|msg) from [the] [channel] %channel/textchannel% and store (them|the messages) in %-objects%");
    }

    private Expression<Number> exprAmount;
    private Expression<GuildChannel> exprChannel;
    private Variable<?> variable;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        Utils.setHasDelayBefore(Kleenean.TRUE);
        exprAmount = (Expression<Number>) exprs[0];
        exprChannel = (Expression<GuildChannel>) exprs[1];
        Expression<?> var = exprs[2];
        if (var != null && !(var instanceof Variable)) {
            Skript.error("Cannot store the message in a non-variable expression");
            return false;
        } else {
            variable = (Variable<?>) var;
            if (!variable.isList()) {
                Skript.error("Cannot store a list of messages into a non-list variable!");
                return false;
            }
        }
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(Event e) {
        Number amount = exprAmount.getSingle(e);
        GuildChannel channel = exprChannel.getSingle(e);
        if (amount == null || channel == null) return getNext();
        if (!channel.getType().equals(ChannelType.TEXT)) return getNext();
        debug(e, true);

        Object localVars = Variables.removeLocals(e); // Back up local variables
        Delay.addDelayedEvent(e); // Mark this event as delayed

        if (!Skript.getInstance().isEnabled()) // See https://github.com/SkriptLang/Skript/issues/3702
            return null;

        ((TextChannel) channel).getHistory().retrievePast(amount.intValue()).queue(
                msg -> runConsumer(msg, e, localVars),
                ex -> runConsumer(null, e, localVars)
        );

        return null;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "retrieve last " + exprAmount.toString(e, debug) + " messages from channel "+ exprChannel.toString(e, debug) +" and store them in " + variable.toString(e, debug);
    }

    private void runConsumer(@Nullable final List<Message> message, final Event e, final Object localVars) {
        if (localVars != null)
            Variables.setLocalVariables(e, localVars);

        if (variable != null && message != null) {
            variable.change(e, UpdatingMessage.convert(message.toArray(new Message[0])), Changer.ChangeMode.SET);
        }

        if (getNext() != null) {
            Object vars = Variables.removeLocals(e); // Back up local variables
            Bukkit.getScheduler().runTask(Skript.getInstance(), () -> { // Walk to next item synchronously
                Object timing = null;
                if (SkriptTimings.enabled()) { // getTrigger call is not free, do it only if we must
                    Trigger trigger = getTrigger();
                    if (trigger != null) {
                        timing = SkriptTimings.start(trigger.getDebugLabel());
                    }
                }
                Variables.setLocalVariables(e, vars);
                TriggerItem.walk(getNext(), e);
                Variables.removeLocals(e); // Clean up local vars, we may be exiting now
                SkriptTimings.stop(timing); // Stop timing if it was even started
            });
        } else {
            Variables.removeLocals(e);
        }
    }

    @Override
    protected void execute(Event e) { }
}
