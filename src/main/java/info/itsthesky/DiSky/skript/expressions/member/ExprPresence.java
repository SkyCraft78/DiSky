package info.itsthesky.disky.skript.expressions.member;

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
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.event.Event;

@Name("Presence")
@Description("Return a custom presence to use in 'mark %bot% as %presence%' effect.")
@Examples("set presence of bot \"NAME\" to listening \"Kill LA Kill, Before my body is Dry\"\n" +
		"set presence of bot \"NAME\" to watching \"nanatsu no taizai\"\n" +
		"set presence of bot \"NAME\" to playing \"Evoland 2\"\n" +
		"set presence of bot \"NAME\" to streaming \"Kill LA Kill, Before my body is Dry\" with url \"https://www.youtube.com/watch?v=2h1OcA7juOQ\"")
@Since("1.12")
public class ExprPresence extends SimpleExpression<Activity> {

	static {
		Skript.registerExpression(ExprPresence.class, Activity.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] listening [to] %string%",
				"["+ Utils.getPrefixName() +"] watching [to] %string%",
				"["+ Utils.getPrefixName() +"] playing [to] %string%",
				"["+ Utils.getPrefixName() +"] streaming [to] %string% with [the] url %string%",
				"["+ Utils.getPrefixName() +"] competing [to] %string%"
				);
	}

	private int pattern;
	private Expression<String> exprInput;
	private Expression<String> exprURL;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		pattern = matchedPattern;
		exprInput = (Expression<String>) exprs[0];
		if (matchedPattern == 3) exprURL = (Expression<String>) exprs[1];
		return true;
	}

	@Override
	protected Activity[] get(Event e) {
		String input = exprInput.getSingle(e);
		String url = exprURL == null ? null : exprURL.getSingle(e);
		if (input == null) return new Activity[0];
		Activity activity = null;
		switch (pattern) {
			case 0:
				activity = Activity.listening(input);
				break;
			case 1:
				activity = Activity.watching(input);
				break;
			case 2:
				activity = Activity.playing(input);
				break;
			case 3:
				if (url == null)
					Skript.error("[DiSky] The streaming URL cannot be null or not set!");
				if (!Activity.isValidStreamingUrl(url))
					Skript.error("[DiSky] The streaming URL specified for the presence is NOT valid! (Input: "+url+")");
				activity = Activity.streaming(input, url);
				break;
			case 4:
				activity = Activity.competing(input);
		}
		return new Activity[] {activity};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Activity> getReturnType() {
		return Activity.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		switch (pattern) {
			case 0:
				return "listening " + exprInput.toString(e, debug);
			case 1:
				return "watching " + exprInput.toString(e, debug);
			case 2:
				return "playing " + exprInput.toString(e, debug);
			case 3:
				return "streaming " + exprInput.toString(e, debug) + " with url " + exprURL.toString(e, debug);
			case 4:
				return "competing " + exprInput.toString(e, debug);
		}
		return "error";
	}

}