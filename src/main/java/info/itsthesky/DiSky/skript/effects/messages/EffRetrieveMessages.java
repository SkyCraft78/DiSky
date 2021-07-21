package info.itsthesky.disky.skript.effects.messages;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.async.MultipleWaiterEffect;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.event.Event;

import java.util.ArrayList;

@Name("Retrieve Messages")
@Description("Retrieve a specific amount of message in a text channel, and store them in a list variable.")
@Examples("retrieve last 100 messages from event-channel and store them in {_msg::*}")
@Since("2.0")
public class EffRetrieveMessages extends MultipleWaiterEffect<UpdatingMessage> {

    static {
        Skript.registerEffect(EffRetrieveMessages.class,
                "["+ Utils.getPrefixName() +"] retrieve [the] [last] %number% (messages|msg) from [the] [channel] %channel/textchannel% and store (them|the messages) in %-objects%");
    }

    private Expression<Number> exprAmount;
    private Expression<GuildChannel> exprChannel;

    @SuppressWarnings("unchecked")
    @Override
    public boolean initEffect(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprAmount = (Expression<Number>) exprs[0];
        exprChannel = (Expression<GuildChannel>) exprs[1];
        Expression<?> var = exprs[2];
        if (var != null && !(var instanceof Variable)) {
            Skript.error("Cannot store the message in a non-variable expression");
            return false;
        } else {
            setChangedVariable((Variable) var);
            if (!changedVariable.isList()) {
                Skript.error("Cannot store a list of messages into a non-list variable!");
                return false;
            }
        }
        return true;
    }

    @Override
    public void runEffect(Event e) {
        Number amount = exprAmount.getSingle(e);
        GuildChannel channel = exprChannel.getSingle(e);
        if (amount == null || channel == null) return;
        if (!channel.getType().equals(ChannelType.TEXT)) return;

        Utils.handleRestAction(
                ((TextChannel) channel).getHistory().retrievePast(amount.intValue()),
                msg -> restart(UpdatingMessage.convert(msg.toArray(new Message[0]))),
                new ArrayList<>()
        );
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "retrieve last " + exprAmount.toString(e, debug) + " messages from channel "+ exprChannel.toString(e, debug) +" and store them in " + changedVariable.toString(e, debug);
    }

}
