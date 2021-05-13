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
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateParentEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventTextParentUpdate extends Event {

    private static final UpdatedValue<Category> updatedValue;

    static {
        Skript.registerEvent("Text Channel Parent Update", SimpleEvent.class, EventTextParentUpdate.class, "[discord] [guild] text [channel] parent (change|update)")
                .description("Run when someone change the category parent of a Text channel.")
                .examples("on text parent update:")
                .since("1.12");

        BotManager.customListener.add(new DiSkyEvent<>(TextChannelUpdateParentEvent.class, e -> Utils.sync(() -> DiSky.getPluginManager().callEvent(new EventTextParentUpdate(e)))));
        updatedValue = new UpdatedValue<>(Category.class, EventTextParentUpdate.class, "channel parent", true).register();

        EventValues.registerEventValue(EventTextParentUpdate.class, Guild.class, new Getter<Guild, EventTextParentUpdate>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventTextParentUpdate event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventTextParentUpdate.class, JDA.class, new Getter<JDA, EventTextParentUpdate>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventTextParentUpdate event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventTextParentUpdate.class, TextChannel.class, new Getter<TextChannel, EventTextParentUpdate>() {
            @Nullable
            @Override
            public TextChannel get(final @NotNull EventTextParentUpdate event) {
                return event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventTextParentUpdate.class, GuildChannel.class, new Getter<GuildChannel, EventTextParentUpdate>() {
            @Nullable
            @Override
            public GuildChannel get(final @NotNull EventTextParentUpdate event) {
                return event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventTextParentUpdate.class, User.class, new Getter<User, EventTextParentUpdate>() {
            @Nullable
            @Override
            public User get(final @NotNull EventTextParentUpdate event) {
                return event.author;
            }
        }, 0);

        EventValues.registerEventValue(EventTextParentUpdate.class, Member.class, new Getter<Member, EventTextParentUpdate>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventTextParentUpdate event) {
                return event.authorM;
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final TextChannelUpdateParentEvent e;
    private final User author;
    private final Member authorM;

    public EventTextParentUpdate(
            final TextChannelUpdateParentEvent e
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

    public TextChannelUpdateParentEvent getEvent() {
        return e;
    }
}