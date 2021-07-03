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

import java.util.ArrayList;
import java.util.List;

@Name("Menu Selected Values")
@Description("Get every values configured in a selection menu which are currently selected.")
@Since("2.0")
public  class ExprSelectedOptions extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprSelectedOptions.class, String.class, ExpressionType.SIMPLE,
                "[all] [the] value[s] selected",
                "[all] [the] selected value[s]"
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!ScriptLoader.isCurrentEvent(SelectionMenu.EvtSelectionMenu.class)) {
            Skript.error("The selected values can only be used in a selection menu update event!");
            return false;
        }
        return true;
    }

    @Nullable
    @Override
    protected String[] get(Event e) {
        SelectionMenu.EvtSelectionMenu event = (SelectionMenu.EvtSelectionMenu) e;
        List<String> strings = new ArrayList<>();
        for (SelectOption option : event.getJDAEvent().getSelectedOptions())
            strings.add(option.getValue());
        return strings.toArray(new String[0]);
    }
    @Override
    public Class<? extends String> getReturnType() { return String.class; }
    @Override
    public boolean isSingle() { return true; }
    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "all of the selected values";
    }
}
