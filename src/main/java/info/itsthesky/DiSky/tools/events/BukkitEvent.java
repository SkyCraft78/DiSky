package info.itsthesky.disky.tools.events;

import org.bukkit.event.HandlerList;

/**
 * Made by Blitz, minor edit by Sky for DiSky
 */
public class BukkitEvent extends org.bukkit.event.Event {

    private static final HandlerList handlerList = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}