package info.itsthesky.disky.skript.effects.status;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.managers.BotManager;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.event.Event;

@Name("Set Playing Presence")
@Description("Make specific bot play something")
@Examples("mark \"MyBot\" playing \"to my favourite game, Evoland2 :D\"")
@Since("1.0")
public class EffSetPlaying extends Effect {

    static {
        Skript.registerEffect(EffSetPlaying.class,
                "["+ Utils.getPrefixName() +"] mark [the] [bot] [(named|with name)] %string% playing [to] %string%");
    }

    private Expression<String> exprName;
    private Expression<String> exprPlaying;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprName = (Expression<String>) exprs[0];
        exprPlaying = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        String playing = exprPlaying.getSingle(e);
        String name = exprName.getSingle(e);
        if (playing == null || name == null) return;
        JDA bot = BotManager.getBot(name);
        bot.getPresence().setActivity(Activity.playing(playing));
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "mark the bot named " + exprName.toString(e, debug) + " playing " + exprPlaying.toString(e, debug);
    }

}
