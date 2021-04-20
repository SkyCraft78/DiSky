package info.itsthesky.DiSky.skript.audio.manage;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.DiSky.managers.music.AudioUtils;
import info.itsthesky.DiSky.managers.music.GuildAudioManager;
import info.itsthesky.DiSky.tools.Utils;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;

@Name("Stop Guild Queue")
@Description("Stop the current queue and clear it. See also 'pause' effect.")
@Examples("stop current queue of event-guild")
@Since("1.9")
public class EffStopGuildTrack extends Effect {

    static {
        Skript.registerEffect(EffStopGuildTrack.class,
                "["+ Utils.getPrefixName() +"] stop [current] queue (from|of) [the] [guild] %guild%");
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
        GuildAudioManager manager = AudioUtils.getGuildAudioPlayer(guild);
        manager.trackScheduler.clearQueue();
        manager.getPlayer().destroy();
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "skip current track of guild " + exprGuild.toString(e, debug);
    }

}
