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
import info.itsthesky.DiSky.managers.BotManager;
import info.itsthesky.DiSky.skript.events.util.DiSkyEvent;
import info.itsthesky.DiSky.tools.UpdatedValue;
import info.itsthesky.DiSky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateRegionEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateVerificationLevelEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;


@Name("Guild Verification Level Update")
@Description({"Run when the verification level of the guild is updated.",
        "Possible updated values:",
        "new guild [verification] level",
        "old guild [verification] level",
})
@Examples("on guild verification level change:")
@Since("1.10")
public class EventGuildUpdateVerif extends Event {

    private static final UpdatedValue<String> updatedLevel;

    static {
        Skript.registerEvent("Guild Owner Update", SimpleEvent.class, EventGuildUpdateVerif.class, "[discord] guild verification [level] (change|update)");

        updatedLevel = new UpdatedValue<>(String.class, EventGuildUpdateVerif.class, "[discord] guild [verification] level", true).register();
        BotManager.customListener.add(new DiSkyEvent<>(GuildUpdateVerificationLevelEvent.class, e -> Utils.sync(() -> DiSky.getPluginManager().callEvent(new EventGuildUpdateVerif(e)))));

        EventValues.registerEventValue(EventGuildUpdateVerif.class, Guild.class, new Getter<Guild, EventGuildUpdateVerif>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventGuildUpdateVerif event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventGuildUpdateVerif.class, JDA.class, new Getter<JDA, EventGuildUpdateVerif>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventGuildUpdateVerif event) {
                return event.getEvent().getJDA();
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final GuildUpdateVerificationLevelEvent e;

    public EventGuildUpdateVerif(
            final GuildUpdateVerificationLevelEvent e
            ) {
        super(Utils.areEventAsync());
        this.e = e;
        updatedLevel.setNewObject(e.getNewValue().name().toLowerCase(Locale.ROOT).replace("_", " "));
        updatedLevel.setOldObject(e.getOldValue().name().toLowerCase(Locale.ROOT).replace("_", " "));
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public GuildUpdateVerificationLevelEvent getEvent() {
        return e;
    }
}