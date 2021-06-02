package info.itsthesky.disky.skript.expressions.buttons;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.AsyncEffect;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.ButtonBuilder;
import info.itsthesky.disky.tools.object.ButtonRow;
import info.itsthesky.disky.tools.object.RoleBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;

@Name("Add Button to Row")
@Description("Add a new button to a specific button row. Maximum of 5 button per row, 5 row per message.")
@Examples("discord command buttons:\n" +
        "\tprefixes: !\n" +
        "\ttrigger:\n" +
        "\t\treply with \"*buttons ...*\" and store it in {_msg}\n" +
        "\t\tset {_row} to new buttons row\n" +
        "\t\tadd new link button with url \"http://disky.itsthesky.info/\" with style danger with content \"DiSky Website\" with emoji \"\uD83D\uDD17\" to row {_row}\n" +
        "\t\tadd new button with url \"custom-id\" with style danger with content \"Dangerous!\" with emoji \"\uD83D\uDD36\" to row {_row}\n" +
        "\t\tadd new button with url \"custom-id2\" with style success with content \"Green :D\" with emoji \"\uD83D\uDCD7\" to row {_row}\n" +
        "\t\tadd new disabled button with url \"custom-id3\" with style secondary with content \"I'm disabled\" with emoji \"\uD83D\uDE2D\" to row {_row}\n" +
        "\t\tadd row {_row} to message {_msg}")
@Since("1.13")
public class EffAddButtonToRow extends AsyncEffect {

    static {
        Skript.registerEffect(EffAddButtonToRow.class,
                "["+ Utils.getPrefixName() +"] add [the] [button] %button% to [the] row %buttonrow%"
        );
    }

    private Expression<ButtonBuilder> exprButton;
    private Expression<ButtonRow> exprRow;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprButton = (Expression<ButtonBuilder>) exprs[0];
        exprRow = (Expression<ButtonRow>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        ButtonBuilder button = exprButton.getSingle(e);
        ButtonRow row = exprRow.getSingle(e);
        if (button == null || row == null) return;
        row.addButton(button);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "add the button " + exprButton.toString(e, debug) + " to the row " + exprRow.toString(e, debug);
    }

}
