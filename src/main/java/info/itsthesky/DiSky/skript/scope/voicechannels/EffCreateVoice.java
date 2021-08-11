package info.itsthesky.disky.skript.scope.voicechannels;

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
import info.itsthesky.disky.tools.object.VoiceChannelBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import org.bukkit.event.Event;

@Name("Create Voice Channel builder in Guild")
@Description("Create the voice channel builder in a guild, and optionally get it as voice channel type.")
@Examples("create voice channel builder in event-guild\ncreate voice channel builder in event-guild and store it in {_voice}")
@Since("1.6")
public class EffCreateVoice extends WaiterEffect<VoiceChannel> {

    static {
        Skript.registerEffect(EffCreateVoice.class,
                "["+ Utils.getPrefixName() +"] create [the] [voice] [channel] [builder] %voicechannelbuilder% in [the] [guild] %guild% [and store it in %-object%]");
    }

    private Expression<VoiceChannelBuilder> exprBuilder;
    private Expression<Guild> exprGuild;

    @SuppressWarnings("unchecked")
    @Override
    public boolean initEffect(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprBuilder = (Expression<VoiceChannelBuilder>) exprs[0];
        exprGuild = (Expression<Guild>) exprs[1];
        setChangedVariable((Variable<VoiceChannel>) exprs[2]);
        return true;
    }

    @Override
    public void runEffect(Event e) {
        VoiceChannelBuilder builder = exprBuilder.getSingle(e);
        Guild guild = exprGuild.getSingle(e);
        if (builder == null || guild == null) return;
        Utils.handleRestAction(
                builder.build(guild),
                this::restart,
                null
        );

    }

    @Override
    public String toString(Event e, boolean debug) {
        return "create voice channel using builder " + exprBuilder.toString(e, debug) + " in guild " + exprGuild.toString(e, debug);
    }

}
