package info.itsthesky.disky.skript.expressions.colors;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.tools.Utils;
import org.bukkit.event.Event;

import java.awt.*;

@Name("Color from RGB")
@Description("Return a java.awt Color (to use in embed) from red, green and blue amount.")
@Examples("set color of embed to color from rgb 255, 30, 126")
@Since("1.3")
public class ExprColorFromRGB extends SimpleExpression<Color> {

	static {
		if (!DiSky.getPluginManager().isPluginEnabled("SkImage")) {
			Skript.registerExpression(ExprColorFromRGB.class, Color.class, ExpressionType.SIMPLE,
					"["+ Utils.getPrefixName() +"] [the] color from rgb %number%[ ][,][ ]%number%[ ][,][ ]%number%");
		}
	}

	private Expression<Number> exprRed, exprGreen, exprBlue;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprRed = (Expression<Number>) exprs[0];
		exprGreen = (Expression<Number>) exprs[1];
		exprBlue = (Expression<Number>) exprs[2];
		return true;
	}

	@Override
	protected Color[] get(final Event e) {
		Number red = exprRed.getSingle(e);
		Number green = exprGreen.getSingle(e);
		Number blue = exprBlue.getSingle(e);
		if (red == null || green == null || blue == null) return new Color[0];
		return new Color[] {new Color(
				Utils.round(red.doubleValue()),
				Utils.round(green.doubleValue()),
				Utils.round(blue.doubleValue())
		)};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Color> getReturnType() {
		return Color.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "color from rgb " + exprRed.toString(e, debug) + ", " + exprGreen.toString(e, debug) + ", " + exprBlue.toString(e, debug);
	}

}