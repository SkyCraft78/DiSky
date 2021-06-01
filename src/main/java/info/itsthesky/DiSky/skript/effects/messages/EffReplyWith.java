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
import info.itsthesky.disky.skript.commands.CommandEvent;
import info.itsthesky.disky.skript.events.skript.messages.EventMessageReceive;
import info.itsthesky.disky.skript.events.skript.messages.EventPrivateMessage;
import info.itsthesky.disky.skript.expressions.messages.ExprLastMessage;
import info.itsthesky.disky.tools.AsyncEffect;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Name("Reply with Message")
@Description("Reply with a message to channel-based events (work with private message too!). You can get the sent message using 'and store it in {_var}' pattern!")
@Examples("reply with \"Hello World :globe_with_meridians:\"")
@Since("1.0")
public class EffReplyWith extends Effect {

    static {
        Skript.registerEffect(EffReplyWith.class,
                "["+ Utils.getPrefixName() +"] reply with [the] [message] %string/message/messagebuilder/embed% [and store it in %-object%]");
    }

    private Expression<Object> exprMessage;
    private Variable<?> variable;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprMessage = (Expression<Object>) exprs[0];

        Utils.setHasDelayBefore(Kleenean.TRUE);

        if (!ScriptLoader.isCurrentEvent(
                EventMessageReceive.class,
                CommandEvent.class,
                EventPrivateMessage.class
        )) {
            Skript.error("The reply effect cannot be used in a " + ScriptLoader.getCurrentEventName() + " event.");
            return false;
        }

        Expression<?> var = exprs[1];
        if (var != null && !(var instanceof Variable)) {
            Skript.error("Cannot store the message in a non-variable expression");
            return false;
        } else {
            variable = (Variable<?>) var;
        }


        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(Event e) {
        Object content = exprMessage.getSingle(e);
        if (content == null) return null;
        debug(e, true);

        Delay.addDelayedEvent(e); // Mark this event as delayed
        Object localVars = Variables.removeLocals(e); // Back up local variables

        if (!Skript.getInstance().isEnabled()) // See https://github.com/SkriptLang/Skript/issues/3702
            return null;

        DiSkyErrorHandler.executeHandleCode(e, event -> {
            /* Message cast */
            MessageBuilder toSend;
            switch (content.getClass().getSimpleName()) {
                case "EmbedBuilder":
                    toSend = new MessageBuilder().setEmbed(((EmbedBuilder) content).build());
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

            // As I said on Discord, it'd be better to have an interface for this stuff so you can cast.
            MessageChannel lastChannel;
            try {
                Object classEvent = event.getClass().getDeclaredMethod("getEvent").invoke(event);
                lastChannel = (MessageChannel) classEvent.getClass().getDeclaredMethod("getChannel").invoke(classEvent);
            } catch (Exception reflect) {
                try {
                    lastChannel = (MessageChannel) event.getClass().getDeclaredMethod("getChannel").invoke(event);
                } catch (Exception reflect2) {
                    reflect2.printStackTrace();
                    DiSky.getInstance().getConsoleLogger().severe("Cannot get the last channel from a message event !");
                    return;
                }
            }


            lastChannel.sendMessage(toSend.build()).queue(m -> {
                // Re-set local variables
                if (localVars != null)
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

                        Variables.removeLocals(event); // Clean up local vars, we may be exiting now

                        SkriptTimings.stop(timing); // Stop timing if it was even started
                    });
                } else {
                    Variables.removeLocals(event);
                }
            });
        });
        return null;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "reply with message " + exprMessage.toString(e, debug) + variable != null ? " and store it in " + variable.toString(e, debug) : "";
    }

    @Override
    protected void execute(Event e) { }
}