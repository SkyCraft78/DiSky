package info.itsthesky.disky.skript.scope.textchannels;

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
import info.itsthesky.disky.tools.WaiterEffect;
import info.itsthesky.disky.tools.object.TextChannelBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.event.Event;

@Name("Create text channel builder in Guild")
@Description("Create the channel builder in a specific guild, and optionally get the text channel created.")
@Examples("create channel builder in event-guild\ncreate channel in event-guild and store it in {_channel}")
@Since("1.0")
public class EffCreateTextChannel extends WaiterEffect {

    static {
        Skript.registerEffect(EffCreateTextChannel.class,
                "["+ Utils.getPrefixName() +"] create [the] [textchannel] [builder] %textchannelbuilder% in [the] [guild] %guild% [and store it in %-object%]");
    }

    private Expression<TextChannelBuilder> exprBuilder;
    private Expression<Guild> exprGuild;
    private Variable<?> var;

    @SuppressWarnings("unchecked")
    @Override
    public boolean initEffect(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprBuilder = (Expression<TextChannelBuilder>) exprs[0];
        exprGuild = (Expression<Guild>) exprs[1];
        if (Utils.parseVar(exprs[2]) == null) return false;
        var = Utils.parseVar(exprs[2]);
        return true;
    }

    @Override
    public void runEffect(Event e) {
        TextChannelBuilder builder = exprBuilder.getSingle(e);
        Guild guild = exprGuild.getSingle(e);
        if (builder == null || guild == null) return;
        pause();
        Utils.handleRestAction(
                builder.createChannel(guild),
                channel -> {
                    if (var != null)
                        Utils.setSkriptVariable(var, channel, e);
                    restart();
                },
                null
        );
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "create text channel using builder " + exprBuilder.toString(e, debug) + " in guild " + exprGuild.toString(e, debug);
    }

}
