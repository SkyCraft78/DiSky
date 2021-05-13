package info.itsthesky.disky.skript.events.skript.update.text;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.managers.BotManager;
import info.itsthesky.disky.skript.events.util.DiSkyEvent;
import info.itsthesky.disky.tools.UpdatedValue;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateTopicEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventTextTopicUpdate extends Event {

    private static final UpdatedValue<String> updatedValue;

    static {
        Skript.registerEvent("Text Channel Topic Update", SimpleEvent.class, EventTextTopicUpdate.class, "[discord] [guild] text [channel] topic (change|update)")
                .description("Run when someone change the topic of a Text channel.")
                .examples("on text topic update:")
                .since("1.12");

        BotManager.customListener.add(new DiSkyEvent<>(TextChannelUpdateTopicEvent.class, e -> Utils.sync(() -> DiSky.getPluginManager().callEvent(new EventTextTopicUpdate(e)))));
        updatedValue = new UpdatedValue<>(String.class, EventTextTopicUpdate.class, "channel topic", true).register();

        EventValues.registerEventValue(EventTextTopicUpdate.class, Guild.class, new Getter<Guild, EventTextTopicUpdate>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventTextTopicUpdate event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventTextTopicUpdate.class, JDA.class, new Getter<JDA, EventTextTopicUpdate>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventTextTopicUpdate event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventTextTopicUpdate.class, TextChannel.class, new Getter<TextChannel, EventTextTopicUpdate>() {
            @Nullable
            @Override
            public TextChannel get(final @NotNull EventTextTopicUpdate event) {
                return event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventTextTopicUpdate.class, GuildChannel.class, new Getter<GuildChannel, EventTextTopicUpdate>() {
            @Nullable
            @Override
            public GuildChannel get(final @NotNull EventTextTopicUpdate event) {
                return event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventTextTopicUpdate.class, User.class, new Getter<User, EventTextTopicUpdate>() {
            @Nullable
            @Override
            public User get(final @NotNull EventTextTopicUpdate event) {
                return event.author;
            }
        }, 0);

        EventValues.registerEventValue(EventTextTopicUpdate.class, Member.class, new Getter<Member, EventTextTopicUpdate>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventTextTopicUpdate event) {
                return event.authorM;
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final TextChannelUpdateTopicEvent e;
    private final User author;
    private final Member authorM;

    public EventTextTopicUpdate(
            final TextChannelUpdateTopicEvent e
            ) {
        super(Utils.areEventAsync());
        updatedValue.setNewObject(e.getNewTopic());
        updatedValue.setOldObject(e.getOldTopic());
        author = e.getGuild().retrieveAuditLogs().complete().get(0).getUser();
        authorM = e.getGuild().getMember(author);
        this.e = e;
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