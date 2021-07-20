package info.itsthesky.disky.skript.component;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky.tools.MultiplyPropertyExpression;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.ButtonBuilder;
import info.itsthesky.disky.tools.object.ButtonRow;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

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
public class ExprMessageButtonsRow extends MultiplyPropertyExpression<UpdatingMessage, Object> {
    static {
        register(
                ExprMessageButtonsRow.class,
                Object.class,
                "(row[s]|component[s])",
                "message"
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (parseResult.expr.contains("inventory"))
            return false;
        this.expr = (Expression<UpdatingMessage>) expr[0];
        return true;
    }

    @Override
    protected String getPropertyName() {
        return "components";
    }

    @Nullable
    @Override
    public Object[] convert(UpdatingMessage updatingMessage) {
        return updatingMessage
                .getMessage()
                .getActionRows()
                .stream()
                .map(Object::toString)
                .toArray();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        UpdatingMessage message = getExpr().getSingle(e);
        if (message == null) return;
        List<ActionRow> rows = new ArrayList<>();
        switch (mode) {
            case SET:
                rows = new ArrayList<>();
                break;
            case ADD:
                rows = new ArrayList<>(message.getMessage().getActionRows());
                break;
            case RESET:
            case REMOVE_ALL:
                message
                        .getMessage()
                        .editMessage(message.getMessage())
                        .setActionRows()
                        .queue();
                return;
        }
        for (Object component : delta) {
            if (component instanceof ButtonRow) {
                List<Button> buttons = new ArrayList<>();
                for (ButtonBuilder buttonBuilder : ((ButtonRow) component).getButtons()) {
                    if (buttonBuilder.build() != null)
                        buttons.add(buttonBuilder.build());
                }
                if (buttons.size() > 0) rows.add(ActionRow.of(buttons.toArray(new Component[0])));
            } else {
                SelectionMenu selects = ((SelectionMenu.Builder) component).build();
                rows.add(ActionRow.of(selects));
            }
        }
        message
                .getMessage()
                .editMessage(message.getMessage())
                .setActionRows(rows.toArray(new ActionRow[0]))
                .queue();
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        switch (mode) {
            case SET:
            case ADD:
            case RESET:
            case REMOVE_ALL:
                return CollectionUtils.array(ButtonRow[].class, SelectionMenu.Builder[].class);
            default:
                return new Class[0];
        }
    }

    @Override
    public Class<?> getReturnType() {
        return Object.class;
    }
}