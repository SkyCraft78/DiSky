package info.itsthesky.disky;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import info.itsthesky.disky.managers.BotManager;
import info.itsthesky.disky.managers.music.AudioUtils;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.Metrics;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.versions.VSkript22;
import info.itsthesky.disky.tools.versions.VSkript23;
import info.itsthesky.disky.tools.versions.VSkript26;
import info.itsthesky.disky.tools.versions.VersionAdapter;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.requests.RestAction;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class DiSky extends JavaPlugin {

    public static final String CONFIG = "config.yml";
    /* Initialisation */
    private static DiSky instance;
    private Logger logger;
    private static PluginManager pluginManager;
    public static VersionAdapter SKRIPT_ADAPTER;

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

        SKRIPT_ADAPTER = SkriptUtils.SKRIPT_VERSION.getAdapter();
        switch (SkriptUtils.SKRIPT_VERSION) {
            case UNKNOWN:
                error("Cannot found your Skript version, defaulting to 2.3 for the adapter.");
                break;
            case SKRIPT_STABLE:
                success("Found Skript 2.3 ~ 2.5! Enabling scope adapter!");
                break;
            case SKRIPT_DEV:
                warn("Found an old version of Skript (2.2 or less)! Enabling color and date adapter!");
                break;
            case SKRIPT_17:
                warn("Found the 2.6 Skript version! Enabling delayed task adapter!");
                break;
        }

        /* Metrics */
        int pluginId = 10911;
        Metrics metrics = new Metrics(this, pluginId);
        // Author: Olyno
        metrics.addCustomChart(new Metrics.SimplePie("skript_version", () ->
                Bukkit.getServer().getPluginManager().getPlugin("Skript").getDescription().getVersion()));
        metrics.addCustomChart(new Metrics.DrilldownPie("java_version", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            String javaVersion = System.getProperty("java.version");
            Map<String, Integer> entry = new HashMap<>();
            entry.put(javaVersion, 1);
            if (javaVersion.startsWith("1.7")) {
                map.put("Java 1.7", entry);
            } else if (javaVersion.startsWith("1.8")) {
                map.put("Java 1.8", entry);
            } else if (javaVersion.startsWith("1.9")) {
                map.put("Java 1.9", entry);
            } else {
                map.put("Other", entry);
            }
            return map;
        }));

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
        log("Skript Version: &9" + Skript.getVersion());
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

    public final static class SkriptUtils {

        public static final SkriptAdapterVersion SKRIPT_VERSION;
        public static final boolean MANAGE_LOCALES;

        static {
            if (Skript.getVersion().getMinor() < 3) {
                SKRIPT_VERSION = SkriptAdapterVersion.SKRIPT_DEV;
            } else if (Skript.getVersion().getMinor() >= 3 && Skript.getVersion().getMinor() < 6) {
                SKRIPT_VERSION = SkriptAdapterVersion.SKRIPT_STABLE;
            } else if (Skript.getVersion().getMinor() >= 6) {
                SKRIPT_VERSION = SkriptAdapterVersion.SKRIPT_17;
            } else {
                SKRIPT_VERSION = SkriptAdapterVersion.UNKNOWN;
            }

            MANAGE_LOCALES = !SKRIPT_VERSION.equals(SkriptAdapterVersion.SKRIPT_DEV);
        }

        public enum SkriptAdapterVersion {
            /**
             * The weird and old version of Skript, aka every 2.2 (included) and less Skript version, which doesn't need any variables managin,g custom colors and more.
             */
            SKRIPT_DEV,
            /**
             * The best Skript version, include every of them from 2.3 to 2.5 (included), which make DiSky work as good as it can :p
             */
            SKRIPT_STABLE,
            /**
             * The new and unstable Skript version of Skript, which works on 1.17 with Java 16, aka 2.6 and more...
             */
            SKRIPT_17,
            /**
             * Cannot identify which version of Skript is loaded, so assume it's SKRIPT_STABLE.
             */
            UNKNOWN,
            ;
            public @NotNull VersionAdapter getAdapter() {
                if (this == null) return null;
                switch (this) {
                    case SKRIPT_DEV:
                        return new VSkript22();
                    case UNKNOWN:
                    case SKRIPT_STABLE:
                        return new VSkript23();
                    case SKRIPT_17:
                        return new VSkript26();
                    default:
                        return null;
                }
            }
        }
    }
}
