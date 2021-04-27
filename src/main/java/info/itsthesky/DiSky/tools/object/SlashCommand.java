package info.itsthesky.DiSky.tools.object;

import info.itsthesky.DiSky.DiSky;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class SlashCommand {
    
    private String name;
    private String description;
    private final List<OptionData> options = new ArrayList<>();
    
    public SlashCommand() {}

    public void addOption(OptionData date) {
        options.add(date);
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    public CommandData build() {
        if (name == null || description == null) {
            DiSky
                    .getInstance()
                    .getLogger()
                    .severe("DiSky can't build a discord command with an empty name and / or empty description!");
            DiSky
                    .getInstance()
                    .getLogger()
                    .severe("Name: '"+name+"' Description: '"+description+"'");
            return null;
        }
        CommandData cmd = new CommandData(
                name,
                description
        );
        for (OptionData data : options) {
            cmd.addOption(data);
        }
        return cmd;
    }
}
