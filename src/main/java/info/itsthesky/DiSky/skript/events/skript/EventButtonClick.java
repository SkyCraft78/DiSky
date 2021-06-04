package info.itsthesky.disky.skript.events.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.managers.BotManager;
import info.itsthesky.disky.skript.events.util.InteractionEvent;
import info.itsthesky.disky.skript.events.util.MessageEvent;
import info.itsthesky.disky.skript.events.util.DiSkyEvent;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.ButtonBuilder;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.interactions.Interaction;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventButtonClick extends Event implements InteractionEvent {

    static {
        Skript.registerEvent("Button Click", SimpleEvent.class, EventButtonClick.class, "[discord] [guild] button [hook] click")
                .description("Run when someone click on a custom button. They only work for CUSTOM button, not LINK buttons! Use 'discord id of event-button' to get the button ID.")
                .examples("on button click:")
                .since("1.12");

        BotManager.customListener.add(new DiSkyEvent<>(ButtonClickEvent.class, e -> Utils.sync(() -> DiSky.getPluginManager().callEvent(new EventButtonClick(e)))));

        EventValues.registerEventValue(EventButtonClick.class, Guild.class, new Getter<Guild, EventButtonClick>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventButtonClick event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventButtonClick.class, JDA.class, new Getter<JDA, EventButtonClick>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventButtonClick event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventButtonClick.class, TextChannel.class, new Getter<TextChannel, EventButtonClick>() {
            @Nullable
            @Override
            public TextChannel get(final @NotNull EventButtonClick event) {
                return (TextChannel) event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventButtonClick.class, ButtonBuilder.class, new Getter<ButtonBuilder, EventButtonClick>() {
            @Nullable
            @Override
            public ButtonBuilder get(final @NotNull EventButtonClick event) {
                return ButtonBuilder.fromButton(event.getEvent().getButton());
            }
        }, 0);

        EventValues.registerEventValue(EventButtonClick.class, GuildChannel.class, new Getter<GuildChannel, EventButtonClick>() {
            @Nullable
            @Override
            public GuildChannel get(final @NotNull EventButtonClick event) {
                return (GuildChannel) event.getEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventButtonClick.class, UpdatingMessage.class, new Getter<UpdatingMessage, EventButtonClick>() {
            @Nullable
            @Override
            public UpdatingMessage get(final @NotNull EventButtonClick event) {
                return UpdatingMessage.from(event.getEvent().getMessage());
            }
        }, 0);

        EventValues.registerEventValue(EventButtonClick.class, User.class, new Getter<User, EventButtonClick>() {
            @Nullable
            @Override
            public User get(final @NotNull EventButtonClick event) {
                return event.getEvent().getMember().getUser();
            }
        }, 0);

        EventValues.registerEventValue(EventButtonClick.class, Member.class, new Getter<Member, EventButtonClick>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventButtonClick event) {
                return event.getEvent().getMember();
            }
        }, 0);
    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final ButtonClickEvent e;

    public EventButtonClick(
            final ButtonClickEvent e
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

    public ButtonClickEvent getEvent() {
        return e;
    }


    @Override
    public GenericInteractionCreateEvent getInteractionEvent() {
        return this.e;
    }
}