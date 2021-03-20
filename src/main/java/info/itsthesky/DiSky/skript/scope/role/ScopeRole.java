package info.itsthesky.DiSky.skript.scope.role;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.DiSky.tools.EffectSection;
import info.itsthesky.DiSky.tools.object.RoleBuilder;
import info.itsthesky.DiSky.tools.object.TextChannelBuilder;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Role Builder")
@Description("This builder allow you to make role easily")
@Since("1.4")
@Examples("")
public class ScopeRole extends EffectSection {

    public static RoleBuilder lastBuilder;

    static {
        Skript.registerCondition(ScopeRole.class, "make [new] [discord] role");
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
        lastBuilder = new RoleBuilder();
        runSection(e);

    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "make new role";
    }

}
