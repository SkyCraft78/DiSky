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

@Name("Set Streaming Presence")
@Description("Make specific bot stream something. The URL must be valid!")
@Examples("mark \"MyBot\" streaming \"My developer :D\" with url \"https://your-twitch-url.com\"")
@Since("1.0")
public class EffSetStreaming extends Effect {

    static {
        Skript.registerEffect(EffSetStreaming.class,
                "["+ Utils.getPrefixName() +"] mark [the] [bot] [(named|with name)] %string% streaming [to] %string% with url %string%");
    }

    private Expression<String> exprName;
    private Expression<String> exprStream;
    private Expression<String> exprURL;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprName = (Expression<String>) exprs[0];
        exprStream = (Expression<String>) exprs[1];
        exprURL = (Expression<String>) exprs[2];
        return true;
    }

    @Override
    protected void execute(Event e) {
        String stream = exprStream.getSingle(e);
        String name = exprName.getSingle(e);
        String url = exprURL.getSingle(e);
        if (stream == null || name == null || url  == null) return;
        JDA bot = BotManager.getBot(name);
        bot.getPresence().setActivity(Activity.streaming(stream, url));
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "mark the bot named " + exprName.toString(e, debug) + " streaming " + exprStream.toString(e, debug) + " with url " + exprURL.toString(e, debug);
    }

}
