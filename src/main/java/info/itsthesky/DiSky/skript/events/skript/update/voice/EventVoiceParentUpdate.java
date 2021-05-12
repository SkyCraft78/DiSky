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
import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateParentEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventVoiceParentUpdate extends Event {

    private static final UpdatedValue<Category> updatedValue;

    static {
        Skript.registerEvent("Voice Channel Parent Update", SimpleEvent.class, EventVoiceParentUpdate.class, "[discord] [guild] voice [channel] parent (change|update)")
                .description("Run when someone change the category parent of a voice channel.")
                .examples("on voice parent update:")
                .since("1.12");

        BotManager.customListener.add(new DiSkyEvent<>(VoiceChannelUpdateParentEvent.class, e -> Utils.sync(() -> DiSky.getPluginManager().callEvent(new EventVoiceParentUpdate(e)))));
        updatedValue = new UpdatedValue<>(Category.class, EventVoiceParentUpdate.class, "channel parent", true).register();

        EventValues.registerEventValue(EventVoiceParentUpdate.class, Guild.class, new Getter<Guild, EventVoiceParentUpdate>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventVoiceParentUpdate event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceParentUpdate.class, JDA.class, new Getter<JDA, EventVoiceParentUpdate>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventVoiceParentUpdate event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceParentUpdate.class, VoiceChannel.class, new Getter<VoiceChannel, EventVoiceParentUpdate>() {
            @Nullable
            @Override
            public VoiceChannel get(final @NotNull EventVoiceParentUpdate event) {
                return event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceParentUpdate.class, GuildChannel.class, new Getter<GuildChannel, EventVoiceParentUpdate>() {
            @Nullable
            @Override
            public GuildChannel get(final @NotNull EventVoiceParentUpdate event) {
                return event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceParentUpdate.class, User.class, new Getter<User, EventVoiceParentUpdate>() {
            @Nullable
            @Override
            public User get(final @NotNull EventVoiceParentUpdate event) {
                return event.author;
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceParentUpdate.class, Member.class, new Getter<Member, EventVoiceParentUpdate>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventVoiceParentUpdate event) {
                return event.authorM;
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final VoiceChannelUpdateParentEvent e;
    private final User author;
    private final Member authorM;

    public EventVoiceParentUpdate(
            final VoiceChannelUpdateParentEvent e
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

    public VoiceChannelUpdateParentEvent getEvent() {
        return e;
    }
}