package info.itsthesky.disky.skript.effects.webhooks;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky.tools.AsyncEffect;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import info.itsthesky.disky.managers.WebhookManager;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.event.Event;

@Name("Make Webhook Talk")
@Description("Make a webhook send a specific message, embed or webhook message builder in his channel.\nSee more information about this on the wiki: https://github.com/SkyCraft78/DiSky/wiki/Webhooks")
@Examples("make webhook \"name\" send message \"Hello World !\"")
@Since("1.8")
public class EffMakeWebhookTalk extends AsyncEffect {

    static {
        Skript.registerEffect(EffMakeWebhookTalk.class, // [the] [bot] [(named|with name)] %string%
                "["+ Utils.getPrefixName() +"] make [the] [webhook] [(named|with name)] %string% send [the] [(message|text|webhook message)] %string/webhookmessagebuilder/embed%");
    }

    private Expression<String> exprName;
    private Expression<Object> exprMessage;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprName = (Expression<String>) exprs[0];
        exprMessage = (Expression<Object>) exprs[1];
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void execute(Event e) {
        String name = exprName.getSingle(e);
        Object entity = exprMessage.getSingle(e);
        if (name == null || entity == null) return;

        WebhookClient client = WebhookManager.getClient(name);
        if (client == null || client.isShutdown()) return;

        if (entity instanceof WebhookMessageBuilder) {
            client.send(((WebhookMessageBuilder) entity).build());
        } else if (entity instanceof EmbedBuilder) {
            client.send(WebhookEmbedBuilder.fromJDA(((EmbedBuilder) entity).build()).build());
        } else {
            client.send(entity.toString());
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "make webhook " + exprName.toString(e, debug) + " send message " + exprMessage.toString(e, debug);
    }

}
