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

@Name("Set Bot Presence")
@Description("Make specific bot playing, streaming, listening or arching something.")
@Examples("mark bot \"MyBot\" listening \"my favourite music, Might+U\"")
@Since("1.12")
public class EffSetPresence extends Effect {

    static {
        Skript.registerEffect(EffSetPresence.class,
                "["+ Utils.getPrefixName() +"] mark %bot% [as] %presence%");
    }

    private Expression<JDA> exprBot;
    private Expression<Activity> exprPresence;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprBot = (Expression<JDA>) exprs[0];
        exprPresence = (Expression<Activity>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        JDA bot = exprBot.getSingle(e);
        Activity activity = exprPresence.getSingle(e);
        if (bot == null || activity == null) return;
        switch (activity.getType()) {
            case WATCHING:
                bot.getPresence().setActivity(Activity.watching(activity.getName()));
                break;
            case LISTENING:
                bot.getPresence().setActivity(Activity.listening(activity.getName()));
                break;
            case DEFAULT:
                bot.getPresence().setActivity(Activity.playing(activity.getName()));
                break;
            case STREAMING:
                bot.getPresence().setActivity(Activity.streaming(activity.getName(), activity.getUrl()));
                break;
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "mark " + exprBot.toString(e, debug) + " as " + exprPresence.toString(e, debug);
    }

}
