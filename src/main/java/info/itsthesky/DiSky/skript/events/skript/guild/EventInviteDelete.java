package info.itsthesky.disky.skript.events.skript.guild;

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
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Name("Invite Delete")
@Description("Run when someone delete an invite in a guild.")
@Examples("on invite delete:")
@Since("1.9")
public class EventInviteDelete extends Event {

    static {
        Skript.registerEvent("Invite Delete", SimpleEvent.class, EventInviteDelete.class, "[discord] [guild] invite delet(e|ion)")
                .description("Run when someone delete an invite in a guild.")
                .examples("on invite delete:")
                .since("1.9");

        EventValues.registerEventValue(EventInviteDelete.class, GuildChannel.class, new Getter<GuildChannel, EventInviteDelete>() {
            @Nullable
            @Override
            public GuildChannel get(final @NotNull EventInviteDelete event) {
                return event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventInviteDelete.class, TextChannel.class, new Getter<TextChannel, EventInviteDelete>() {
            @Nullable
            @Override
            public TextChannel get(final @NotNull EventInviteDelete event) {
                return event.getEvent().getTextChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventInviteDelete.class, VoiceChannel.class, new Getter<VoiceChannel, EventInviteDelete>() {
            @Nullable
            @Override
            public VoiceChannel get(final @NotNull EventInviteDelete event) {
                return event.getEvent().getVoiceChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventInviteDelete.class, JDA.class, new Getter<JDA, EventInviteDelete>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventInviteDelete event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventInviteDelete.class, Guild.class, new Getter<Guild, EventInviteDelete>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventInviteDelete event) {
                return event.getEvent().getGuild();
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final GuildInviteDeleteEvent e;

    public EventInviteDelete(
            final GuildInviteDeleteEvent e
            ) {
        super(Utils.areEventAsync());
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

    public GuildInviteDeleteEvent getEvent() {
        return e;
    }
}