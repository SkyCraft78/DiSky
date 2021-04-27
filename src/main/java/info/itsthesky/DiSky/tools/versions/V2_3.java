package info.itsthesky.DiSky.tools.versions;

import java.lang.reflect.Field;
import java.util.HashMap;

import ch.njol.skript.Skript;
import ch.njol.skript.util.Color;

public class V2_3 implements VersionAdapter {

	private Field byNameField;
	
	public V2_3() {
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
}
