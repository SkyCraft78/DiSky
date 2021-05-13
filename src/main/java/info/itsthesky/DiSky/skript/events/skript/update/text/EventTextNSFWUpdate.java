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
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateNSFWEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventTextNSFWUpdate extends Event {

    private static final UpdatedValue<Boolean> updatedValue;

    static {
        Skript.registerEvent("Text Channel NSFW Update", SimpleEvent.class, EventTextNSFWUpdate.class, "[discord] [guild] text [channel] nsfw (change|update)")
                .description("Run when someone change the nsfw of a Text channel.")
                .examples("on text nsfw update:")
                .since("1.12");

        BotManager.customListener.add(new DiSkyEvent<>(TextChannelUpdateNSFWEvent.class, e -> Utils.sync(() -> DiSky.getPluginManager().callEvent(new EventTextNSFWUpdate(e)))));
        updatedValue = new UpdatedValue<>(Boolean.class, EventTextNSFWUpdate.class, "channel nsfw", true).register();

        EventValues.registerEventValue(EventTextNSFWUpdate.class, Guild.class, new Getter<Guild, EventTextNSFWUpdate>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventTextNSFWUpdate event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventTextNSFWUpdate.class, JDA.class, new Getter<JDA, EventTextNSFWUpdate>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventTextNSFWUpdate event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventTextNSFWUpdate.class, TextChannel.class, new Getter<TextChannel, EventTextNSFWUpdate>() {
            @Nullable
            @Override
            public TextChannel get(final @NotNull EventTextNSFWUpdate event) {
                return event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventTextNSFWUpdate.class, GuildChannel.class, new Getter<GuildChannel, EventTextNSFWUpdate>() {
            @Nullable
            @Override
            public GuildChannel get(final @NotNull EventTextNSFWUpdate event) {
                return event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventTextNSFWUpdate.class, User.class, new Getter<User, EventTextNSFWUpdate>() {
            @Nullable
            @Override
            public User get(final @NotNull EventTextNSFWUpdate event) {
                return event.author;
            }
        }, 0);

        EventValues.registerEventValue(EventTextNSFWUpdate.class, Member.class, new Getter<Member, EventTextNSFWUpdate>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventTextNSFWUpdate event) {
                return event.authorM;
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final TextChannelUpdateNSFWEvent e;
    private final User author;
    private final Member authorM;

    public EventTextNSFWUpdate(
            final TextChannelUpdateNSFWEvent e
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

    public TextChannelUpdateNSFWEvent getEvent() {
        return e;
    }
}