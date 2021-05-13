package info.itsthesky.disky.skript.events.skript.update.text;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.managers.BotManager;
import info.itsthesky.disky.skript.events.util.DiSkyEvent;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.audit.TargetType;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EventTextCreate extends Event {

    static {
        Skript.registerEvent("Text Channel Create", SimpleEvent.class, EventTextCreate.class, "[discord] [guild] text [channel] create")
                .description("Run when someone create a Text channel.")
                .examples("on text channel create:")
                .since("1.12");

        BotManager.customListener.add(new DiSkyEvent<>(TextChannelCreateEvent.class, e -> Utils.sync(() -> DiSky.getPluginManager().callEvent(new EventTextCreate(e)))));

        EventValues.registerEventValue(EventTextCreate.class, Guild.class, new Getter<Guild, EventTextCreate>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventTextCreate event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventTextCreate.class, JDA.class, new Getter<JDA, EventTextCreate>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventTextCreate event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventTextCreate.class, TextChannel.class, new Getter<TextChannel, EventTextCreate>() {
            @Nullable
            @Override
            public TextChannel get(final @NotNull EventTextCreate event) {
                return event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventTextCreate.class, GuildChannel.class, new Getter<GuildChannel, EventTextCreate>() {
            @Nullable
            @Override
            public GuildChannel get(final @NotNull EventTextCreate event) {
                return event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventTextCreate.class, User.class, new Getter<User, EventTextCreate>() {
            @Nullable
            @Override
            public User get(final @NotNull EventTextCreate event) {
                return event.author;
            }
        }, 0);

        EventValues.registerEventValue(EventTextCreate.class, Member.class, new Getter<Member, EventTextCreate>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventTextCreate event) {
                return event.authorM;
            }
        }, 0);

        EventValues.registerEventValue(EventTextCreate.class, GuildChannel.class, new Getter<GuildChannel, EventTextCreate>() {
            @Nullable
            @Override
            public GuildChannel get(final @NotNull EventTextCreate event) {
                return event.channel;
            }
        }, 0);

        EventValues.registerEventValue(EventTextCreate.class, TextChannel.class, new Getter<TextChannel, EventTextCreate>() {
            @Nullable
            @Override
            public TextChannel get(final @NotNull EventTextCreate event) {
                return event.channel;
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final TextChannelCreateEvent e;
    private final User author;
    private final Member authorM;
    private final TextChannel channel;

    public EventTextCreate(
            final TextChannelCreateEvent e
            ) {
        super(Utils.areEventAsync());
        List<AuditLogEntry> logs = e.getGuild().retrieveAuditLogs().complete();
        author = logs.get(0).getUser();
        authorM = e.getGuild().getMember(author);
        channel = e.getGuild().getTextChannelById(logs.get(0).getTargetId());
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

    public TextChannelCreateEvent getEvent() {
        return e;
    }
}