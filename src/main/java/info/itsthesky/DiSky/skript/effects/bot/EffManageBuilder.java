package info.itsthesky.disky.skript.effects.bot;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.skript.scope.ScopeBotBuilder;
import info.itsthesky.disky.tools.AsyncEffect;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.EffectSection;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.bukkit.event.Event;

@Name("Enable Intent")
@Description({"Enable specific intent for the current bot in a 'create discord bot' scope. The login effect MUST be in the scope to take the desired configuration.",
"You can also enable (or disable) every default intent needed for a bot, and if you can enable both intent on the discord developer portal."})
@Examples("on load:\n" +
        "    make new discord bot:\n" +
        "        enable guild bans intent\n" +
        "        enable guild emojis intent\n" +
        "        enable guild webhooks intent\n" +
        "        enable guild invites intent\n" +
        "        enable guild voice states intent\n" +
        "        enable guild message reactions intent\n" +
        "        enable guild message typing intent\n" +
        "        enable direct messages intent\n" +
        "        enable guild messages intent\n" +
        "        enable guild members intent\n" +
        "        enable guild presences intent\n" +
        "        login to \"bot token\" with name \"bot name\"")
@Since("2.0")
public class EffManageBuilder extends AsyncEffect {

    static {
        Skript.registerEffect(EffManageBuilder.class,
                "(enable|disable) [intent] %intents% [intent]",
                "(enable|disable) [all] default intent[s]");
    }

    private final GatewayIntent[] defaults = new GatewayIntent[] {
            GatewayIntent.GUILD_BANS,
            GatewayIntent.GUILD_EMOJIS,
            GatewayIntent.GUILD_WEBHOOKS,
            GatewayIntent.GUILD_INVITES,
            GatewayIntent.GUILD_VOICE_STATES,
            GatewayIntent.GUILD_MESSAGE_REACTIONS,
            GatewayIntent.GUILD_MESSAGE_TYPING,
            GatewayIntent.DIRECT_MESSAGES,
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.GUILD_PRESENCES
    };

    private Expression<GatewayIntent> exprIntent;
    private boolean enable;
    private boolean defaultIntents;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!EffectSection.isCurrentSection(ScopeBotBuilder.class)) {
            Skript.error("The 'enable intent' effect can only be used in a create discord bot scope!");
            return false;
        }
        defaultIntents = matchedPattern == 1;
        enable = parseResult.expr.startsWith("enable");
        if (!defaultIntents) exprIntent = (Expression<GatewayIntent>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            GatewayIntent[] intents = defaultIntents ? defaults : exprIntent.getArray(e);
            if (intents == null) return;

            for (GatewayIntent intent : intents) {
                if (enable) {
                    ScopeBotBuilder.lastBuilder.enableIntents(intent);

                    switch (intent) {
                        case GUILD_MEMBERS:
                            ScopeBotBuilder.lastBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
                            break;
                        case GUILD_PRESENCES:
                            ScopeBotBuilder.lastBuilder.enableCache(CacheFlag.CLIENT_STATUS);
                            ScopeBotBuilder.lastBuilder.enableCache(CacheFlag.ACTIVITY);
                            break;
                        case GUILD_VOICE_STATES:
                            ScopeBotBuilder.lastBuilder.enableCache(CacheFlag.VOICE_STATE);
                            break;
                        case GUILD_EMOJIS:
                            ScopeBotBuilder.lastBuilder.enableCache(CacheFlag.EMOTE);
                            break;
                    }
                } else {
                    ScopeBotBuilder.lastBuilder.disableIntents(intent);

                    switch (intent) {
                        case GUILD_MEMBERS:
                            ScopeBotBuilder.lastBuilder.setMemberCachePolicy(MemberCachePolicy.DEFAULT);
                            break;
                        case GUILD_PRESENCES:
                            ScopeBotBuilder.lastBuilder.disableCache(CacheFlag.CLIENT_STATUS);
                            ScopeBotBuilder.lastBuilder.disableCache(CacheFlag.ACTIVITY);
                            break;
                        case GUILD_VOICE_STATES:
                            ScopeBotBuilder.lastBuilder.disableCache(CacheFlag.VOICE_STATE);
                            break;
                        case GUILD_EMOJIS:
                            ScopeBotBuilder.lastBuilder.disableCache(CacheFlag.EMOTE);
                            break;
                    }
                }
            }
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "enable " + exprIntent + " intent";
    }

}
