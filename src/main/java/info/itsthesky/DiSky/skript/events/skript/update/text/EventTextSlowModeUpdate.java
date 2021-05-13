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
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateSlowmodeEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventTextSlowModeUpdate extends Event {

    private static final UpdatedValue<Integer> updatedValue;

    static {
        Skript.registerEvent("Text Channel Slowmode Update", SimpleEvent.class, EventTextSlowModeUpdate.class, "[discord] [guild] text [channel] slowmode (change|update)")
                .description("Run when someone change the slowmode of a Text channel.")
                .examples("on text slowmode update:")
                .since("1.12");

        BotManager.customListener.add(new DiSkyEvent<>(TextChannelUpdateSlowmodeEvent.class, e -> Utils.sync(() -> DiSky.getPluginManager().callEvent(new EventTextSlowModeUpdate(e)))));
        updatedValue = new UpdatedValue<>(Integer.class, EventTextSlowModeUpdate.class, "channel slowmode", true).register();

        EventValues.registerEventValue(EventTextSlowModeUpdate.class, Guild.class, new Getter<Guild, EventTextSlowModeUpdate>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventTextSlowModeUpdate event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventTextSlowModeUpdate.class, JDA.class, new Getter<JDA, EventTextSlowModeUpdate>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventTextSlowModeUpdate event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventTextSlowModeUpdate.class, TextChannel.class, new Getter<TextChannel, EventTextSlowModeUpdate>() {
            @Nullable
            @Override
            public TextChannel get(final @NotNull EventTextSlowModeUpdate event) {
                return event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventTextSlowModeUpdate.class, GuildChannel.class, new Getter<GuildChannel, EventTextSlowModeUpdate>() {
            @Nullable
            @Override
            public GuildChannel get(final @NotNull EventTextSlowModeUpdate event) {
                return event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventTextSlowModeUpdate.class, User.class, new Getter<User, EventTextSlowModeUpdate>() {
            @Nullable
            @Override
            public User get(final @NotNull EventTextSlowModeUpdate event) {
                return event.author;
            }
        }, 0);

        EventValues.registerEventValue(EventTextSlowModeUpdate.class, Member.class, new Getter<Member, EventTextSlowModeUpdate>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventTextSlowModeUpdate event) {
                return event.authorM;
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final TextChannelUpdateSlowmodeEvent e;
    private final User author;
    private final Member authorM;

    public EventTextSlowModeUpdate(
            final TextChannelUpdateSlowmodeEvent e
            ) {
        super(Utils.areEventAsync());
        updatedValue.setNewObject(e.getNewSlowmode());
        updatedValue.setOldObject(e.getOldSlowmode());
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

    public TextChannelUpdateSlowmodeEvent getEvent() {
        return e;
    }
}