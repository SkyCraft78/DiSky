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

@Name("Color from Hex")
@Description("Return a java.awt Color from hexadecimal code (can be used in embed). Don't add the # into the code!")
@Examples("set color of embed to color from hex \"d99400\"")
@Since("1.3")
public class ExprColorFromHex extends SimpleExpression<Color> {

	static {
		if (!DiSky.getPluginManager().isPluginEnabled("SkImage")) {
			Skript.registerExpression(ExprColorFromHex.class, Color.class, ExpressionType.SIMPLE,
					"["+ Utils.getPrefixName() +"] [the] color from hex[adecimal] [code] %string%");
		}
	}

	private Expression<String> exprHex;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprHex = (Expression<String>) exprs[0];
		return true;
	}

	@Override
	protected Color[] get(final Event e) {
		String hex = exprHex.getSingle(e);
		if (hex == null) return new Color[0];
		return new Color[] {new Color(
				Utils.hexToInt(hex)
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
		return "color from hexadecimal code " + exprHex.toString(e, debug);
	}

}