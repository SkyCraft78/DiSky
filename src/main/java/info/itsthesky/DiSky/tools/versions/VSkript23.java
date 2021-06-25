package info.itsthesky.disky.tools.versions;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.lang.Variable;
import ch.njol.skript.lang.VariableString;
import ch.njol.skript.log.HandlerList;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.skript.util.SkriptColor;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * @author ItsTheSky, thanks to TPGamesNL for the delayed reflection method, and Sashie for the color conversion <3
 */
public class VSkript23 implements VersionAdapter {

	private final Field hasDelayBeforeField;

	public VSkript23() {
		hasDelayBeforeField = ReflectionUtils.getField(ScriptLoader.class, "hasDelayBefore");
		hasDelayBeforeField.setAccessible(true);
	}

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
		// Check https://github.com/SkriptLang/Skript/blob/7301bab6fe12b37c864f83b2bb9dfc0e505f3559/src/main/java/ch/njol/skript/ScriptLoader.java#L206
		return ReflectionUtils.getFieldValue(hasDelayBeforeField);
	}

	@Override
	public HandlerList getHandlers() {
		return ReflectionUtils.getField(SkriptLogger.class, null, "handlers");
	}

	@Override
	public void setHasDelayBefore(Kleenean hasDelayBefore) {
		ReflectionUtils.setFieldValue(hasDelayBeforeField, hasDelayBefore);
	}

	@Override
	public VariableString getVariableName(Variable<?> var) {
		return var.getName();
	}
}
