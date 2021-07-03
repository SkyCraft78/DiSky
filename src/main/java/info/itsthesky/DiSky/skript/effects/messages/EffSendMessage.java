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
import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.skript.expressions.messages.ExprLastMessage;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.ButtonBuilder;
import info.itsthesky.disky.tools.object.ButtonRow;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("Send discord Message")
@Description("Send a message in a specific channel, with a specific bot. Use that syntax only for non-textchannel event.")
@Examples("on load:\n" +
        "\tmake embed:\n" +
        "\t\tset title of embed to \"The bot has been started!\"\n" +
        "\t\tset color of embed to green\n" +
        "\t\tset timestamp of embed to now\n" +
        "\tsend last embed to text channel with id \"818182473502294066\"")
@Since("1.0")
public class EffSendMessage extends Effect {

    static {
        Skript.registerEffect(EffSendMessage.class,
                "["+ Utils.getPrefixName() +"] send [message] %string/message/embed/messagebuilder% to [the] [(user|channel)] %user/member/textchannel/channel% [with [(component|row)[s]] %-buttonrows/selectbuilder%] [with [the] %-bot%] [and store it in %-object%]");
    }

    private Expression<Object> exprMessage;
    private Expression<Object> exprChannel;
    private Expression<Object> exprComponents;
    private Variable<?> variable;
    private Expression<JDA> exprBot;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        Utils.setHasDelayBefore(Kleenean.TRUE);
        exprMessage = (Expression<Object>) exprs[0];
        exprChannel = (Expression<Object>) exprs[1];
        exprComponents = (Expression<Object>) exprs[2];
        exprBot = (Expression<JDA>) exprs[3];
        Expression<?> var = exprs[4];
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
        Object entity = exprChannel.getSingle(e);
        Object content = exprMessage.getSingle(e);
        Object component = Utils.verifyVars(e, exprComponents);
        JDA bot = Utils.verifyVar(e, exprBot);
        if (entity == null || content == null) return null;
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
            MessageBuilder toSend = null;
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
            }
            if (toSend == null) {
                Skript.error("[DiSky] Cannot parse or cast the message in the send effect!");
                return;
            }

            /* Channel Cast */
            MessageChannel channel = null;
            switch (entity.getClass().getSimpleName()) {
                case "TextChannel":
                case "TextChannelImpl":
                    channel = (MessageChannel) entity;
                    break;
                case "GuildChannel":
                case "GuildChannelImpl":
                    channel = ((GuildChannel) entity).getType().equals(ChannelType.TEXT) ? (MessageChannel) entity : null;
                    break;
                case "User":
                case "UserImpl":
                    channel = ((User) entity).openPrivateChannel().complete();
                    break;
                case "Member":
                case "MemberImpl":
                    channel = ((Member) entity).getUser().openPrivateChannel().complete();
                    break;
            }
            if (channel == null) {
                Skript.error("[DiSky] Cannot parse or cast the message channel in the send effect!");
                return;
            }

            toSend = new MessageBuilder("test");
            //toSend = new MessageBuilder().setEmbeds(new EmbedBuilder().setTitle("test").build());

            if (bot != null)
                channel = bot.getTextChannelById(channel.getId());

            MessageAction action = channel.sendMessage(toSend.build());
            action = Utils.parseComponents(action, component);
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
        return null;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "send discord message " + exprMessage.toString(e, debug) + " to channel or user " + exprChannel.toString(e, debug);
    }

    @Override
    protected void execute(Event event) { }
}
