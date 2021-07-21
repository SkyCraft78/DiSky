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

@Name("Pinned Messages")
@Description("Get all pinned message of a specific text channel.")
@Examples("set {_msg::*} to pinned message of event-channel")
@Since("1.0")
public class EffRetrievePinned extends MultipleWaiterEffect<UpdatingMessage> {

    static {
        Skript.registerEffect(EffRetrievePinned.class,
                "["+ Utils.getPrefixName() +"] retrieve [the] pin[ned] (messages|msg) from [the] [channel] %channel/textchannel% and store (them|the messages) in %-objects%");
    }

    private Expression<GuildChannel> exprChannel;

    @SuppressWarnings("unchecked")
    @Override
    public boolean initEffect(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprChannel = (Expression<GuildChannel>) exprs[0];
        Expression<?> var = exprs[1];
        if (var != null && !(var instanceof Variable)) {
            Skript.error("Cannot store the message in a non-variable expression");
            return false;
        } else {
            setChangedVariable((Variable<UpdatingMessage>) var);
            if (!changedVariable.isList()) {
                Skript.error("Cannot store a list of messages into a non-list variable!");
                return false;
            }
        }
        return true;
    }

    @Override
    public void runEffect(Event e) {
        GuildChannel channel = exprChannel.getSingle(e);
        if (channel == null) return;
        if (!channel.getType().equals(ChannelType.TEXT)) return;


        Utils.handleRestAction(
                ((TextChannel) channel).retrievePinnedMessages(),
                msg -> restart(UpdatingMessage.convert(msg.toArray(new Message[0]))),
                new ArrayList<>()
        );
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "retrieve pinned messages from channel "+ exprChannel.toString(e, debug) +" and store them in " + changedVariable.toString(e, debug);
    }
}
