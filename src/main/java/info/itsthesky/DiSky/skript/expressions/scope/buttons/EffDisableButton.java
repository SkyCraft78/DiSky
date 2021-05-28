package info.itsthesky.disky.skript.expressions.scope.buttons;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.ButtonBuilder;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;

@Name("Disable / Enable Button")
@Description("Enable or disable a button before building.")
@Examples({"enable the button", "disable the button"})
@Since("1.12")
public class EffDisableButton extends Effect {

    static {
        Skript.registerEffect(EffDisableButton.class,
                "["+ Utils.getPrefixName() +"] enable [the] [button] %button%",
                "["+ Utils.getPrefixName() +"] disable [the] [button] %button%"
                );
    }

    private Expression<ButtonBuilder> exprButton;
    private int pattern;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprButton = (Expression<ButtonBuilder>) exprs[0];
        this.pattern = matchedPattern;
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            ButtonBuilder button = exprButton.getSingle(e);
            if (button == null) return;
            button.setDisabled(pattern != 0);
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return (pattern == 0 ? "disable " : "enable ") + "the button " + exprButton.toString(e, debug);
    }

}
