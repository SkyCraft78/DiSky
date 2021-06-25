package info.itsthesky.disky.skript.scope.messagebuilder;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.EffectSection;
import info.itsthesky.disky.tools.MessageBuilder;
import info.itsthesky.disky.tools.object.ButtonBuilder;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Message Builder Scope")
@Description("This builder allow you to create message builder easily, specifying if a new line character have to be added after each append effect.")
@Since("2.0")
public class ScopeMessageBuilder extends EffectSection {

    public static MessageBuilder lastBuilder;

    static {
        Skript.registerCondition(ScopeMessageBuilder.class, "make [new] [discord] (msg|message) builder [with new lined]");
    }

    private boolean isNewLined;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (checkIfCondition()) return false;
        if (!hasSection()) return false;
        loadSection(true);
        isNewLined = parseResult.expr.contains("with new lined");
        return true;
    }

    @Override
    protected void execute(Event e) {
        lastBuilder = new MessageBuilder();
        if (isNewLined)
            lastBuilder.setNewLined(true);
        runSection(e);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "make new message builder " + (isNewLined ? " with new lined " : "");
    }

}
