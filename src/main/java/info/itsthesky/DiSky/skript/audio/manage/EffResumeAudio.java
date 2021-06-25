package info.itsthesky.disky.skript.audio.manage;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.managers.music.AudioUtils;
import info.itsthesky.disky.skript.events.audio.TrackEvent;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

@Name("Resume Guild Audio")
@Description("Resume the current audio a guild is playing.")
@Examples("discord command resume:\n" +
        "\tprefixes: *\n" +
        "\ttrigger:\n" +
        "\t\tresume in event-guild")
@Since("1.6")
public class EffResumeAudio extends Effect {

    static {
        Skript.registerEffect(EffResumeAudio.class, // [the] [bot] [(named|with name)] %string%
                "["+ Utils.getPrefixName() +"] resume [the] [audio] [track] (in|from) [the] [guild] %guild%");
    }

    private Expression<Guild> exprGuild;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprGuild = (Expression<Guild>) exprs[0];
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void execute(Event e) {
        Guild guild = exprGuild.getSingle(e);
        if (guild == null) return;
        AudioUtils.getGuildAudioPlayer(guild).getPlayer().setPaused(false);
        Utils.sync(() -> Bukkit.getPluginManager().callEvent(new TrackEvent(TrackEvent.TrackState.PLAY, guild.getJDA(), guild, AudioUtils.getGuildAudioPlayer(guild).getScheduler().getQueue().peek())));
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "resume audio track in guild " + exprGuild.toString(e, debug);
    }

}
