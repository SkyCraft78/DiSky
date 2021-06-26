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

@Name("Add or Set Row to Message")
@Description("Add a new button rows to a message. Maximum 5 row per message.getMessage().")
@Examples("discord command buttons:\n" +
        "\tprefixes: !\n" +
        "\ttrigger:\n" +
        "\t\t# > Buttons are working via rows;\n" +
        "\t\t# > One message can only have 5 rows, and 5 component per row.\n" +
        "\t\t# > Currently, there's only button as component, so maximum 25 buttons per messages, with 5 row of buttons.\n" +
        "\n" +
        "\t\t# > We create a new row\n" +
        "\t\tset {_row} to new buttons row\n" +
        "\t\t# > And we add different buttons, with a style, an ID or an URL, a content or an emoji to the row.\n" +
        "\t\tadd new link button with url \"http://disky.itsthesky.info/\" with style danger with content \"DiSky Website\" with emoji \"\uD83D\uDD17\" to row {_row}\n" +
        "\t\tadd new button with url \"button-id-here\" with style danger with content \"Dangerous!\" with emoji \"\uD83D\uDD36\" to row {_row}\n" +
        "\t\tadd new button with url \"cant-have-two-same-id\" with style success with content \"Green :D\" with emoji \"\uD83D\uDCD7\" to row {_row}\n" +
        "\t\tadd new disabled button with url \"no-space-allowed-either\" with style secondary with content \"I'm disabled\" with emoji \"\uD83D\uDE2D\" to row {_row}\n" +
        "\n" +
        "\t\t# > And we finally can send a message with buttons directly.\n" +
        "\t\t# > (Send (or reply) with buttons works since 2.0!\n" +
        "\t\treply with \"*I love buttons ...*\" with rows {_row}\n" +
        "\n" +
        "# > We can handle the button click in an INTERACTION event.\n" +
        "# > Since it's an interaction, we HAVE to either:\n" +
        "# -> Defer the interaction (can't reply)\n" +
        "# -> Reply to the interaction (can't defer)\n" +
        "on button click:\n" +
        "\tset {_id} to discord id of event-button\n" +
        "\t# > We check here the button's ID, and see if it's one of us:\n" +
        "\t{_id} is \"button-id-here\", \"cant-have-two-same-id\" or \"no-space-allowed-either\"\n" +
        "\t# > Since we don't reply, we just defering the interaction\n" +
        "\tdefer the interaction")
@Since("1.13")
public class EffAddRowToMessage extends AsyncEffect {

    static {
        Skript.registerEffect(EffAddRowToMessage.class,
                "add [the] [button] row[s] %buttonrows% to [the] [message] %message%",
                "set [the] [button] row[s] of [the] [message] %message% to %buttonrows%"
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
        isSet = matchedPattern == 1;
        return true;
    }

    @Override
    protected void execute(Event e) {
        UpdatingMessage message = exprMessage.getSingle(e);
        ButtonRow[] row1 = exprRow.getAll(e);
        if (message == null || row1.length == 0) return;

        List<ActionRow> rows = new ArrayList<>(message.getMessage().getActionRows());
        if (!isSet) {
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
        } else {
            rows = new ArrayList<>();
            for (ButtonRow row : row1) {
                List<Button> buttons = new ArrayList<>();
                for (ButtonBuilder buttonBuilder : row.getButtons()) {
                    if (buttonBuilder.build() != null)
                        buttons.add(buttonBuilder.build());
                }
                if (buttons.size() > 0) rows.add(ActionRow.of(buttons.toArray(new Component[0])));
            }
            message.getMessage()
                    .editMessage(message.getMessage())
                    .setActionRows(rows)
                    .queue();
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "add the row " + exprRow.toString(e, debug) + " to the message " + exprMessage.toString(e, debug);
    }

}
