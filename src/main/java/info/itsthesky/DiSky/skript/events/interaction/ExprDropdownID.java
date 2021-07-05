package info.itsthesky.disky.skript.events.interaction;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("Id of event-dropdown")
@Description("Get the ID of the dropdown interacted in a selection menu event.")
@Since("2.0")
public  class ExprDropdownID extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprDropdownID.class, String.class, ExpressionType.SIMPLE,
                "[the] (drop[ ]down|seletc[s]|selection menu) id"
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!ScriptLoader.isCurrentEvent(SelectionMenu.EvtSelectionMenu.class)) {
            Skript.error("The dropdown id can only be used in a selection menu update event!");
            return false;
        }
        return true;
    }

    @Nullable
    @Override
    protected String[] get(Event e) {
        SelectionMenu.EvtSelectionMenu event = (SelectionMenu.EvtSelectionMenu) e;
        return new String[] {event.getJDAEvent().getComponent().getId()};
    }
    @Override
    public Class<? extends String> getReturnType() { return String.class; }

    @Override
    public boolean isSingle() { return true; }
    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the dropdown id";
    }
}
