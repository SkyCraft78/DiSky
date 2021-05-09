package info.itsthesky.disky.skript.scope.webhookmessage;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Last Webhook Message Builder")
@Description("Return the last used webhook message builder.")
@Since("1.8")
public class ExprLastWebhookMessageBuilder extends SimpleExpression<WebhookMessageBuilder> {

    static {
        Skript.registerExpression(ExprLastWebhookMessageBuilder.class, WebhookMessageBuilder.class, ExpressionType.SIMPLE,
                "[the] [last] [(generated|created)] webhook [(msg|message)] [builder]"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Nullable
    @Override
    protected WebhookMessageBuilder[] get(Event e) {
        return new WebhookMessageBuilder[]{ScopeWebhookMessage.lastBuilder};
    }

    @Override
    public Class<? extends WebhookMessageBuilder> getReturnType() {
        return WebhookMessageBuilder.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the last webhook message builder";
    }
}
