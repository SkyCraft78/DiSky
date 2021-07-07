package info.itsthesky.disky.tools;

import ch.njol.skript.effects.Delay;
import ch.njol.skript.lang.*;
import ch.njol.skript.timings.SkriptTimings;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

/**
 * Effect that can easily manage how the TriggerItem are executed.
 * @author ItsTheSky
 */
public abstract class WaiterEffect extends Effect {

    private @Nullable TriggerItem next;
    private Event event;
    private Object firstVars;
    private Object localVars;

    public abstract boolean initEffect(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult);

    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        Utils.setHasDelayBefore(Kleenean.TRUE);
        return initEffect(expressions, i, kleenean, parseResult);
    }

    public abstract void runEffect(Event e);

    @Override
    protected void execute(Event e) {
        next = getNext();
        firstVars = copyVar(e);
        event = e;
    }

    /* @Nullable
    @Override
    protected TriggerItem walk(Event e) {
        // We init the next item (can be null)
        next = getNext();
        event = e;
        // And we finally can execute the effect itself.
        Delay.addDelayedEvent(e);
        Object secondVar = copyVar(e);
        localVars = Utils.pasteVarMaps(firstVars, secondVar);
        if (localVars != null)
            Variables.setLocalVariables(e, localVars);
        runEffect(e);
        // We overriding it, no need to return the next trigger item
        // The execution will always be stopped, we always returning null here.
        return null;
    }
    */

    protected void restart() {
        runItems();
    }

    protected void runItems(Event e) {
        if (next == null) return;

        if (localVars != null)
            Variables.setLocalVariables(e, localVars);

        Object timing = null;
        if (SkriptTimings.enabled()) {
            Trigger trigger = getTrigger();
            if (trigger != null) {
                timing = SkriptTimings.start(trigger.getDebugLabel());
            }
        }

        TriggerItem.walk(next, e);
        SkriptTimings.stop(timing);

    }

    protected Object copyVar(Event e) {
        Object vars = Variables.removeLocals(e);
        Variables.setLocalVariables(e, vars);
        return vars;
    }

    protected void runItems() {
        runItems(this.event);
    }
}
