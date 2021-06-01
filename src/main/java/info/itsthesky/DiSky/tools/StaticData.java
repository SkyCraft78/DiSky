package info.itsthesky.disky.tools;

import info.itsthesky.disky.skript.commands.Argument;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StaticData {

    public static SlashCommandEvent lastSlashCommandEvent;
    public static List<Argument<?>> lastArguments = new ArrayList<Argument<?>>();
    public static HashMap<Long, List<ActionRow>> actionRows = new HashMap<>();

}
