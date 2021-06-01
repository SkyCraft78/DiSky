package info.itsthesky.disky.skript.effects.bot;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky.tools.AsyncEffect;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.managers.BotManager;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import org.bukkit.event.Event;

@Name("Shutdown Discord Bot")
@Description("Close JDA instance and shutdown the specific bot." +
        "\n You can't use it as discord bot after shutdown it!")
@Examples("shutdown the bot named \"MyBot\"")
@Since("1.0")
public class EffShutdownBot extends AsyncEffect {

    static {
        Skript.registerEffect(EffShutdownBot.class,
                "["+ Utils.getPrefixName() +"] (stop|shutdown|close instance of) %bot%");
    }

    private Expression<JDA> exprName;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprName = (Expression<JDA>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        JDA name = exprName.getSingle(e);
        if (name == null) return;
        BotManager.removeAndShutdown(BotManager.getNameByJDA(name));
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "shutdown " + exprName.toString(e, debug);
    }

}
