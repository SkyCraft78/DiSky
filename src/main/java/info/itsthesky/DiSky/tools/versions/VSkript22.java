package info.itsthesky.disky.tools.versions;

import java.lang.reflect.Field;
import java.util.HashMap;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Variable;
import ch.njol.skript.lang.VariableString;
import ch.njol.skript.log.HandlerList;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.skript.util.Color;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.ReflectionUtils;

/**
 * @author ItsTheSky, thanks to TPGamesNL for the delayed reflection method, and Sashie for the color conversion <3
 */
public class VSkript22 implements VersionAdapter {

	private Field byNameField;
	private final Field hasDelayBeforeField;
	
	public VSkript22() {
		try {
			Class<?> colorClass = Class.forName("ch.njol.skript.util.Color");
			if (Skript.fieldExists(getColorClass(), "BY_NAME")) {
				byNameField = getColorClass().getDeclaredField("BY_NAME");
			} else {
				byNameField = getColorClass().getDeclaredField("byName");
			}
		} catch (NoSuchFieldException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		byNameField.setAccessible(true);
		hasDelayBeforeField = ReflectionUtils.getField(ScriptLoader.class, "hasDelayBefore");
		hasDelayBeforeField.setAccessible(true);
	}
	
	@Override
	public Class<Color> getColorClass() {
		return Color.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Color colorFromName(String name) {
		try {
			return (Color) ((HashMap<String, Object>) byNameField.get(null)).get(name);
		} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
		//return Color.byName(name);
	}

	@Override
	public String getColorName(Object color) {
		return color.toString();
	}

	@Override
	public Kleenean getHasDelayBefore() {
		// Check https://github.com/SkriptLang/Skript/blob/7301bab6fe12b37c864f83b2bb9dfc0e505f3559/src/main/java/ch/njol/skript/ScriptLoader.java#L206
		return ReflectionUtils.getFieldValue(hasDelayBeforeField);
	}

	@Override
	public void setHasDelayBefore(Kleenean hasDelayBefore) {
		ReflectionUtils.setFieldValue(hasDelayBeforeField, hasDelayBefore);
	}

	@Override
	public HandlerList getHandlers() {
		return ReflectionUtils.getField(SkriptLogger.class, null, "handlers");
	}

	@Override
	public VariableString getVariableName(Variable<?> var) {
		return var.getName();
	}
}
