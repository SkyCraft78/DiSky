package info.itsthesky.disky.tools;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.events.bukkit.SkriptStartEvent;
import ch.njol.skript.lang.*;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.DiSky;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import ch.njol.skript.Skript;
import ch.njol.skript.effects.Delay;
import ch.njol.skript.timings.SkriptTimings;
import ch.njol.skript.variables.Variables;
import org.jetbrains.annotations.Nullable;

/**
 * Effects that extend this class are ran asynchronously. Next trigger item will be ran
 * in main server thread, as if there had been a delay before.
 * <p>
 * Majority of Skript and Minecraft APIs are not thread-safe, so be careful.
 *
 * Make sure to add set {@link ScriptLoader#getHasDelayBefore()} to
 * {@link ch.njol.util.Kleenean#TRUE} in the {@code init} method.
 *
 * @author SkriptLang team
 */
public abstract class AsyncEffect extends Effect {

	@Override
	@Nullable
	protected TriggerItem walk(Event e) {
		debug(e, true);
		
		Delay.addDelayedEvent(e); // Mark this event as delayed
		Object _localVars = null;
		if (DiSky.SkriptUtils.MANAGE_LOCALES)
			_localVars = Variables.removeLocals(e); // Back up local variables
		Object localVars = _localVars;

		if (!Skript.getInstance().isEnabled()) // See https://github.com/SkriptLang/Skript/issues/3702
			return null;

		Bukkit.getScheduler().runTaskAsynchronously(Skript.getInstance(), () -> {
			// Re-set local variables
			if (DiSky.SkriptUtils.MANAGE_LOCALES && localVars != null)
				Variables.setLocalVariables(e, localVars);

			execute(e); // Execute this effect
			
			if (getNext() != null) {
				Bukkit.getScheduler().runTask(Skript.getInstance(), () -> { // Walk to next item synchronously
					Object timing = null;
					if (SkriptTimings.enabled()) { // getTrigger call is not free, do it only if we must
						Trigger trigger = getTrigger();
						if (trigger != null) {
							timing = SkriptTimings.start(trigger.getDebugLabel());
						}
					}

					if (DiSky.SkriptUtils.MANAGE_LOCALES && localVars != null)
						Variables.setLocalVariables(e, localVars);
					
					TriggerItem.walk(getNext(), e);

					if (DiSky.SkriptUtils.MANAGE_LOCALES)
						Variables.removeLocals(e); // Clean up local vars, we may be exiting now
					
					SkriptTimings.stop(timing); // Stop timing if it was even started
				});
			} else {
				if (DiSky.SkriptUtils.MANAGE_LOCALES)
					Variables.removeLocals(e);
			}
		});
		return null;
	}
}