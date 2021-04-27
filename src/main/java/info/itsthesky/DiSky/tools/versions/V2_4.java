package info.itsthesky.DiSky.tools.versions;

import ch.njol.skript.util.SkriptColor;

public class V2_4 implements VersionAdapter {

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
}
