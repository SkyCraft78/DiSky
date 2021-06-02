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
import info.itsthesky.disky.tools.StaticData;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.ButtonBuilder;
import info.itsthesky.disky.tools.object.ButtonRow;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

@Name("Add Row to Message")
@Description("Add a new button rows to a message. Maximum 5 row per message.getMessage().")
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
public class EffAddRowToMessage extends AsyncEffect {

    static {
        Skript.registerEffect(EffAddRowToMessage.class,
                "(add|set) [the] [button] row %buttonrows% to [the] [message] %message%"
        );
    }

    private Expression<ButtonRow> exprRow;
    private Expression<UpdatingMessage> exprMessage;
    private boolean isSet;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprRow = (Expression<ButtonRow>) exprs[0];
        exprMessage = (Expression<UpdatingMessage>) exprs[1];
        isSet = parseResult.expr.contains("set");
        return true;
    }

    @Override
    protected void execute(Event e) {
        UpdatingMessage message = exprMessage.getSingle(e);
        ButtonRow[] row1 = exprRow.getAll(e);
        if (message == null || row1.length == 0) return;
        
        if (!isSet) {
            List<ActionRow> rows = new ArrayList<>(StaticData.actionRows.get(message.getMessage().getIdLong()) == null ? new ArrayList<>() : StaticData.actionRows.get(message.getMessage().getIdLong()));
            for (ButtonRow row : row1) {
                List<Button> buttons = new ArrayList<>();

                for (ButtonBuilder buttonBuilder : row.getButtons()) {
                    if (buttonBuilder.build() != null)
                        buttons.add(buttonBuilder.build());
                }
                if (buttons.size() > 0) rows.add(ActionRow.of(buttons.toArray(new Component[0])));
            }
            message.getMessage().editMessage(message.getMessage())
                    .setActionRows(rows)
                    .queue();
            StaticData.actionRows.put(message.getMessage().getIdLong(), rows);   
        } else {
            List<ActionRow> rows = new ArrayList<>(StaticData.actionRows.get(message.getMessage().getIdLong()) == null ? new ArrayList<>() : StaticData.actionRows.get(message.getMessage().getIdLong()));
            for (ButtonRow row : row1) {
                List<Button> buttons = new ArrayList<>();

                for (ButtonBuilder buttonBuilder : row.getButtons()) {
                    if (buttonBuilder.build() != null)
                        buttons.add(buttonBuilder.build());
                }
                if (buttons.size() > 0) rows.add(ActionRow.of(buttons.toArray(new Component[0])));
            }
            List<Button> buttons = new ArrayList<>();
            for (ButtonRow row : row1) {
                for (ButtonBuilder buttonBuilder : row.getButtons()) {
                    if (buttonBuilder.build() != null)
                        buttons.add(buttonBuilder.build());
                }
            }
            ActionRow row = ActionRow.of(buttons.toArray(new Component[0]));
            message.getMessage().editMessage(message.getMessage())
                    .setActionRows(row)
                    .queue();
            StaticData.actionRows.put(message.getMessage().getIdLong(), rows);
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "add the row " + exprRow.toString(e, debug) + " to the message " + exprMessage.toString(e, debug);
    }

}
