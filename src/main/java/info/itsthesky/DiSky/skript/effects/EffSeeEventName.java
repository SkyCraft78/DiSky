package info.itsthesky.disky.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky.tools.async.AsyncEffect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.Utils;
import org.bukkit.event.Event;

@Name("Current Trigger event name")
@Description("Simple debug effect to sout the event's name which this effect is in.")
@Examples("sout the event name")
@Since("1.0")
public class EffSeeEventName extends AsyncEffect {

    static {
        Skript.registerEffect(EffSeeEventName.class,
                "["+ Utils.getPrefixName() +"] (debug|sout|broadcast) [the] event[ ][-][ ]name");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected void execute(Event e) {
        System.out.println(e.getEventName());
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "debug the event name";
    }

}
