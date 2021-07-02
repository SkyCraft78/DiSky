package info.itsthesky.disky.skript.events.interaction;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Menu Selected Options")
@Description("Get every options selected in a dropdown menu interact event.")
@Since("2.0")
public  class ExprSelectedOptions extends SimpleExpression<SelectOption> {

    static {
        Skript.registerExpression(ExprSelectedOptions.class, SelectOption.class, ExpressionType.SIMPLE,
                "[all] [the] (choices|options) selected",
                "[all] [the] selected (choices|options)"
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!ScriptLoader.isCurrentEvent(SelectionMenu.EvtSelectionMenu.class)) {
            Skript.error("The selected options can only be used in a selection menu update event!");
            return false;
        }
        return true;
    }

    @Nullable
    @Override
    protected SelectOption[] get(Event e) {
        SelectionMenu.EvtSelectionMenu event = (SelectionMenu.EvtSelectionMenu) e;
        return event.getJDAEvent().getSelectedOptions().toArray(new SelectOption[0]);
    }
    @Override
    public Class<? extends SelectOption> getReturnType() { return SelectOption.class; }
    @Override
    public boolean isSingle() { return true; }
    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "all of the selected choices";
    }
}
