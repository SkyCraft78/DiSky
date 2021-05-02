package info.itsthesky.DiSky.skript.events.skript.textchannels;

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
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateSlowmodeEvent;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateTopicEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Name("Text Channel Slow Mode Update")
@Description({"Run when the slow mode of a text channel is updated.",
        "Possible updated values:",
        "new [discord] [text] channel slow[-][ ]mode",
        "old [discord] [text] channel slow[-][ ]mode",
})
@Examples("on channel slow mode change:")
@Since("1.10")
public class EventTextUpdateSlowMode extends Event {

    private static final UpdatedValue<Object> updatedOwner;

    static {
        Skript.registerEvent("Text Channel Slow Mode Update", SimpleEvent.class, EventTextUpdateSlowMode.class, "[discord] [text] channel slow[-][ ]mode (change|update)");

        updatedOwner = new UpdatedValue<>(EventTextUpdateSlowMode.class, "number", true).register();
        new DiSkyEvent<>(TextChannelUpdateSlowmodeEvent.class, e -> Utils.sync(() -> DiSky.getPluginManager().callEvent(new EventTextUpdateSlowMode(e))));

        EventValues.registerEventValue(EventTextUpdateSlowMode.class, Guild.class, new Getter<Guild, EventTextUpdateSlowMode>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventTextUpdateSlowMode event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventTextUpdateSlowMode.class, JDA.class, new Getter<JDA, EventTextUpdateSlowMode>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventTextUpdateSlowMode event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventTextUpdateSlowMode.class, TextChannel.class, new Getter<TextChannel, EventTextUpdateSlowMode>() {
            @Nullable
            @Override
            public TextChannel get(final @NotNull EventTextUpdateSlowMode event) {
                return event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventTextUpdateSlowMode.class, GuildChannel.class, new Getter<GuildChannel, EventTextUpdateSlowMode>() {
            @Nullable
            @Override
            public GuildChannel get(final @NotNull EventTextUpdateSlowMode event) {
                return event.getEvent().getChannel();
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final TextChannelUpdateSlowmodeEvent e;

    public EventTextUpdateSlowMode(
            final TextChannelUpdateSlowmodeEvent e
            ) {
        super(Utils.areEventAsync());
        this.e = e;
        updatedOwner.setNewObject(e.getNewSlowmode());
        updatedOwner.setOldObject(e.getOldSlowmode());
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public TextChannelUpdateSlowmodeEvent getEvent() {
        return e;
    }
}