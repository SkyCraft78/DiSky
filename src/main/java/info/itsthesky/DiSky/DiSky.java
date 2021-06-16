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
import org.bukkit.Bukkit;
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

        log("===== Starting DiSky v" + getDescription().getVersion() + " ... =====");

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
            error("DiSky found another JDA version installed on the Server!");
            error("Search over your plugins if none of them are already using the JDA.");
            error("Version found: " + customVersion + " Version needed: " + JDAInfo.VERSION);
            error("Plugins such as DiscordSRV or Vixio can be the cause of that problem!");
            pluginManager.disablePlugin(this);
            return;
        }

        RestAction.setDefaultFailure(DiSkyErrorHandler::logException);

        /* Skript loading */
        log("Skript detection ...");
        if ((pluginManager.getPlugin("Skript") != null) && Skript.isAcceptRegistrations()) {
            success("Skript found! Starting registration ...");
            SkriptAddon addon = Skript.registerAddon(this);
            try {
                addon.loadClasses("info.itsthesky.disky.skript");
            } catch (IOException e) {
                e.printStackTrace();
                error("Wait, this is anormal. Please report the error above on the DiSky GitHub!.");
            }
        } else {
            error("Cannot hook into Skript (Skript is not loaded or doesn't accept registration)");
            pluginManager.disablePlugin(this);
        }
        Utils.saveResourceAs(CONFIG);

        /* Skript color adapter */
        boolean usesSkript24 = (Skript.getVersion().getMajor() >= 3 || (Skript.getVersion().getMajor() == 2 && Skript.getVersion().getMinor() >= 4));
        SKRIPT_ADAPTER = usesSkript24 ? new V2_4() : new V2_3();
        if (!usesSkript24) warn("You're using an old version of Skript. Enable Color and Date adapter!");

        /* Metrics */
        int pluginId = 10911;
        Metrics metrics = new Metrics(this, pluginId);

        /* Audio system */
        AudioUtils.initializeAudio();

        boolean containAlphaOrBeta =
                getDescription().getVersion().contains("alpha")  ||
                        getDescription().getVersion().contains("beta");
        String latestVersion = Utils.getLatestVersion();
        String currentVersion = getDescription().getVersion();
        if (!containAlphaOrBeta && !currentVersion.equals(latestVersion))
            warn("You are not on the latest released version of DiSky! You're on " + currentVersion + " but latest is " + latestVersion);

        if (containAlphaOrBeta)
            warn("You're using an alpha (or beta) version of DiSky. If you have bugs, report them to the discord!");

        log("JDA Version detected & used: &9" + JDAInfo.VERSION);
        log("DiSky Version: &9" + getDescription().getVersion());
        log("Discord: &9" + "https://discord.gg/whWuXwaVwM");
        log("GitHub: &9" + "https://github.com/SkyCraft78/DiSky/");
        log("===== DiSky seems to be started correctly! =====");
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

    public static void log(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(Utils.colored("&3[DiSky] &b" + message));
    }

    public static void error(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(Utils.colored("&4[DiSky] &c" + message));
    }

    public static void success(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(Utils.colored("&3[DiSky] &a" + message));
    }

    public static void warn(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(Utils.colored("&6[DiSky] &e" + message));
    }

    @NotNull
    public Logger getConsoleLogger() {
        return logger;
    }
}
