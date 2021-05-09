package info.itsthesky.disky.skript.scope.commands;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.managers.BotManager;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.SlashCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

@Name("Create command builder with Bot")
@Description("[BETA] Create the command builder with a bot.\n!!! COMMAND TOOK AN HOUR TO BE UPDATED !!!\nWE RECOMMEND TO REGISTER ALL COMMANDS IN ONE !!!")
@Examples("create command builder with bot \"MyBot\"")
@Since("1.5")
public class EffCreateCommand extends Effect {

    static {
        Skript.registerEffect(EffCreateCommand.class,
                    "["+ Utils.getPrefixName() +"] create [the] [command] [builder] %commandbuilders% (using|with|via) [bot] [(named|with name)] %string%");
    }

    private Expression<SlashCommand> exprBuilder;
    private Expression<String> exprName;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprBuilder = (Expression<SlashCommand>) exprs[0];
        exprName = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        SlashCommand[] builder = exprBuilder.getArray(e);
        String name = exprName.getSingle(e);
        if (name == null) return;
        JDA bot = BotManager.getBot(name);
        if (bot == null) return;
        List<CommandData> cmd = new ArrayList<>();
        for (SlashCommand c : builder) {
            cmd.add(c.build());
            if (c.build() == null) return;
        }
        DiSky
                .getInstance()
                .getLogger()
                .warning("A Slash Command just has been requested to Discord. Don't forget it can take an hour to be updated!");
        bot.updateCommands()
                .addCommands(cmd)
                .queue();
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "create command using builder " + exprBuilder.toString(e, debug) + " with bot " + exprName.toString(e, debug);
    }

}
