package info.itsthesky.disky.skript.events.skript.guild.afk;

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
import net.dv8tion.jda.api.events.guild.update.GuildUpdateAfkChannelEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Name("AFK Channel Update")
@Description("Run when someone change the AFK channel of the guild. Use new afk channel and old afk channel expression.")
@Examples("on afk channel update:")
@Since("1.9")
public class EventAFKChannelUpdate extends Event {

    static {
        Skript.registerEvent("Invite Create", SimpleEvent.class, EventAFKChannelUpdate.class, "[discord] [guild] afk channel (change|update)");

        EventValues.registerEventValue(EventAFKChannelUpdate.class, GuildChannel.class, new Getter<GuildChannel, EventAFKChannelUpdate>() {
            @Nullable
            @Override
            public GuildChannel get(final @NotNull EventAFKChannelUpdate event) {
                return event.getEvent().getNewAfkChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventAFKChannelUpdate.class, VoiceChannel.class, new Getter<VoiceChannel, EventAFKChannelUpdate>() {
            @Nullable
            @Override
            public VoiceChannel get(final @NotNull EventAFKChannelUpdate event) {
                return event.getEvent().getNewAfkChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventAFKChannelUpdate.class, JDA.class, new Getter<JDA, EventAFKChannelUpdate>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventAFKChannelUpdate event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventAFKChannelUpdate.class, Guild.class, new Getter<Guild, EventAFKChannelUpdate>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventAFKChannelUpdate event) {
                return event.getEvent().getGuild();
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final GuildUpdateAfkChannelEvent e;

    public EventAFKChannelUpdate(
            final GuildUpdateAfkChannelEvent e
            ) {
        super(Utils.areEventAsync());
        this.e = e;
        ExprNewAFKChannel.newAFKChannel = e.getNewAfkChannel();
        ExprOldAFKChannel.oldAFKChannel = e.getOldAfkChannel();
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public GuildUpdateAfkChannelEvent getEvent() {
        return e;
    }
}