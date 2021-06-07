package info.itsthesky.disky.skript.events.skript.bot;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.managers.BotManager;
import info.itsthesky.disky.skript.events.util.DiSkyEvent;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventBotLeave extends Event {

    static {
        // [seen by [bot] [(named|with name)]%string%]
        Skript.registerEvent("Bot Leave", SimpleEvent.class, EventBotLeave.class, "[discord] bot leave (guild|server)")
                .description("Fired when a bot leave any guild where the bot is in.")
                .examples("on bot leave guild:")
                .since("1.13");

        BotManager.customListener.add(new DiSkyEvent<>(GuildLeaveEvent.class, e ->
                Utils.sync(() -> Bukkit.getServer().getPluginManager().callEvent(new EventBotLeave(e)))));

        EventValues.registerEventValue(EventBotLeave.class, JDA.class, new Getter<JDA, EventBotLeave>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventBotLeave event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventBotLeave.class, Guild.class, new Getter<Guild, EventBotLeave>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventBotLeave event) {
                return event.getEvent().getGuild();
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final GuildLeaveEvent event;

    public EventBotLeave(
            final GuildLeaveEvent event) {
        super(Utils.areEventAsync());
        this.event = event;
    }

    public GuildLeaveEvent getEvent() {
        return event;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}