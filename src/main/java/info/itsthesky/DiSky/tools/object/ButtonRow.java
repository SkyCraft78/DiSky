package info.itsthesky.disky.tools.object;

import java.util.ArrayList;
import java.util.List;

public class ButtonRow {

    private final List<ButtonBuilder> buttons;

    public ButtonRow(final List<ButtonBuilder> buttons) {
        this.buttons = buttons;
    }
    
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
