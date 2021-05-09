package info.itsthesky.disky.skript.scope.category;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.EffectSection;
import info.itsthesky.disky.tools.object.CategoryBuilder;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Category Builder")
@Description("This builder allow you to make category easily")
@Since("1.4.1")
@Examples("")
public class ScopeCategory extends EffectSection {

    public static CategoryBuilder lastBuilder;

    static {
        Skript.registerCondition(ScopeCategory.class, "make [new] [discord] category");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (checkIfCondition()) {
            return false;
        }
        if (!hasSection()) {
            return false;
        }
        loadSection(true);
        return true;
    }

    @Override
    protected void execute(Event e) {
        lastBuilder = new CategoryBuilder();
        runSection(e);

    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "make new category";
    }

}
