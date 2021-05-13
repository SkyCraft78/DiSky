package info.itsthesky.disky.skript.events.skript.update.text;

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
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateNameEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventTextNameUpdate extends Event {

    private static final UpdatedValue<String> updatedValue;

    static {
        Skript.registerEvent("Text Channel Name Update", SimpleEvent.class, EventTextNameUpdate.class, "[discord] [guild] text [channel] name (change|update)")
                .description("Run when someone change the name of a Text channel.")
                .examples("on text name update:")
                .since("1.12");

        BotManager.customListener.add(new DiSkyEvent<>(TextChannelUpdateNameEvent.class, e -> Utils.sync(() -> DiSky.getPluginManager().callEvent(new EventTextNameUpdate(e)))));
        updatedValue = new UpdatedValue<>(String.class, EventTextNameUpdate.class, "channel name", true).register();

        EventValues.registerEventValue(EventTextNameUpdate.class, Guild.class, new Getter<Guild, EventTextNameUpdate>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventTextNameUpdate event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventTextNameUpdate.class, JDA.class, new Getter<JDA, EventTextNameUpdate>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventTextNameUpdate event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventTextNameUpdate.class, TextChannel.class, new Getter<TextChannel, EventTextNameUpdate>() {
            @Nullable
            @Override
            public TextChannel get(final @NotNull EventTextNameUpdate event) {
                return event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventTextNameUpdate.class, GuildChannel.class, new Getter<GuildChannel, EventTextNameUpdate>() {
            @Nullable
            @Override
            public GuildChannel get(final @NotNull EventTextNameUpdate event) {
                return event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventTextNameUpdate.class, User.class, new Getter<User, EventTextNameUpdate>() {
            @Nullable
            @Override
            public User get(final @NotNull EventTextNameUpdate event) {
                return event.author;
            }
        }, 0);

        EventValues.registerEventValue(EventTextNameUpdate.class, Member.class, new Getter<Member, EventTextNameUpdate>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventTextNameUpdate event) {
                return event.authorM;
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final TextChannelUpdateNameEvent e;
    private final User author;
    private final Member authorM;

    public EventTextNameUpdate(
            final TextChannelUpdateNameEvent e
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

    public TextChannelUpdateNameEvent getEvent() {
        return e;
    }
}