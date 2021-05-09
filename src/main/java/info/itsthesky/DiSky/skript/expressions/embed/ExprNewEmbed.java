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
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.event.Event;

@Name("New Embed")
@Description("Return an empty discord message embed.")
@Examples("set {_embed} to new discord embed")
@Since("1.0")
public class ExprNewEmbed extends SimpleExpression<EmbedBuilder> {

	static {
		Skript.registerExpression(ExprNewEmbed.class, EmbedBuilder.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] new [discord] [message] embed"
		);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		return true;
	}

	@Override
	protected EmbedBuilder[] get(Event e) {
		return new EmbedBuilder[] {new EmbedBuilder()};
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