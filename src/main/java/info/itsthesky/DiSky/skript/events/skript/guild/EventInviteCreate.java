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
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Name("Invite Create")
@Description("Run when someone create a new invite in a guild.")
@Examples("on invite create:")
@Since("1.9")
public class EventInviteCreate extends Event {

    static {
        Skript.registerEvent("Invite Create", SimpleEvent.class, EventInviteCreate.class, "[discord] [guild] invite creat(e|ion)");

        EventValues.registerEventValue(EventInviteCreate.class, Invite.class, new Getter<Invite, EventInviteCreate>() {
            @Nullable
            @Override
            public Invite get(final @NotNull EventInviteCreate event) {
                return event.getEvent().getInvite();
            }
        }, 0);

        EventValues.registerEventValue(EventInviteCreate.class, GuildChannel.class, new Getter<GuildChannel, EventInviteCreate>() {
            @Nullable
            @Override
            public GuildChannel get(final @NotNull EventInviteCreate event) {
                return event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventInviteCreate.class, TextChannel.class, new Getter<TextChannel, EventInviteCreate>() {
            @Nullable
            @Override
            public TextChannel get(final @NotNull EventInviteCreate event) {
                return event.getEvent().getTextChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventInviteCreate.class, VoiceChannel.class, new Getter<VoiceChannel, EventInviteCreate>() {
            @Nullable
            @Override
            public VoiceChannel get(final @NotNull EventInviteCreate event) {
                return event.getEvent().getVoiceChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventInviteCreate.class, JDA.class, new Getter<JDA, EventInviteCreate>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventInviteCreate event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventInviteCreate.class, Guild.class, new Getter<Guild, EventInviteCreate>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventInviteCreate event) {
                return event.getEvent().getGuild();
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final GuildInviteCreateEvent e;

    public EventInviteCreate(
            final GuildInviteCreateEvent e
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

    public GuildInviteCreateEvent getEvent() {
        return e;
    }
}