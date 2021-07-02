package info.itsthesky.disky.tools.object;

import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ButtonRow {

    private List<ButtonBuilder> buttons;

    public ButtonRow(final List<ButtonBuilder> buttons) {
        this.buttons = buttons;
    }

    public void clearButtons() { buttons = new ArrayList<>(); }

    public ButtonRow() {
        this.buttons = new ArrayList<>();
    }

    public void addButton(ButtonBuilder builder) {
        this.buttons.add(builder);
    }

    public List<ButtonBuilder> getButtons() {
        return buttons;
    }
}
