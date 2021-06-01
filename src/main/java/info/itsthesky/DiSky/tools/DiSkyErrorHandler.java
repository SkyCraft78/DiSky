package info.itsthesky.disky.tools;

import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.skript.events.skript.EventDiSkyException;
import info.itsthesky.disky.skript.expressions.ExprLastDiSkyError;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.bukkit.event.Event;

import java.util.function.Consumer;
import java.util.logging.Logger;

public class DiSkyErrorHandler {

    private final static Logger logger = DiSky.getInstance().getLogger();

    public static void executeHandleCode(Event e, Consumer<Event> consumer) {
        try {
            consumer.accept(e);
        } catch (Exception ex) {
            logException(ex);
        }
    }

    public static void logException(Throwable ex) {
        ExprLastDiSkyError.lastError = ex.getMessage();
        Utils.sync(() -> DiSky.getInstance().getServer().getPluginManager().callEvent(new EventDiSkyException(ex.getMessage())));
        if (ex instanceof InsufficientPermissionException) {

            InsufficientPermissionException exception = (InsufficientPermissionException) ex;
            if (Utils.getOrSetDefault("config.yml", "HandleErrors.InsufficientPermissionException", false)) ex.printStackTrace();
            logger.warning("DiSky tried to do an action on Discord, but doesn't have the " + exception.getPermission().getName() + " permission!");

        } else if (ex instanceof NullPointerException) {

            NullPointerException exception = (NullPointerException) ex;
            if (Utils.getOrSetDefault("config.yml", "HandleErrors.NullPointerException", false))
                exception.printStackTrace();
            logger.warning("DiSky tried to do an action on Discord, but got a NullPointerException! Check if all your value are set!");

        } else if (ex instanceof IllegalStateException) {

            if (ex.getMessage().equalsIgnoreCase("Cannot build an empty embed!")) {
                logger.warning("You're trying to build an embed, however, you don't set any title, author of description! (The embed is empty)");
            }

        } else if (ex instanceof ErrorResponseException) {

            ErrorResponseException exception = (ErrorResponseException) ex;
            if (exception.getErrorCode() == 10008) logger.severe("The message specified in a DiSky effect / expression / event doesn't exist on discord!");

        }
    }

}
