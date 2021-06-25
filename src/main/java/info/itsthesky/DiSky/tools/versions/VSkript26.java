package info.itsthesky.disky.tools.versions;

import ch.njol.skript.lang.Variable;
import ch.njol.skript.lang.VariableString;
import ch.njol.skript.lang.parser.ParserInstance;
import ch.njol.skript.log.HandlerList;
import ch.njol.skript.util.SkriptColor;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.ReflectionUtils;

/**
 * @author ItsTheSky, thanks to TPGamesNL for the delayed reflection method, and Sashie for the color conversion <3
 */
public class VSkript26 implements VersionAdapter {
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
		return ParserInstance.get().getHasDelayBefore();
	}

	@Override
	public void setHasDelayBefore(Kleenean hasDelayBefore) {
		ParserInstance.get().setHasDelayBefore(hasDelayBefore);
	}

	@Override
	public HandlerList getHandlers() {
		return ParserInstance.get().getHandlers();
	}

	@Override
	public VariableString getVariableName(Variable<?> var) {
		return ReflectionUtils.getField(var.getClass(), var, "name");
	}


}
