package info.itsthesky.disky.skript.events.skript.guild;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.managers.BotManager;
import info.itsthesky.disky.skript.events.util.DiSkyEvent;
import info.itsthesky.disky.tools.UpdatedValue;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateOwnerEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Name("Guild Owner Update")
@Description({"Run when the owner of the guild is updated.",
        "Possible updated values:",
        "new guild owner",
        "old guild owner",
})
@Examples("on guild owner change:")
@Since("1.10")
public class EventGuildUpdateOwner extends Event {

    private static final UpdatedValue<Member> updatedOwner;

    static {
        Skript.registerEvent("Guild Owner Update", SimpleEvent.class, EventGuildUpdateOwner.class, "[discord] guild owner (change|update)")
        .description("Run when the owner of the guild is updated.",
                "Possible updated values:",
                "new guild owner",
                "old guild owner")
        .examples("on guild owner change:")
        .since("1.10");

        updatedOwner = new UpdatedValue<>(Member.class, EventGuildUpdateOwner.class, "[discord] guild owner", true).register();
        BotManager.customListener.add(new DiSkyEvent<>(GuildUpdateOwnerEvent.class, e -> Utils.sync(() -> DiSky.getPluginManager().callEvent(new EventGuildUpdateOwner(e)))));

        EventValues.registerEventValue(EventGuildUpdateOwner.class, Guild.class, new Getter<Guild, EventGuildUpdateOwner>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventGuildUpdateOwner event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventGuildUpdateOwner.class, JDA.class, new Getter<JDA, EventGuildUpdateOwner>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventGuildUpdateOwner event) {
                return event.getEvent().getJDA();
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final GuildUpdateOwnerEvent e;

    public EventGuildUpdateOwner(
            final GuildUpdateOwnerEvent e
            ) {
        super(Utils.areEventAsync());
        this.e = e;
        updatedOwner.setNewObject(e.getNewOwner());
        updatedOwner.setOldObject(e.getOldOwner());
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public GuildUpdateOwnerEvent getEvent() {
        return e;
    }
}