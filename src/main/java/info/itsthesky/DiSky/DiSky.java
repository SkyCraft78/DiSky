package info.itsthesky.disky;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import info.itsthesky.disky.managers.BotManager;
import info.itsthesky.disky.managers.music.AudioUtils;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.Metrics;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.versions.V2_3;
import info.itsthesky.disky.tools.versions.V2_4;
import info.itsthesky.disky.tools.versions.VersionAdapter;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.RestAction;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DiSky extends JavaPlugin {

    public static final String CONFIG = "config.yml";
    /* Initialisation */
    private static DiSky instance;
    private Logger logger;
    private static PluginManager pluginManager;
    private static VersionAdapter SKRIPT_ADAPTER;
    public static GatewayIntent[] intents = new GatewayIntent[0];

    @Override
    public void onEnable() {

        /* Variable loading */
        instance = this;
        logger = getLogger();
        pluginManager = getServer().getPluginManager();

        /* JDA Check */
        try {
            Class.forName("net.dv8tion.jda.api.JDAInfo");
        } catch (ClassNotFoundException ignored) { }
        String customVersion = "";
        try {
            customVersion = Utils.getFieldValue("net.dv8tion.jda.api.JDAInfo.VERSION").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!customVersion.equalsIgnoreCase(JDAInfo.VERSION)) {
            getLogger().severe("DiSky found another JDA version installed on the Server!");
            getLogger().severe("Search over your plugins if none of them are already using the JDA.");
            getLogger().severe("Version found: " + customVersion + " Version needed: " + JDAInfo.VERSION);
            getLogger().severe("Plugins such as DiscordSRV or Vixio can be the cause of that problem!");
            pluginManager.disablePlugin(this);
            return;
        }

        RestAction.setDefaultFailure(DiSkyErrorHandler::logException);

        /* Skript loading */
        getServer().getConsoleSender().sendMessage(Utils.colored("&bDiSky &9is loading ..."));
        if ((pluginManager.getPlugin("Skript") != null) && Skript.isAcceptRegistrations()) {
            getServer().getConsoleSender().sendMessage(Utils.colored("&aSkript found! &6Starting registration ..."));
            SkriptAddon addon = Skript.registerAddon(this);
            try {
                addon.loadClasses("info.itsthesky.disky.skript");
            } catch (IOException e) {
                Skript.error("Wait, this is anormal. Please report this error on GitHub.");
                e.printStackTrace();
            }
        } else {
            Skript.error("Skript isn't installed or doesn't accept registrations.");
            pluginManager.disablePlugin(this);
        }
        Utils.saveResourceAs(CONFIG);
        File file = new File(getDataFolder(), CONFIG);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<GatewayIntent> gatewayIntents = new ArrayList<>();
        for (GatewayIntent intent : GatewayIntent.values()) {
            if (Utils.getOrSetDefault(CONFIG, "Intents." + intent.name(), true))
                gatewayIntents.add(intent);
        }
        intents = gatewayIntents.toArray(new GatewayIntent[0]);

        /* Skript color adapter */
        boolean usesSkript24 = (Skript.getVersion().getMajor() >= 3 || (Skript.getVersion().getMajor() == 2 && Skript.getVersion().getMinor() >= 4));
        SKRIPT_ADAPTER = usesSkript24 ? new V2_4() : new V2_3();
        if (!usesSkript24) logger.info("You're using an old version of Skript. Enable Color and Date adapter!");

        /* Metrics */
        int pluginId = 10911;
        Metrics metrics = new Metrics(this, pluginId);

        /* Audio system */
        AudioUtils.initializeAudio();

        getServer().getConsoleSender().sendMessage(Utils.colored("&aYou're using the &2JDA&a version &2" + JDAInfo.VERSION));

        getServer().getConsoleSender().sendMessage(Utils.colored("&bDiSky &9seems to be loaded correctly!"));
        getServer().getConsoleSender().sendMessage(Utils.colored("&9Join our &bDiscord &9for new update and support:"));
        getServer().getConsoleSender().sendMessage(Utils.colored("&bhttps://discord.gg/whWuXwaVwM"));
    }

    @Override
    public void onDisable() {
        /* Clients Shutdown */
        BotManager.clearBots();
    }

    public static DiSky getInstance() { return instance; }
    public static PluginManager getPluginManager() { return pluginManager; }

    public static VersionAdapter getSkriptAdapter() {
        boolean usesSkript24 = (Skript.getVersion().getMajor() >= 3 || (Skript.getVersion().getMajor() == 2 && Skript.getVersion().getMinor() >= 4));
        if (SKRIPT_ADAPTER == null)
            SKRIPT_ADAPTER = usesSkript24 ? new V2_4() : new V2_3();
        return SKRIPT_ADAPTER;
    }

    @NotNull
    public Logger getConsoleLogger() {
        return logger;
    }
}
