package info.itsthesky.disky.tools;

import info.itsthesky.disky.skript.commands.Argument;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StaticData {

    public static SlashCommandEvent lastSlashCommandEvent;
    public static List<Argument<?>> lastArguments = new ArrayList<Argument<?>>();
    public static HashMap<Long, List<ActionRow>> actionRows = new HashMap<>();

    public void idk() {
        SelectionMenu menu = SelectionMenu.create("menu:class")
                .setPlaceholder("Choose your class") // shows the placeholder indicating what this menu is for
                .setRequiredRange(1, 1) // only one can be selected
                .addOptions(SelectOption.of("mage-fire", "Fire Mage").withDefault(true)) // default value
                .addOption("mage-arcane", "Arcane Mage")
                .addOption("mage-frost", "Frost Mage")
                .build();
    }

}
