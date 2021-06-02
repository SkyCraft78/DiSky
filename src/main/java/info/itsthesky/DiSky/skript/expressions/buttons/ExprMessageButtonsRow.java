package info.itsthesky.disky.skript.expressions.buttons;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.ButtonBuilder;
import info.itsthesky.disky.tools.object.ButtonRow;
import info.itsthesky.disky.tools.object.ButtonRow;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

@Name("Message Buttons Row")
@Description("Get every row of buttons of a message. The number must be between 1 and 5 (max 5 buttons per row, 5 row per message)")
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
public class ExprMessageButtonsRow extends SimpleExpression<ButtonRow> {
    static {
        Skript.registerExpression(ExprMessageButtonsRow.class, ButtonRow.class, ExpressionType.SIMPLE,
                "[buttons] row of [the] [message] %message%");
    }

    private Expression<UpdatingMessage> exprMessage;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprMessage = (Expression<UpdatingMessage>) exprs[0];
        return true;
    }

    @Override
    protected ButtonRow[] get(Event e) {
        UpdatingMessage message = exprMessage.getSingle(e);
        if (message == null) return new ButtonRow[0];
        List<ButtonRow> rows = new ArrayList<>();
        for (ActionRow row : message.getMessage().getActionRows()) {
            rows.add(new ButtonRow(Utils.convertButtons(row.getButtons())));
        }
        return rows.toArray(new ButtonRow[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends ButtonRow> getReturnType() {
        return ButtonRow.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "buttons row of message " + exprMessage.toString(e, debug);
    }
}