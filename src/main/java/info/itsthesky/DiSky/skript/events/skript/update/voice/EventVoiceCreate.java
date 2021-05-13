package info.itsthesky.disky.skript.events.skript.update.voice;

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
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EventVoiceCreate extends Event {

    static {
        Skript.registerEvent("Voice Channel Create", SimpleEvent.class, EventVoiceCreate.class, "[discord] [guild] voice [channel] create")
                .description("Run when someone create a Voice channel.")
                .examples("on voice channel create:")
                .since("1.12");

        BotManager.customListener.add(new DiSkyEvent<>(VoiceChannelCreateEvent.class, e -> Utils.sync(() -> DiSky.getPluginManager().callEvent(new EventVoiceCreate(e)))));

        EventValues.registerEventValue(EventVoiceCreate.class, Guild.class, new Getter<Guild, EventVoiceCreate>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventVoiceCreate event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceCreate.class, JDA.class, new Getter<JDA, EventVoiceCreate>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventVoiceCreate event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceCreate.class, VoiceChannel.class, new Getter<VoiceChannel, EventVoiceCreate>() {
            @Nullable
            @Override
            public VoiceChannel get(final @NotNull EventVoiceCreate event) {
                return event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceCreate.class, GuildChannel.class, new Getter<GuildChannel, EventVoiceCreate>() {
            @Nullable
            @Override
            public GuildChannel get(final @NotNull EventVoiceCreate event) {
                return event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceCreate.class, User.class, new Getter<User, EventVoiceCreate>() {
            @Nullable
            @Override
            public User get(final @NotNull EventVoiceCreate event) {
                return event.author;
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceCreate.class, Member.class, new Getter<Member, EventVoiceCreate>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventVoiceCreate event) {
                return event.authorM;
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceCreate.class, GuildChannel.class, new Getter<GuildChannel, EventVoiceCreate>() {
            @Nullable
            @Override
            public GuildChannel get(final @NotNull EventVoiceCreate event) {
                return event.channel;
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceCreate.class, VoiceChannel.class, new Getter<VoiceChannel, EventVoiceCreate>() {
            @Nullable
            @Override
            public VoiceChannel get(final @NotNull EventVoiceCreate event) {
                return event.channel;
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final VoiceChannelCreateEvent e;
    private final User author;
    private final Member authorM;
    private final VoiceChannel channel;

    public EventVoiceCreate(
            final VoiceChannelCreateEvent e
            ) {
        super(Utils.areEventAsync());
        List<AuditLogEntry> logs = e.getGuild().retrieveAuditLogs().complete();
        author = logs.get(0).getUser();
        authorM = e.getGuild().getMember(author);
        channel = e.getGuild().getVoiceChannelById(logs.get(0).getTargetId());
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

    public VoiceChannelCreateEvent getEvent() {
        return e;
    }
}