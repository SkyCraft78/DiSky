package info.itsthesky.disky.tools;

import info.itsthesky.disky.skript.commands.Argument;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.ArrayList;
import java.util.List;

public class StaticData {

    public static SlashCommandEvent lastSlashCommandEvent;
    public static List<Argument<?>> lastArguments = new ArrayList<Argument<?>>();

}
