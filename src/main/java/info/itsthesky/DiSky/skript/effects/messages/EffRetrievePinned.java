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
import info.itsthesky.disky.tools.WaiterEffect;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Name("Pinned Messages")
@Description("Get all pinned message of a specific text channel.")
@Examples("set {_msg::*} to pinned message of event-channel")
@Since("1.0")
public class EffRetrievePinned extends WaiterEffect {

    static {
        Skript.registerEffect(EffRetrievePinned.class,
                "["+ Utils.getPrefixName() +"] retrieve [the] pin[ned] (messages|msg) from [the] [channel] %channel/textchannel% and store (them|the messages) in %-objects%");
    }

    private Expression<GuildChannel> exprChannel;
    private Variable<?> variable;

    @SuppressWarnings("unchecked")
    @Override
    public boolean initEffect(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprChannel = (Expression<GuildChannel>) exprs[0];
        Expression<?> var = exprs[1];
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
    public void runEffect(Event e) {
        GuildChannel channel = exprChannel.getSingle(e);
        if (channel == null) return;
        if (!channel.getType().equals(ChannelType.TEXT)) return;


        Utils.handleRestAction(
                ((TextChannel) channel).retrievePinnedMessages(),
                msg -> {
                    if (variable != null)
                        Utils.setSkriptList(variable, e, Arrays.asList(UpdatingMessage.convert(msg.toArray(new Message[0]))));
                    restart(); // We change the next trigger item and resume the trigger execution
                },
                new ArrayList<>()
        );
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "retrieve pinned messages from channel "+ exprChannel.toString(e, debug) +" and store them in " + variable.toString(e, debug);
    }
}
