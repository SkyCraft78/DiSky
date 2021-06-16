package info.itsthesky.disky.skript.events;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import net.dv8tion.jda.api.JDA;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class TrackResume extends SkriptEvent {

    static {
        DiSkyEvent.register("Inner Event Name", TrackResume.class, TrackEvent.class,
                "track (play|resume)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");

    }

    private Expression<JDA> bot;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Literal<?>[] exprs, int i, SkriptParser.ParseResult parseResult) {
        bot = (Expression<JDA>) exprs[0];
        return true;
    }

    @Override
    public boolean check(Event event) {
        return ((TrackEvent) event).getState() == TrackEvent.TrackState.PLAY;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "on track resume" + (bot == null ? "" : " seen by" + bot.toString(e, debug));
    }

}