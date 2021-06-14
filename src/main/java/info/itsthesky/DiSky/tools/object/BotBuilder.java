package info.itsthesky.disky.tools.object;

import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.HashMap;

public class BotBuilder {

    private final HashMap<GatewayIntent, Boolean> INTENTS = new HashMap<>();
    private final HashMap<CacheFlag, Boolean> CACHE = new HashMap<>();

    public BotBuilder() {
        for (GatewayIntent intent : GatewayIntent.values()) INTENTS.put(intent, false);
        INTENTS.put(GatewayIntent.GUILD_MEMBERS, true);
        INTENTS.put(GatewayIntent.GUILD_BANS, true);
        INTENTS.put(GatewayIntent.GUILD_EMOJIS, true);
        INTENTS.put(GatewayIntent.GUILD_WEBHOOKS, true);
        INTENTS.put(GatewayIntent.GUILD_INVITES, true);
        INTENTS.put(GatewayIntent.GUILD_VOICE_STATES, true);
        INTENTS.put(GatewayIntent.GUILD_MESSAGE_REACTIONS, true);
        INTENTS.put(GatewayIntent.GUILD_MESSAGE_TYPING, true);
        INTENTS.put(GatewayIntent.DIRECT_MESSAGES, true);
        INTENTS.put(GatewayIntent.GUILD_PRESENCES, true);
        INTENTS.put(GatewayIntent.GUILD_MESSAGES, true);

        for (CacheFlag flag : CacheFlag.values()) CACHE.put(flag, false);
        CACHE.put(CacheFlag.ACTIVITY, true);
        CACHE.put(CacheFlag.VOICE_STATE, true);
        CACHE.put(CacheFlag.CLIENT_STATUS, true);
        CACHE.put(CacheFlag.ONLINE_STATUS, true);
    }

    public HashMap<GatewayIntent, Boolean> getIntents() {
        return INTENTS;
    }

    public void changeIntents(GatewayIntent intent, boolean value) {
        this.INTENTS.put(intent, value);
    }

    public void changeCache(CacheFlag flag, boolean value) {
        this.CACHE.put(flag, value);
    }

    public HashMap<CacheFlag, Boolean> getCache() {
        return CACHE;
    }

    @Override
    public String toString() {
        return "BotBuilder{" +
                "INTENTS=" + INTENTS +
                ", CACHE=" + CACHE +
                '}';
    }
}
