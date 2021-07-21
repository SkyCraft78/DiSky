package info.itsthesky.disky.tools.async;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.*;
import ch.njol.skript.timings.SkriptTimings;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.Utils;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

/**
 * Effect that can easily manage how the TriggerItem are executed.
 * @author ItsTheSky
 */
public abstract class WaiterEffect<T> extends Effect {

    private Event event;
    private Object localVars;
    @Nullable protected Variable<T> changedVariable = null;

    public abstract boolean initEffect(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult);

    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        Utils.setHasDelayBefore(Kleenean.TRUE);
        return initEffect(expressions, i, kleenean, parseResult);
    }

    public abstract void runEffect(Event e);

    public void setChangedVariable(@Nullable Variable<T> changedVariable) {
        this.changedVariable = changedVariable;
    }

    public void clearChangedVariable() {
        setChangedVariable(null);
    }

    /*
    Working on walk method so :>
     */
    @Override
    protected void execute(Event event) { }

    @Nullable
    @Override
    protected TriggerItem walk(Event e) {
        debug(e, true);
        event = e;

        localVars = Variables.removeLocals(e);
        if (localVars != null)
            Variables.setLocalVariables(e, localVars);

        if (!Skript.getInstance().isEnabled()) // See https://github.com/SkriptLang/Skript/issues/3702
            return null;

        runEffect(e);

        return null;
    }

    protected void restart() {
        runItems();
    }

    protected void restart(T object) {
        runItems(object);
    }

    protected void changeVariable(Event e, Object object) {
        if (changedVariable != null)
            changedVariable.change(e, new Object[] {object}, Changer.ChangeMode.SET);
    }

    protected void runItems(Event e, T object) {

        if (localVars != null)
            Variables.setLocalVariables(e, localVars);

        changeVariable(e, object);

        localVars = Variables.removeLocals(e);

        if (getNext() != null) {
            Bukkit.getScheduler().runTask(Skript.getInstance(), () -> { // Walk to next item synchronously
                Object timing = null;
                if (SkriptTimings.enabled()) { // getTrigger call is not free, do it only if we must
                    Trigger trigger = getTrigger();
                    if (trigger != null)
                        timing = SkriptTimings.start(trigger.getDebugLabel());
                }

                if (localVars != null)
                    Variables.setLocalVariables(e, localVars);
                TriggerItem.walk(getNext(), e);
                SkriptTimings.stop(timing); // Stop timing if it was even started
            });
        }

    }

    protected void runItems() {
        runItems(this.event, null);
    }

    protected void runItems(T object) {
        runItems(this.event, object);
    }
}
