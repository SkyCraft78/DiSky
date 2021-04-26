package info.itsthesky.DiSky.skript.events.skript.guild;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.DiSky.DiSky;
import info.itsthesky.DiSky.skript.events.util.DiSkyEvent;
import info.itsthesky.DiSky.tools.UpdatedValue;
import info.itsthesky.DiSky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateOwnerEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateRegionEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Name("Guild Region Update")
@Description({"Run when the region of the guild is updated.",
        "Possible updated values:",
        "new guild region",
        "old guild region",
})
@Examples("on guild region change:")
@Since("1.10")
public class EventGuildUpdateRegion extends Event {

    private static final UpdatedValue<String> updatedOwner;

    static {
        Skript.registerEvent("Guild Owner Update", SimpleEvent.class, EventGuildUpdateRegion.class, "[discord] guild region (change|update)");

        updatedOwner = new UpdatedValue<>(String.class, EventGuildUpdateRegion.class, "[discord] guild region", true).register();
        new DiSkyEvent<>(GuildUpdateRegionEvent.class, e -> Utils.sync(() -> DiSky.getPluginManager().callEvent(new EventGuildUpdateRegion(e))));

        EventValues.registerEventValue(EventGuildUpdateRegion.class, Guild.class, new Getter<Guild, EventGuildUpdateRegion>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventGuildUpdateRegion event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventGuildUpdateRegion.class, JDA.class, new Getter<JDA, EventGuildUpdateRegion>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventGuildUpdateRegion event) {
                return event.getEvent().getJDA();
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final GuildUpdateRegionEvent e;

    public EventGuildUpdateRegion(
            final GuildUpdateRegionEvent e
            ) {
        super(Utils.areEventAsync());
        this.e = e;
        updatedOwner.setNewObject(e.getNewRegion().getName());
        updatedOwner.setOldObject(e.getOldRegion().getName());
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public GuildUpdateRegionEvent getEvent() {
        return e;
    }
}