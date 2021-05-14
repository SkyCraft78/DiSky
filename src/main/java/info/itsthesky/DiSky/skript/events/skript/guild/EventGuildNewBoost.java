package info.itsthesky.disky.skript.events.skript.guild;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
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
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBoostCountEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Name("Guild New Boost")
@Description({"Run when a member boost a new time the guild.",
        "Possible updated values:",
        "new boost amount",
        "old boost amount",
})
@Examples("on guild new boost:")
@Since("1.10")
public class EventGuildNewBoost extends Event {

    private static final UpdatedValue<Integer> updatedAmount;

    static {
        Skript.registerEvent("Guild New Boost", SimpleEvent.class, EventGuildNewBoost.class, "[discord] [guild] new [member] boost")
            .description("Run when a member boost a new time in the guild.",
                    "Possible updated values:",
                    "new boost amount",
                    "old boost amount")
            .examples("on guild new boost:")
            .since("1.10");

        updatedAmount = new UpdatedValue<>(Integer.class, EventGuildNewBoost.class, "[discord] boost amount", true).register();
        BotManager.customListener.add(new DiSkyEvent<>(GuildUpdateBoostCountEvent.class, e -> Utils.sync(() -> {
            if (e.getNewValue() < e.getOldValue()) return;
            DiSky.getPluginManager().callEvent(new EventGuildNewBoost(e));
        })));

        EventValues.registerEventValue(EventGuildNewBoost.class, Guild.class, new Getter<Guild, EventGuildNewBoost>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventGuildNewBoost event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventGuildNewBoost.class, JDA.class, new Getter<JDA, EventGuildNewBoost>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventGuildNewBoost event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventGuildNewBoost.class, Member.class, new Getter<Member, EventGuildNewBoost>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventGuildNewBoost event) {
                return event.getEvent().getEntity().getSelfMember();
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final GuildUpdateBoostCountEvent e;

    public EventGuildNewBoost(
            final GuildUpdateBoostCountEvent e
            ) {
        super(Utils.areEventAsync());
        this.e = e;
        updatedAmount.setNewObject(e.getNewValue());
        updatedAmount.setOldObject(e.getOldValue());
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public GuildUpdateBoostCountEvent getEvent() {
        return e;
    }
}