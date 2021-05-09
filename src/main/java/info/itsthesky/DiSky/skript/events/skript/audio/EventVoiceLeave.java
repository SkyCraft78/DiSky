package info.itsthesky.disky.skript.events.skript.audio;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Voice Channel Leave")
@Description("Fired when any member leave a voice channel.")
@Examples("on voice channel leave:")
@Since("1.9")
public class EventVoiceLeave extends Event {

    static {
        Skript.registerEvent("Voice Channel Leave", SimpleEvent.class, EventVoiceLeave.class, "[discord] [member] voice channel leave");

        EventValues.registerEventValue(EventVoiceLeave.class, VoiceChannel.class, new Getter<VoiceChannel, EventVoiceLeave>() {
            @Nullable
            @Override
            public VoiceChannel get(final @NotNull EventVoiceLeave event) {
                return event.getEvent().getChannelLeft();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceLeave.class, GuildChannel.class, new Getter<GuildChannel, EventVoiceLeave>() {
            @Nullable
            @Override
            public GuildChannel get(final @NotNull EventVoiceLeave event) {
                return event.getEvent().getChannelLeft();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceLeave.class, Guild.class, new Getter<Guild, EventVoiceLeave>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventVoiceLeave event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceLeave.class, JDA.class, new Getter<JDA, EventVoiceLeave>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventVoiceLeave event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceLeave.class, Member.class, new Getter<Member, EventVoiceLeave>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventVoiceLeave event) {
                return event.getEvent().getEntity();
            }
        }, 0);

        EventValues.registerEventValue(EventVoiceLeave.class, User.class, new Getter<User, EventVoiceLeave>() {
            @Nullable
            @Override
            public User get(final @NotNull EventVoiceLeave event) {
                return event.getEvent().getEntity().getUser();
            }
        }, 0);

    }

    private final GuildVoiceLeaveEvent e;

    public EventVoiceLeave(
            final GuildVoiceLeaveEvent e) {
        super(Utils.areEventAsync());
        this.e = e;
    }

    public GuildVoiceLeaveEvent getEvent() {
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