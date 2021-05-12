package info.itsthesky.disky.skript.events.skript.update.voice;

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
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateBitrateEvent;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateNameEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventVoiceBitrateUpdate extends Event {

    private static final UpdatedValue<Integer> updatedValue;

    static {
        Skript.registerEvent("Voice Channel Bitrate Update", SimpleEvent.class, EventVoiceBitrateUpdate.class, "[discord] [guild] voice [channel] bitrate (change|update)")
                .description("Run when someone change the bitrate of a voice channel.")
                .examples("on voice bitrate update:")
                .since("1.12");

        BotManager.customListener.add(new DiSkyEvent<>(VoiceChannelUpdateBitrateEvent.class, e -> Utils.sync(() -> DiSky.getPluginManager().callEvent(new EventVoiceBitrateUpdate(e)))));
        updatedValue = new UpdatedValue<>(Integer.class, EventVoiceBitrateUpdate.class, "channel bitrate", true).register();

        EventValues.registerEventValue(EventVoiceBitrateUpdate.class, Guild.class, new Getter<Guild, EventVoiceBitrateUpdate>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventVoiceBitrateUpdate event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceBitrateUpdate.class, JDA.class, new Getter<JDA, EventVoiceBitrateUpdate>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventVoiceBitrateUpdate event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceBitrateUpdate.class, VoiceChannel.class, new Getter<VoiceChannel, EventVoiceBitrateUpdate>() {
            @Nullable
            @Override
            public VoiceChannel get(final @NotNull EventVoiceBitrateUpdate event) {
                return event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceBitrateUpdate.class, GuildChannel.class, new Getter<GuildChannel, EventVoiceBitrateUpdate>() {
            @Nullable
            @Override
            public GuildChannel get(final @NotNull EventVoiceBitrateUpdate event) {
                return event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceBitrateUpdate.class, User.class, new Getter<User, EventVoiceBitrateUpdate>() {
            @Nullable
            @Override
            public User get(final @NotNull EventVoiceBitrateUpdate event) {
                return event.author;
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceBitrateUpdate.class, Member.class, new Getter<Member, EventVoiceBitrateUpdate>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventVoiceBitrateUpdate event) {
                return event.authorM;
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final VoiceChannelUpdateBitrateEvent e;
    private final User author;
    private final Member authorM;

    public EventVoiceBitrateUpdate(
            final VoiceChannelUpdateBitrateEvent e
            ) {
        super(Utils.areEventAsync());
        updatedValue.setNewObject(e.getNewValue());
        updatedValue.setOldObject(e.getOldValue());
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

    public VoiceChannelUpdateBitrateEvent getEvent() {
        return e;
    }
}