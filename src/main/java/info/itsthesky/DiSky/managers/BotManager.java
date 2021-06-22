package info.itsthesky.disky.managers;

import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.managers.cache.InviteTracker;
import info.itsthesky.disky.managers.cache.Messages;
import info.itsthesky.disky.skript.commands.CommandListener;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.BotBuilder;
import info.itsthesky.disky.tools.object.MessageUpdater;
import info.itsthesky.disky.tools.section.WaiterListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.security.auth.login.LoginException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

/**
 * Main bot manager of DiSky
 * @author ItsTheSky
 */
public class BotManager {

    private static final Logger logger = DiSky.getInstance().getLogger();
    public static final HashMap<String, JDA> bots = new HashMap<>();
    public static final HashMap<JDA, String> prefixes = new HashMap<>();
    public static final List<ListenerAdapter> customListener = new ArrayList<>();

    public static HashMap<String, JDA> getBots() { return bots; }

    /**
     * Change the default prefixes of a bot. Use null to clear it.
     * @param prefix The prefix, or null to clear it.
     * @param bot The JDA instance of the target bot
     */
    public static void setDefaultPrefixes(@NotNull final JDA bot, @Nullable final String prefix) {
        if (!bots.containsValue(bot)) return;
        if (prefix == null) {
            if (!prefixes.containsKey(bot)) return;
            prefixes.remove(bot);
        } else prefixes.put(bot, prefix);
    }

    /**
     * Register a new bot with specific name in the bots list.
     * Load the JDA instance linked to it via the token.
     * @param name The bot name (= id)
     * @param token The token to connect to
     * @param builder The {@link BotBuilder} related to use as bot base
     */
    public static void addBot(final String name, final String token, final BotBuilder builder) {
        if (bots.containsKey(name)) {
            logger.warning("The bot named '"+name+"' is already loaded on the server! Shut it down or change the name.");
            return;
        }

        JDA jda;
        try {
            JDABuilder builder1 = JDABuilder.createDefault(token)
                    //.addEventListeners(new JDAListener())
                    .addEventListeners(new Messages())
                    //.addEventListeners(new EditedMessages())
                    .addEventListeners(new Utils())
                    .addEventListeners(new CommandListener())
                    .addEventListeners(new InviteTracker())
                    .addEventListeners(new WaiterListener())
                    .addEventListeners(new MessageUpdater())
                    .addEventListeners(customListener.toArray(new Object[0]))
                    .setChunkingFilter(ChunkingFilter.NONE)
                    .setMemberCachePolicy(MemberCachePolicy.ALL);
            builder.getIntents().forEach(((intent, enabled) -> {
                if (enabled) builder1.enableIntents(intent);
            }));
            builder.getCache().forEach(((flag, enabled) -> {
                if (enabled) builder1.enableCache(flag);
            }));
            jda = builder1.build();
        } catch (LoginException e) {
            e.printStackTrace();
            logger.severe("Can't load the bot named '"+name+"', see error above for more information.");
            return;
        }

        bots.put(name, jda);
        logger.info("The bot named '"+name+"' seems to be loaded correctly!");
        //Utils.sync(() -> DiSky.getInstance().getServer().getPluginManager().callEvent(new EventBotConnect(jda)));
    }

    /**
     * Get all JDA instance of the current bot hashmap.
     * @return Set of JDA instance
     */
    public static Set<JDA> getBotsJDA() {
        Set<JDA> set = new HashSet<>();
        bots.forEach((name, jda) -> set.add(jda));
        return set;
    }

    /**
     * Shutdown all JDA instance of all loaded bots, and clear the bots list.
     */
    public static void clearBots() {
        bots.forEach((name, jda) -> {
            jda.shutdown();
            logger.warning("The bot '"+name+"' has been disconnected!");
        });
        bots.clear();
    }

    /**
     * Get the first bot loaded, to use it in non-bot syntaxes.
     * @return The instance of the first bot, else null
     */
    public static JDA getFirstBot() {
        return Arrays.asList(bots.values().toArray(new JDA[0])).get(0);
    }

    /**
     * Get the first name of the first bot loaded.
     * @return The instance of the first bot, else null
     */
    public static String getFirstBotName() {
        AtomicReference<String> r = new AtomicReference<>();
        bots.forEach((name, jda) -> r.set(name));
        return r.get();
    }

    /**
     * If into the bots list, shutdown the JDA instance and remove the bot name from the bots list.
     * @param name The name (= id) of the bot
     */
    public static void removeAndShutdown(final String name) {
        if (!bots.containsKey(name)) {
            logger.warning("The bot named '"+name+"' is not loaded / already shut down!");
            return;
        }

        final JDA jda = bots.get(name);
        jda.shutdown();
        logger.warning("The bot '"+name+"' has been disconnected!");
        bots.remove(name);
    }

    /**
     * Get a bot name via its JDA.
     * Mainly used in discord command factory
     * @param target The bot's JDA instance
     * @return The name of bot via the JDA
     */
    public static @Nullable String getNameByJDA(final JDA target) {
        if (!bots.containsValue(target)) return null;
        for (Map.Entry<String, JDA> entry : bots.entrySet()) {
            String id = entry.getKey();
            JDA jda = entry.getValue();
            if (jda.equals(target)) return id;
        }
        return null;
    }

    /**
     * Get, if loaded, the JDA instance of a bot from its name (returns null if the bot isn't loaded)
     * @param name The name (= id) of the wanted bot
     * @param skipWarning If we alert the user if the bot doesn't exist
     * @return The JDA instance of the specific bot
     */
    public static JDA getBot(final String name, final boolean skipWarning) {
        if (!bots.containsKey(name) && !skipWarning) {
            logger.warning("The bot named '"+name+"' is not loaded, but you're trying to use it in an effect / expression!");
            return null;
        }
        return bots.get(name);
    }

    /**
     * Same as {@link BotManager#getBot(String, boolean)} but doesn't specify a skip warning boolean.
     * @param name The name (= id) of the wanted bot
     * @return The JDA instance of the specific bot
     */
    public static JDA getBot(final String name) {
        return getBot(name, false);
    }
}
