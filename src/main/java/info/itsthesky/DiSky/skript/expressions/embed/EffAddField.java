package info.itsthesky.disky.skript.expressions.embed;

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
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.event.Event;

@Name("Add Embed Field")
@Description("Add new field with name and value to embed's fields.")
@Examples("add inline field named \"Field name\" with value \"And epic description <3\" to fields of embed")
@Since("1.0")
public class EffAddField extends Effect {

    static {
        Skript.registerEffect(EffAddField.class,
                "["+ Utils.getPrefixName() +"] add field (named|with name) %string% [and] with [the] value %string% to [fields of] %embed%",
                "["+ Utils.getPrefixName() +"] add inline field (named|with name) %string% [and] with [the] value %string% to [fields of] %embed%");
    }

    private Expression<String> exprName;
    private Expression<String> exprDesc;
    private Expression<EmbedBuilder> exprEmbed;
    private boolean isInline;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprName = (Expression<String>) exprs[0];
        exprDesc = (Expression<String>) exprs[1];
        exprEmbed = (Expression<EmbedBuilder>) exprs[2];
        isInline = matchedPattern != 0;
        return true;
    }

    @Override
    protected void execute(Event e) {
        String name = exprName.getSingle(e);
        String desc = exprDesc.getSingle(e);
        EmbedBuilder embed = exprEmbed.getSingle(e);
        if (name == null || desc == null || embed == null) return;

        if (name.length() > 256) {
            DiSky.getInstance().getLogger()
                    .warning("The title of a field cannot be bigger than 256 characters. The one you're trying to set is '"+name.length()+"' length!");
            return;
        }
        if (desc.length() > 1024) {
            DiSky.getInstance().getLogger()
                    .warning("The value of a field cannot be bigger than 1024 characters. The one you're trying to set is '"+desc.length()+"' length!");
            return;
        }

        embed.addField(name, desc, isInline);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "add field with named " + exprName.toString(e, debug) + " with description " + exprDesc.toString(e, debug) + " to fields of " + exprEmbed.toString(e, debug);
    }

}
