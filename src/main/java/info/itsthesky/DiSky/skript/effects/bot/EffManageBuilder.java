package info.itsthesky.disky.skript.effects.bot;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.AsyncEffect;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.BotBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.bukkit.event.Event;

@Name("Change Cache & Intents of Bot Builder")
@Description("Enable or disable either intents or cache for a bot builder. Check 'intent' and 'cache' type for more information.")
@Examples("enable member voice state intent for ")
@Since("1.14")
public class EffManageBuilder extends AsyncEffect {

    static {
        Skript.registerEffect(EffManageBuilder.class,
                "(enable|disable) %intent/cache% [(intent|cache)] for [the] [builder] %botbuilder%");
    }

    private Expression<Object> exprChange;
    private Expression<BotBuilder> exprBuilder;
    private boolean enable = false;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprChange = (Expression<Object>) exprs[0];
        exprBuilder = (Expression<BotBuilder>) exprs[1];
        enable = parseResult.expr.startsWith("enable");
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            Object change = exprChange.getSingle(e);
            BotBuilder builder = exprBuilder.getSingle(e);
            if (change == null || builder == null) return;

            if (change instanceof GatewayIntent) builder.changeIntents((GatewayIntent) change, enable);
            if (change instanceof CacheFlag) builder.changeCache((CacheFlag) change, enable);
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return (enable ? "enable " : "disable ") + exprChange.toString(e, debug) + " for bot builder " + exprBuilder.toString(e, debug);
    }

}
