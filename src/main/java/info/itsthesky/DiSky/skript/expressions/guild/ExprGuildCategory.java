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
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Category;
import org.bukkit.event.Event;

@Name("Guild Category")
@Description("Return all category of the specific guild")
@Examples("reply with \"This server have %size of category of event-guild% categories!!\"")
@Since("1.6")
public class ExprGuildCategory extends SimpleExpression<Category> {

	static {
		Skript.registerExpression(ExprGuildCategory.class, Category.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] [discord] [guild] categor(y|ies) of [the] [guild] %guild%");
	}

	private Expression<Guild> exprGuild;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprGuild = (Expression<Guild>) exprs[0];
		return true;
	}

	@Override
	protected Category[] get(final Event e) {
		Guild guild = exprGuild.getSingle(e);
		if (guild == null) return new Category[0];
		return guild.getCategories().toArray(new Category[0]);
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends Category> getReturnType() {
		return Category.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "category of guild " + exprGuild.toString(e, debug);
	}

}