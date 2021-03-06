package info.itsthesky.disky.skript.scope.invites;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.async.WaiterEffect;
import info.itsthesky.disky.tools.object.InviteBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.event.Event;

@Name("Create Invite builder in Channel")
@Description("Create the invite builder in a specific text channel, and optionally get it.")
@Examples("create invite builder in event-channel\ncreate invite builder in event-channel and store it in {_voice}")
@Since("1.7")
public class EffCreateInvite extends WaiterEffect<Invite> {

    static {
        Skript.registerEffect(EffCreateInvite.class,
                "["+ Utils.getPrefixName() +"] create [the] [invite] [builder] %invitebuilder% in [the] [channel] %channel/textchannel% [and store it in %-object%]");
    }

    private Expression<InviteBuilder> exprBuilder;
    private Expression<GuildChannel> exprChannel;

    @SuppressWarnings("unchecked")
    @Override
    public boolean initEffect(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprBuilder = (Expression<InviteBuilder>) exprs[0];
        exprChannel = (Expression<GuildChannel>) exprs[1];
        setChangedVariable((Variable<Invite>) exprs[2]);
        return true;
    }

    @Override
    public void runEffect(Event e) {
        InviteBuilder builder = exprBuilder.getSingle(e);
        GuildChannel channel = exprChannel.getSingle(e);
        if (builder == null || channel == null) return;
        if (!channel.getType().equals(ChannelType.TEXT)) return;
        Utils.handleRestAction(
                builder.build((TextChannel) channel),
                this::restart,
                null
        );
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "create invite using builder " + exprBuilder.toString(e, debug) + " in channel " + exprChannel.toString(e, debug);
    }

}
