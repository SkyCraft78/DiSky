package info.itsthesky.disky.skript.expressions.scope.commands;

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
import info.itsthesky.disky.tools.NodeInformation;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.SlashCommand;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.utils.Checks;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Name("Add Slash Command Option")
@Description("Add an (require or not) option to a specific command builder. See OptionType for possible sort of input!")
@Examples({"register require option USER with id \"user\" and description \"The 'USER' word is an option type.\" to command",
        "register option STRING with id \"text\" and description \"This option is not require\" to command"})
@Since("1.5")
public class EffAddOption extends Effect {

    static {
        Skript.registerEffect(EffAddOption.class,
                "[" + Utils.getPrefixName() + "] register option [with type] %optiontype% with (id|name) %string% [[and] with pre( |-)defined value[s] %-objects%] [and] [with] [(desc|description)] %string% to [command] [builder] %commandbuilder%",
                "[" + Utils.getPrefixName() + "] register require option [with type] %optiontype% with (id|name) %string% [[and] with pre( |-)defined value[s] %-objects%] [and] [with] [(desc|description)] %string% to [command] [builder] %commandbuilder%"
        );
    }

    private boolean isRequire;
    private Expression<OptionType> exprType;
    private Expression<String> exprName;
    private Expression<Object> exprPredifValues;
    private Expression<String> exprDesc;
    private Expression<SlashCommand> exprBuilder;
    private NodeInformation information;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprType = (Expression<OptionType>) exprs[0];
        exprName = (Expression<String>) exprs[1];
        exprDesc = (Expression<String>) exprs[3];
        exprBuilder = (Expression<SlashCommand>) exprs[4];
        exprPredifValues = (Expression<Object>) exprs[2];
        isRequire = matchedPattern != 0;
        information = new NodeInformation();
        return true;
    }

    @Override
    protected void execute(Event e) {
        OptionType type = exprType.getSingle(e);
        String name = exprName.getSingle(e);
        String desc = exprDesc.getSingle(e);
        Object[] predifValues = Utils.verifyVars(e, exprPredifValues);

        boolean acceptChoices = type.equals(OptionType.STRING) || type.equals(OptionType.INTEGER);
        if (exprPredifValues != null && !(acceptChoices)) {
            DiSky.error("You cannot have predefined value for the " + type.name().toLowerCase(Locale.ROOT) + " type! It only support STRING and INTEGER!");
            return;
        }

        SlashCommand builder = exprBuilder.getSingle(e);
        if (name == null || desc == null || type == null || builder == null) return;
        if (!Checks.ALPHANUMERIC_WITH_DASH.matcher(name).find()) {
            DiSky.error("The specified ID in an option of slash command cannot have any space! " + information.getDebugLabel());
            return;
        }
        final List<Command.Choice> choices = new ArrayList<>();
        for (String s : Utils.valuesToString(predifValues))
            choices.add(new Command.Choice(s, safeID(s)));

        OptionData data = new OptionData(type, name, desc).setRequired(isRequire);
        if (acceptChoices)
            data = data.addChoices(choices);

        builder.addOption(data);
    }

    private String safeID(String input) {
        return input
                .toLowerCase(Locale.ROOT)
                .replaceAll(" ", "_");
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "add option with type " + exprType.toString(e, debug) + " named " + exprName.toString(e, debug) + " with description " + exprDesc.toString(e, debug) + " to builder " + exprBuilder.toString(e, debug);
    }

}
