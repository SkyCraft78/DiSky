package info.itsthesky.disky.tools.versions;

import ch.njol.skript.lang.Variable;
import ch.njol.skript.lang.VariableString;
import ch.njol.skript.log.HandlerList;
import ch.njol.util.Kleenean;

/**
 * @author ItsTheSky, thanks to TPGamesNL for the delayed reflection method <3
 */
public interface VersionAdapter {

	Class<?> getColorClass();

	Object colorFromName(String name);

	String getColorName(Object color);

	Kleenean getHasDelayBefore();

	void setHasDelayBefore(Kleenean hasDelayBefore);

	HandlerList getHandlers();

	VariableString getVariableName(Variable<?> var);
}
