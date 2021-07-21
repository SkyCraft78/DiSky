package info.itsthesky.disky.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky.tools.async.AsyncEffect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.managers.EmbedManager;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.event.Event;

@Name("Register Embed Template")
@Description("Register a new embed template using an existing template and an id. See also 'make embed using template \"ID\"' section.")
@Since("1.7")
public class EffRegisterEmbed extends AsyncEffect {

    static {
        Skript.registerEffect(EffRegisterEmbed.class,
                "["+ Utils.getPrefixName() +"] register [new] [embed] template with [the] [embed] %embed% [and] with [the] (name|id) %string%");
    }

    private Expression<String> exprID;
    private Expression<EmbedBuilder> exprEmbed;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprEmbed = (Expression<EmbedBuilder>) exprs[0];
        exprID = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        String id = exprID.getSingle(e);
        EmbedBuilder builder = exprEmbed.getSingle(e);
        if (id == null || builder == null) return;
        EmbedManager.registerTemplate(id, builder);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "register new embed template from " + exprEmbed.toString(e, debug) + " with id " + exprID.toString(e, debug);
    }

}
