package info.itsthesky.disky.skript.scope.component;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.EffectSection;
import info.itsthesky.disky.tools.object.ButtonBuilder;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Selection Menu Builder")
@Description("Create easily a new selection menu (aka dropdown).")
@Since("2.0")
public class ScopeSelection extends EffectSection {

    public static SelectionMenu.Builder lastBuilder;

    static {
        Skript.registerCondition(ScopeSelection.class, "make [new] [discord] (select[s]|dropdown|selection menu) with [the] id %string%");
    }

    private Expression<String> exprInput;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (checkIfCondition()) return false;
        if (!hasSection()) return false;
        loadSection(true);
        exprInput = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        String input = exprInput.getSingle(e);
        if (input == null) return;
        lastBuilder = SelectionMenu.create(input);
        runSection(e);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "make new select with id " + exprInput.toString(e, debug);
    }

}
