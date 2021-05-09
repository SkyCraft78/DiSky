package info.itsthesky.disky.managers;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import info.itsthesky.disky.DiSky;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;

/**
 * Class which handle all webhooks actions.
 * @author ItsTheSky
 */
public class WebhookManager {

    private final static DiSky instance = DiSky.getInstance();

    public static HashMap<String, WebhookClient> webhooks = new HashMap<>();

    /**
     * Register a new webhook client using a specific ID and an input (URL / Token).
     * @param id The webhook's ID
     * @param input The URL or the Token of the webhook
     */
    public static void registerWebhooks(final @NotNull String id, final @NotNull String input) {
        if (webhooks.containsKey(id)) {
            instance.getLogger().warning("You're trying to register a new webhook named " + id +" but this name already exist!");
            return;
        }
        WebhookClientBuilder builder = new WebhookClientBuilder(input);
        builder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("Webhooks");
            thread.setDaemon(true);
            return thread;
        });
        builder.setWait(true);
        WebhookClient client = builder.build();
        webhooks.put(id, client);
    }

    /**
     * Retrieve a webhook client according to the ID inputted.
     * @param id The webhook's ID
     * @return The webhook client
     */
    @Nullable
    public static WebhookClient getClient(final @NotNull String id) {
        if (!webhooks.containsKey(id)) {
            instance.getLogger().warning("You're trying to get a webhook with id " +id+ " however it doesn't exist! You need to register them before.");
            return null;
        }
        return webhooks.get(id);
    }

}
