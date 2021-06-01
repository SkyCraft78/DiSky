package info.itsthesky.disky.skript.expressions.buttons;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.object.ButtonRow;
import info.itsthesky.disky.tools.object.ButtonRow;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import org.bukkit.event.Event;

@Name("New Button Row")
@Description("Create a new row of button with a maximum of 5 button per row. A message can only have a maximum of 5 row.")
@Since("1.13")
public class ExprNewButtonsRow extends SimpleExpression<ButtonRow> {
    
    static {
        Skript.registerExpression(ExprNewButtonsRow.class, ButtonRow.class, ExpressionType.SIMPLE,
                "[a] new [buttons] row");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected ButtonRow[] get(Event e) {
        return new ButtonRow[] {new ButtonRow()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends ButtonRow> getReturnType() {
        return ButtonRow.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "new button row";
    }
}