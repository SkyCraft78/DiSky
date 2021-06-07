package info.itsthesky.disky.skript.events.skript.bot;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Bot Join Guild")
@Description("Fired when a bot join any guild where the bot is in.")
@Examples({"on bot join guild:",
            "\tsend message \"I'm the better bot ever, made with DiSky by Sky!\" to text channel with id \"750449611302371469\""})
@Since("1.0")
public class EventBotJoin extends Event {

    static {
        // [seen by [bot] [(named|with name)]%string%]
        Skript.registerEvent("Bot Join", SimpleEvent.class, EventBotJoin.class, "[discord] bot join (guild|server)")
        .description("Fired when a bot join any guild where the bot is in.")
        .examples("on bot join guild:",
                "\tsend message \"I'm the better bot ever, made with DiSky by Sky!\" to text channel with id \"750449611302371469\"")
        .since("1.0");

        EventValues.registerEventValue(EventBotJoin.class, JDA.class, new Getter<JDA, EventBotJoin>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventBotJoin event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventBotJoin.class, Guild.class, new Getter<Guild, EventBotJoin>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventBotJoin event) {
                return event.getEvent().getGuild();
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final GuildJoinEvent event;

    public EventBotJoin(
            final GuildJoinEvent event) {
        super(Utils.areEventAsync());
        this.event = event;
    }

    public GuildJoinEvent getEvent() {
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