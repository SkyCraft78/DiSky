package info.itsthesky.disky.skript.scope.commands;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.EffectSection;
import info.itsthesky.disky.tools.object.SlashCommand;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Command Builder")
@Description("[BETA] This builder allow you to make discord command easily")
@Since("1.5")
@Examples("make new command:")
public class ScopeCommand extends EffectSection {

    public static SlashCommand lastBuilder;

    static {
        Skript.registerCondition(ScopeCommand.class, "make [new] [discord] command");
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
        lastBuilder = new SlashCommand();
        runSection(e);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "make new command";
    }

}
