package info.itsthesky.disky.skript.scope.category;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.CategoryBuilder;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;

@Name("Create Category builder in Guild")
@Description("Create the category builder in a guild, and optionally get it as category type.")
@Examples("create category builder in event-guild\ncreate category builder in event-guild and store it in {_cat}")
@Since("1.4.1")
public class EffCreateCategory extends Effect {

    static {
        Skript.registerEffect(EffCreateCategory.class,
                "["+ Utils.getPrefixName() +"] create [the] [category] [builder] %categorybuilder% in [the] [guild] %guild% [and store it in %-object%]");
    }

    private Expression<CategoryBuilder> exprBuilder;
    private Expression<Guild> exprGuild;
    private Expression<Object> exprVar;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprBuilder = (Expression<CategoryBuilder>) exprs[0];
        exprGuild = (Expression<Guild>) exprs[1];
        if (exprs.length == 2) return true;
        exprVar = (Expression<Object>) exprs[2];
        return true;
    }

    @Override
    protected void execute(Event e) {
        CategoryBuilder builder = exprBuilder.getSingle(e);
        Guild guild = exprGuild.getSingle(e);
        if (builder == null || guild == null) return;
        Category category = builder.build(guild);
        if (exprVar == null) return;
        if (!(exprVar instanceof Variable)) return;
        Variable variable = (Variable) exprVar;
        Utils.setSkriptVariable(
                variable,
                category,
                e);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "create category using builder " + exprBuilder.toString(e, debug) + " in guild " + exprGuild.toString(e, debug);
    }

}
