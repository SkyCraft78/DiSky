package info.itsthesky.DiSky.skript.audio.manage;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.DiSky.managers.music.AudioUtils;
import info.itsthesky.DiSky.tools.Utils;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;

@Name("Bot is Paused in Guild")
@Description("Return if the bot is currently paused in a specific guild.")
@Since("1.11")
public class CondBotIsPaused extends Condition {

	static {
		Skript.registerCondition(CondBotIsPaused.class,
				"["+ Utils.getPrefixName() +"] [the] bot is [currently] paused in [the] [guild] %guild%",
				"["+ Utils.getPrefixName() +"] [the] bot (is not|isn't) [currently] paused in [the] [guild] %guild%"
		);
	}

	Expression<Guild> exprGuild;
	private boolean isNegate;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprGuild = (Expression<Guild>) exprs[0];
		isNegate = matchedPattern != 0;
		return true;
	}

	@Override
	public boolean check(final Event e) {
		Guild guild = exprGuild.getSingle(e);
		if (guild == null) return false;
		if (isNegate) {
			return !AudioUtils.getGuildAudioPlayer(guild).getPlayer().isPaused();
		} else {
			return AudioUtils.getGuildAudioPlayer(guild).getPlayer().isPaused();
		}
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "track the bot is paused in guild " + exprGuild.toString(e, debug);
	}

}