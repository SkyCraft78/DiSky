package info.itsthesky.disky.tools.section;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.skript.commands.CommandFactory;
import info.itsthesky.disky.tools.EffectSection;
import info.itsthesky.disky.tools.StaticData;
import info.itsthesky.disky.tools.Utils;
import org.bukkit.event.Event;

public abstract class DiSkySection extends EffectSection {

    public static void register(final String pattern, final Class<? extends DiSkySection> clazz) {
        Skript.registerCondition(clazz,
                "["+ Utils.getPrefixName() +"] " + pattern + " [one time]"
        );
    }

    private boolean isOneTime;
    private boolean alreadyExecuted = false;

    // This one should never be used btw
    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        //throw new IllegalStateException();
        return init("disky section", null, null);
    }

    public boolean init(String sectionName, Class<? extends Event> clazzEvent, SkriptParser.ParseResult parseResult) {
        Utils.setHasDelayBefore(Kleenean.TRUE);
        if (checkIfCondition()) return false;
        isOneTime = parseResult.expr.endsWith("one time");
        StaticData.lastArguments = CommandFactory.getInstance().currentArguments;
        if (hasSection()) loadSection(sectionName, false, clazzEvent);
        return true;
    }

    @Override
    protected void runSection(Event e) {
        if (isOneTime) {
            if (alreadyExecuted) return;
            alreadyExecuted = true;
        }
        super.runSection(e);
    }

    // Working on the walk() method to make the section async
    @Override
    protected void execute(Event e) { walk(e); }

}
