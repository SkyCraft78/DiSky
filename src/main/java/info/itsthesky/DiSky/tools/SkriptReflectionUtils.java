package info.itsthesky.disky.tools;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/**
 * Bypass the fact Skript doesn't accept registration by enabling it again.
 * @author ItsTheSky
 */
public final class SkriptReflectionUtils {

    public static boolean openRegistration() {
        return toggle(true);
    }

    public static boolean closeRegistration() {
        return toggle(false);
    }

    private static boolean toggle(boolean state) {
        try {
            ReflectionUtils.setFieldValue(
                    ReflectionUtils.getField(
                            Skript.class,
                            "acceptRegistrations"
                    ),
                    state
            );
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public static void removeAddon(String name) {
        try {
            Field addonsHashmap = ReflectionUtils.getField(Skript.class, "addons");
            ((HashMap<String, SkriptAddon>) addonsHashmap.get(null)).remove(name);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void removeAddon(JavaPlugin plugin) {
        removeAddon(plugin.getName());
    }

}
