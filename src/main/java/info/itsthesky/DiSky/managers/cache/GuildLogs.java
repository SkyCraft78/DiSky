package info.itsthesky.disky.managers.cache;

import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

/**
 * Instead of retrieving logs everytime for almost each events, it's better to have a cache system for that.
 *
 * This is why i made that class, which handle a little the performance of the thread-blocking
 */
public class GuildLogs extends ListenerAdapter {

    public static final HashMap<Long, List<AuditLogEntry>> LOGS_STORAGE = new HashMap<>();

    @Override
    public void onReady(@NotNull ReadyEvent e) {
        for (Guild guild : e.getJDA().getGuilds())
            tryLogsCache(guild);
    }

    private void tryLogsCache(Guild guild) {
        Utils.async(() -> {
            long before = System.currentTimeMillis();
            if (Utils.INFO_CACHE)
                DiSky.getInstance().getLogger().info("Starting cache of logs for guild " + guild.getName() + " ...");
            guild.retrieveAuditLogs().queue(
                    logs -> {
                        LOGS_STORAGE.put(guild.getIdLong(), logs);
                        if (Utils.INFO_CACHE)
                            DiSky.getInstance().getLogger().info("Finished caching logs for guild" + guild.getName() + "! Took" + (before - System.currentTimeMillis()) + "ms !");
                    },
                    ex -> {
                        if (Utils.INFO_CACHE)
                            DiSky.getInstance().getLogger().severe("DiSky cannot cache logs for guild " + guild.getName() + " because of an internal error: " + ex.getMessage());
                    });
        });
    }

    // Since there's no logs event, I need to make a runnable for that :')
    // And since a runnable will be a lot less performant than the normal .complete way, I'll use it :'(
}
