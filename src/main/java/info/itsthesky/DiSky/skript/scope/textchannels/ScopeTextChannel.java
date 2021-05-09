package info.itsthesky.disky.skript.scope.textchannels;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.EffectSection;
import info.itsthesky.disky.tools.object.TextChannelBuilder;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Text Channel Builder")
@Description("This builder allow you to make text channel easily")
@Since("1.4")
@Examples("")
public class ScopeTextChannel extends EffectSection {

    public static TextChannelBuilder lastBuilder;

    static {
        Skript.registerCondition(ScopeTextChannel.class, "make [new] [discord] text[ ][-][ ]channel");
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
        lastBuilder = new TextChannelBuilder();
        runSection(e);

    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "make new text channel";
    }

}
