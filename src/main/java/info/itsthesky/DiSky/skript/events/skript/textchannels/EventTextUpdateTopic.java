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
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateNameEvent;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateTopicEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Name("Text Channel Topic Update")
@Description({"Run when the topic of a text channel is updated.",
        "Possible updated values:",
        "new [discord] [text] channel topic",
        "old [discord] [text] channel topic",
})
@Examples("on channel topic change:")
@Since("1.10")
public class EventTextUpdateTopic extends Event {

    private static final UpdatedValue<String> updatedOwner;

    static {
        Skript.registerEvent("Text Channel Topic Update", SimpleEvent.class, EventTextUpdateTopic.class, "[discord] [text] channel topic (change|update)");

        updatedOwner = new UpdatedValue<>(String.class, EventTextUpdateTopic.class, "[discord] [text] channel topic", true).register();
        new DiSkyEvent<>(TextChannelUpdateTopicEvent.class, e -> Utils.sync(() -> DiSky.getPluginManager().callEvent(new EventTextUpdateTopic(e))));

        EventValues.registerEventValue(EventTextUpdateTopic.class, Guild.class, new Getter<Guild, EventTextUpdateTopic>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventTextUpdateTopic event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventTextUpdateTopic.class, JDA.class, new Getter<JDA, EventTextUpdateTopic>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventTextUpdateTopic event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventTextUpdateTopic.class, TextChannel.class, new Getter<TextChannel, EventTextUpdateTopic>() {
            @Nullable
            @Override
            public TextChannel get(final @NotNull EventTextUpdateTopic event) {
                return event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventTextUpdateTopic.class, GuildChannel.class, new Getter<GuildChannel, EventTextUpdateTopic>() {
            @Nullable
            @Override
            public GuildChannel get(final @NotNull EventTextUpdateTopic event) {
                return event.getEvent().getChannel();
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final TextChannelUpdateTopicEvent e;

    public EventTextUpdateTopic(
            final TextChannelUpdateTopicEvent e
            ) {
        super(Utils.areEventAsync());
        this.e = e;
        updatedOwner.setNewObject(e.getNewTopic());
        updatedOwner.setOldObject(e.getOldTopic());
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public TextChannelUpdateTopicEvent getEvent() {
        return e;
    }
}