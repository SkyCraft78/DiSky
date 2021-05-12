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
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateParentEvent;
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateUserLimitEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventVoiceUserLimitUpdate extends Event {

    private static final UpdatedValue<Integer> updatedValue;

    static {
        Skript.registerEvent("Voice Channel User Limit Update", SimpleEvent.class, EventVoiceUserLimitUpdate.class, "[discord] [guild] voice [channel] [user] limit (change|update)")
                .description("Run when someone change the user limit parent of a voice channel.")
                .examples("on voice user limit update:")
                .since("1.12");

        BotManager.customListener.add(new DiSkyEvent<>(VoiceChannelUpdateUserLimitEvent.class, e -> Utils.sync(() -> DiSky.getPluginManager().callEvent(new EventVoiceUserLimitUpdate(e)))));
        updatedValue = new UpdatedValue<>(Integer.class, EventVoiceUserLimitUpdate.class, "channel user limit", true).register();

        EventValues.registerEventValue(EventVoiceUserLimitUpdate.class, Guild.class, new Getter<Guild, EventVoiceUserLimitUpdate>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventVoiceUserLimitUpdate event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceUserLimitUpdate.class, JDA.class, new Getter<JDA, EventVoiceUserLimitUpdate>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventVoiceUserLimitUpdate event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceUserLimitUpdate.class, VoiceChannel.class, new Getter<VoiceChannel, EventVoiceUserLimitUpdate>() {
            @Nullable
            @Override
            public VoiceChannel get(final @NotNull EventVoiceUserLimitUpdate event) {
                return event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceUserLimitUpdate.class, GuildChannel.class, new Getter<GuildChannel, EventVoiceUserLimitUpdate>() {
            @Nullable
            @Override
            public GuildChannel get(final @NotNull EventVoiceUserLimitUpdate event) {
                return event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceUserLimitUpdate.class, User.class, new Getter<User, EventVoiceUserLimitUpdate>() {
            @Nullable
            @Override
            public User get(final @NotNull EventVoiceUserLimitUpdate event) {
                return event.author;
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceUserLimitUpdate.class, Member.class, new Getter<Member, EventVoiceUserLimitUpdate>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventVoiceUserLimitUpdate event) {
                return event.authorM;
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final VoiceChannelUpdateUserLimitEvent e;
    private final User author;
    private final Member authorM;

    public EventVoiceUserLimitUpdate(
            final VoiceChannelUpdateUserLimitEvent e
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

    public VoiceChannelUpdateUserLimitEvent getEvent() {
        return e;
    }
}