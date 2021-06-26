package info.itsthesky.disky.skript.expressions.buttons;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky.tools.AsyncEffect;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.MultiplyPropertyExpression;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.ButtonBuilder;
import info.itsthesky.disky.tools.object.ButtonRow;
import info.itsthesky.disky.tools.object.RoleBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Row's button")
@Description("Manage the buttons of the specific row. Maximum of 5 button per row, 5 row per message.")
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
@Since("2.0")
public class EffAddButtonToRow extends MultiplyPropertyExpression<ButtonRow, ButtonBuilder> {

    static {
        register(EffAddButtonToRow.class,
                ButtonBuilder.class,
                "row[s]",
                "buttonrow"
        );
    }
    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        ButtonBuilder button = (ButtonBuilder) delta[0];
        switch (mode) {
            case ADD:
                for (ButtonRow row : getExpr().getAll(e)) {
                    row.addButton(button);
                }
                break;
            case SET:
                for (ButtonRow row : getExpr().getAll(e)) {
                    row.clearButtons();
                    row.addButton(button);
                }
                break;
        }
    }

    @Override
    protected String getPropertyName() {
        return "rows";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode.equals(Changer.ChangeMode.ADD) || mode.equals(Changer.ChangeMode.SET))
            return CollectionUtils.array(ButtonBuilder.class);
        return new Class[0];
    }

    @Nullable
    @Override
    public ButtonBuilder[] convert(ButtonRow buttonRow) {
        return buttonRow.getButtons().toArray(new ButtonBuilder[0]);
    }

    @Override
    public Class<? extends ButtonBuilder> getReturnType() {
        return ButtonBuilder.class;
    }
}
