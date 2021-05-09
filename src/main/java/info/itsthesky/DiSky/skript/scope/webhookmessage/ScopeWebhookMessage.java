package info.itsthesky.disky.skript.scope.webhookmessage;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import info.itsthesky.disky.tools.EffectSection;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Webhook Message Builder")
@Description("Webhook Message can have multiple embed, text content and different appearance than the original webhook.")
@Since("1.8")
public class ScopeWebhookMessage extends EffectSection {

    public static WebhookMessageBuilder lastBuilder;

    static {
        Skript.registerCondition(ScopeWebhookMessage.class, "make [new] [discord] webhook message");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (checkIfCondition()) {
            return false;
        }
        if (!hasSection()) {
            return false;
        }
        loadSection(true);
        return true;
    }

    @Override
    protected void execute(Event e) {
        lastBuilder = new WebhookMessageBuilder();
        runSection(e);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "make new webhook message";
    }

}
