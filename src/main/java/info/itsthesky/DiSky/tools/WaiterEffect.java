package info.itsthesky.disky.tools;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
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

    public abstract boolean initEffect(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult);

    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        Utils.setHasDelayBefore(Kleenean.TRUE);
        return initEffect(expressions, i, kleenean, parseResult);
    }

    public abstract void runEffect(Event e);

    protected void resume() {
        setNext(next == null ? null : next);
    }

    protected void restart() {
        resume();
        runItems();
    }

    @Override
    protected void execute(Event e) {
        next = getNext();
        event = e;
    }

    @Nullable
    @Override
    protected TriggerItem walk(Event e) {
        // We init the next item (can be null)
        next = getNext();
        event = e;
        // Then we force an enabling before doing anything stupid
        resume();
        // And we finally can execute the effect itself.
        runEffect(e);
        // We overriding it, no need to return the next trigger item
        return null;
    }

    protected void runItems(Event e) {
        if (next == null) return;
        TriggerItem.walk(next, e);
    }

    protected void runItems() {
        runItems(this.event);
    }

    protected void pause() {
        next = getNext();
        setNext(null);
    }
}
