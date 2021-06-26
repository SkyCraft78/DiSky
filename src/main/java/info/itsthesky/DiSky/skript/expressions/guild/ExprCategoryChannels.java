package info.itsthesky.disky.skript.expressions.guild;

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
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.GuildChannel;
import org.bukkit.event.Event;

@Name("Category Channels")
@Description("Return all channels (can be either voice or text) of a specific category")
@Examples("set {_channels::*} to channels of category with id \"818182473312895014\"")
@Since("2.0")
public class ExprCategoryChannels extends SimpleExpression<GuildChannel> {

	static {
		Skript.registerExpression(ExprCategoryChannels.class, GuildChannel.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] [discord] channel[s] of [the] [category] %category%");
	}

	private Expression<Category> exprCategory;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprCategory = (Expression<Category>) exprs[0];
		return true;
	}

	@Override
	protected GuildChannel[] get(final Event e) {
		Category category = exprCategory.getSingle(e);
		if (category == null) return new GuildChannel[0];
		return category.getChannels().toArray(new GuildChannel[0]);
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends GuildChannel> getReturnType() {
		return GuildChannel.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "channels of category " + exprCategory.toString(e, debug);
	}

}