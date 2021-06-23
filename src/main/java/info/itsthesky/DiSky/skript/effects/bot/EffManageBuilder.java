package info.itsthesky.disky.skript.effects.bot;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.skript.scope.bot.ScopeBotBuilder;
import info.itsthesky.disky.tools.AsyncEffect;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.EffectSection;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.bukkit.event.Event;

@Name("Enable Intent")
@Description("Enable specific intent for the current bot in a 'create discord bot' scope.")
@Examples("on load:\n" +
        "    make new discord bot:\n" +
        "        disable guild presences intent for bot builder\n" +
        "        disable guild members intent for bot builder\n" +
        "        disable activity cache for bot builder\n" +
        "        disable client status cache for bot builder\n" +
        "        login to \"TOKEN\" using base bot builder with the name \"NAME\"")
@Since("2.0")
public class EffManageBuilder extends AsyncEffect {

    static {
        Skript.registerEffect(EffManageBuilder.class,
                "enable [intent] %intent% [intent]");
    }

    private Expression<GatewayIntent> exprIntent;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!EffectSection.isCurrentSection(ScopeBotBuilder.class)) {
            Skript.error("The 'enable intent' effect can only be used in a create discord bot scope!");
            return false;
        }
        exprIntent = (Expression<GatewayIntent>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            GatewayIntent intent = exprIntent.getSingle(e);
            if (intent == null) return;
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
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "enable " + exprIntent + " intent";
    }

}
