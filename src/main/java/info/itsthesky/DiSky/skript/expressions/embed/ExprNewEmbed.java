package info.itsthesky.disky.skript.expressions.embed;

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
import info.itsthesky.disky.managers.EmbedManager;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.event.Event;

@Name("New Embed")
@Description("Return an empty discord message embed.")
@Examples("set {_embed} to new discord embed")
@Since("1.0, 1.12 (Template Support)")
public class ExprNewEmbed extends SimpleExpression<EmbedBuilder> {

	static {
		Skript.registerExpression(ExprNewEmbed.class, EmbedBuilder.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] new [discord] [message] embed [using [the] [template] [(named|with name|with id)] %-string%]"
		);
	}

	private Expression<String> exprTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		if (exprs.length != 0) exprTemplate = (Expression<String>) exprs[0];
		return true;
	}

	@Override
	protected EmbedBuilder[] get(Event e) {
		if (exprTemplate == null) {
			return new EmbedBuilder[] {new EmbedBuilder()};
		} else {
			if (exprTemplate.getSingle(e) == null)
				return new EmbedBuilder[] {new EmbedBuilder()};
			return new EmbedBuilder[] {EmbedManager.getTemplate(exprTemplate.getSingle(e))};
		}
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends EmbedBuilder> getReturnType() {
		return EmbedBuilder.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "new discord embed";
	}

}