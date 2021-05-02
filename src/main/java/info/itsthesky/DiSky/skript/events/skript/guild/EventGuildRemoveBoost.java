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
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBoostCountEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Name("Guild Remove Boost")
@Description({"Run when a member remove a boost from the guild.",
        "Possible updated values:",
        "new boost amount",
        "old boost amount",
})
@Examples("on guild new boost:")
@Since("1.10")
public class EventGuildRemoveBoost extends Event {

    private static final UpdatedValue<Object> updatedAmount;

    static {
        Skript.registerEvent("Guild New Boost", SimpleEvent.class, EventGuildRemoveBoost.class, "[discord] [guild] remove [member] boost");

        updatedAmount = new UpdatedValue<>(EventGuildRemoveBoost.class, "number", true).register();
        BotManager.customListener.add(new DiSkyEvent<>(GuildUpdateBoostCountEvent.class, e -> Utils.sync(() -> {
            if (e.getNewValue() > e.getOldValue()) return;
            DiSky.getPluginManager().callEvent(new EventGuildRemoveBoost(e));
        })));

        EventValues.registerEventValue(EventGuildRemoveBoost.class, Guild.class, new Getter<Guild, EventGuildRemoveBoost>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventGuildRemoveBoost event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventGuildRemoveBoost.class, JDA.class, new Getter<JDA, EventGuildRemoveBoost>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventGuildRemoveBoost event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventGuildRemoveBoost.class, Member.class, new Getter<Member, EventGuildRemoveBoost>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventGuildRemoveBoost event) {
                return event.getEvent().getEntity().getSelfMember();
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final GuildUpdateBoostCountEvent e;

    public EventGuildRemoveBoost(
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