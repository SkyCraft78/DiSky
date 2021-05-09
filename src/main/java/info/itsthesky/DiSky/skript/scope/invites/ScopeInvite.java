package info.itsthesky.disky.skript.scope.invites;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.EffectSection;
import info.itsthesky.disky.tools.object.InviteBuilder;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Invite Builder")
@Description("This builder allow you to make invite easily")
@Since("1.7")
@Examples("")
public class ScopeInvite extends EffectSection {

    public static InviteBuilder lastBuilder;

    static {
        Skript.registerCondition(ScopeInvite.class, "make [new] [discord] invit(e|ation)");
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
        lastBuilder = new InviteBuilder();
        runSection(e);

    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "make new invite";
    }

}
