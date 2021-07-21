package info.itsthesky.disky.skript.effects.webhooks;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky.tools.async.AsyncEffect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.managers.WebhookManager;
import info.itsthesky.disky.tools.Utils;
import org.bukkit.event.Event;

@Name("Register Webhooks")
@Description("Register a new webhook client, using a specific ID and its url or token.")
@Examples("register webhook \"name\" using url \"url\"")
@Since("1.8")
public class EffRegisterWebhook extends AsyncEffect {

    static {
        Skript.registerEffect(EffRegisterWebhook.class, // [the] [bot] [(named|with name)] %string%
                "["+ Utils.getPrefixName() +"] register [new] [webhook] [(with name|named)] %string% (using|with) [the] [(url|token|id)] %string%");
    }

    private Expression<String> exprName;
    private Expression<String> exprURL;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprName = (Expression<String>) exprs[0];
        exprURL = (Expression<String>) exprs[1];
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void execute(Event e) {
        String name = exprName.getSingle(e);
        String url = exprURL.getSingle(e);
        if (name == null || url == null) return;
        WebhookManager.registerWebhooks(name, url);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "register webhook named " + exprName.toString(e, debug) + " using url " + exprURL.toString(e, debug);
    }

}
