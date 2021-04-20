package info.itsthesky.DiSky.skript.events.skript.audio;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.DiSky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Voice Channel Join")
@Description("Fired when any member join a voice channel.")
@Examples("on voice channel join:")
@Since("1.9")
public class EventVoiceJoin extends Event {

    static {
        Skript.registerEvent("Voice Channel Join", SimpleEvent.class, EventVoiceJoin.class, "[discord] [member] voice channel join");

        EventValues.registerEventValue(EventVoiceJoin.class, VoiceChannel.class, new Getter<VoiceChannel, EventVoiceJoin>() {
            @Nullable
            @Override
            public VoiceChannel get(final @NotNull EventVoiceJoin event) {
                return event.getEvent().getChannelJoined();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceJoin.class, GuildChannel.class, new Getter<GuildChannel, EventVoiceJoin>() {
            @Nullable
            @Override
            public GuildChannel get(final @NotNull EventVoiceJoin event) {
                return event.getEvent().getChannelJoined();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceJoin.class, Guild.class, new Getter<Guild, EventVoiceJoin>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventVoiceJoin event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceJoin.class, JDA.class, new Getter<JDA, EventVoiceJoin>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventVoiceJoin event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceJoin.class, Member.class, new Getter<Member, EventVoiceJoin>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventVoiceJoin event) {
                return event.getEvent().getEntity();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceJoin.class, User.class, new Getter<User, EventVoiceJoin>() {
            @Nullable
            @Override
            public User get(final @NotNull EventVoiceJoin event) {
                return event.getEvent().getEntity().getUser();
            }
        }, 0);

    }

    private final GuildVoiceJoinEvent e;

    public EventVoiceJoin(
            final GuildVoiceJoinEvent e) {
        super(Utils.areEventAsync());
        this.e = e;
    }

    public GuildVoiceJoinEvent getEvent() {
        return e;
    }

    private static final HandlerList HANDLERS = new HandlerList();
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}