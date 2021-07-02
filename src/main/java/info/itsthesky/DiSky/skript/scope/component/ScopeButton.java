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
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Button Builder")
@Description("This builder allow you to custom button easily. Use the one-line creator for a faster way! The string input represent the button ID or the URL if it's a link button.")
@Since("1.12")
public class ScopeButton extends EffectSection {

    public static ButtonBuilder lastBuilder;

    static {
        Skript.registerCondition(ScopeButton.class, "make [new] [discord] button with [the] [(url|id)] %string%");
    }

    private Expression<String> exprInput;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (checkIfCondition()) {
            return false;
        }
        if (!hasSection()) {
            return false;
        }
        loadSection(true);
        exprInput = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        String input = exprInput.getSingle(e);
        if (input == null) return;
        lastBuilder = new ButtonBuilder(input, null);
        runSection(e);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "make new button with id or url " + exprInput.toString(e, debug);
    }

}
