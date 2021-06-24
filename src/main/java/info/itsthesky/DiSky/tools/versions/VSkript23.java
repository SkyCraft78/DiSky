package info.itsthesky.disky.tools.versions;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.lang.Variable;
import ch.njol.skript.lang.VariableString;
import ch.njol.skript.log.HandlerList;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.skript.util.SkriptColor;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.ReflectionUtils;

public class VSkript23 implements VersionAdapter {
	@Override
	public Class<SkriptColor> getColorClass() {
		return SkriptColor.class;
	}

	@Override
	public SkriptColor colorFromName(String name) {
		return SkriptColor.fromName(name);
	}

	@Override
	public String getColorName(Object color) {
		return ((SkriptColor) color).getName();
	}

	@Override
	public Kleenean getHasDelayBefore() {
		return ScriptLoader.getHasDelayBefore();
	}

	@Override
	public HandlerList getHandlers() {
		return ReflectionUtils.getField(SkriptLogger.class, null, "handlers");
	}

	@Override
	public void setHasDelayBefore(Kleenean hasDelayBefore) {
		ScriptLoader.setHasDelayBefore(hasDelayBefore);
	}

	@Override
	public VariableString getVariableName(Variable<?> var) {
		return var.getName();
	}
}
