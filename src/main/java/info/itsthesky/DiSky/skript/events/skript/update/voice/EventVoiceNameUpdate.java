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
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateNameEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateNameEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventVoiceNameUpdate extends Event {

    private static final UpdatedValue<String> updatedValue;

    static {
        Skript.registerEvent("Voice Channel Name Update", SimpleEvent.class, EventVoiceNameUpdate.class, "[discord] [guild] voice [channel] name (change|update)")
                .description("Run when someone change the name of a voice channel.")
                .examples("on voice name update:")
                .since("1.12");

        BotManager.customListener.add(new DiSkyEvent<>(VoiceChannelUpdateNameEvent.class, e -> Utils.sync(() -> DiSky.getPluginManager().callEvent(new EventVoiceNameUpdate(e)))));
        updatedValue = new UpdatedValue<>(String.class, EventVoiceNameUpdate.class, "channel name", true).register();

        EventValues.registerEventValue(EventVoiceNameUpdate.class, Guild.class, new Getter<Guild, EventVoiceNameUpdate>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventVoiceNameUpdate event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceNameUpdate.class, JDA.class, new Getter<JDA, EventVoiceNameUpdate>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventVoiceNameUpdate event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceNameUpdate.class, VoiceChannel.class, new Getter<VoiceChannel, EventVoiceNameUpdate>() {
            @Nullable
            @Override
            public VoiceChannel get(final @NotNull EventVoiceNameUpdate event) {
                return event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceNameUpdate.class, GuildChannel.class, new Getter<GuildChannel, EventVoiceNameUpdate>() {
            @Nullable
            @Override
            public GuildChannel get(final @NotNull EventVoiceNameUpdate event) {
                return event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceNameUpdate.class, User.class, new Getter<User, EventVoiceNameUpdate>() {
            @Nullable
            @Override
            public User get(final @NotNull EventVoiceNameUpdate event) {
                return event.author;
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceNameUpdate.class, Member.class, new Getter<Member, EventVoiceNameUpdate>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventVoiceNameUpdate event) {
                return event.authorM;
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final VoiceChannelUpdateNameEvent e;
    private final User author;
    private final Member authorM;

    public EventVoiceNameUpdate(
            final VoiceChannelUpdateNameEvent e
            ) {
        super(Utils.areEventAsync());
        updatedValue.setNewObject(e.getNewName());
        updatedValue.setOldObject(e.getOldName());
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

    public VoiceChannelUpdateNameEvent getEvent() {
        return e;
    }
}