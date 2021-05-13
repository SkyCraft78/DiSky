package info.itsthesky.disky.skript.events.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.skript.commands.CommandObject;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("On DiSky Command")
@Description("Fired when any discord command from DiSky is done.")
@Examples("on disky command:")
@Since("1.8")
public class EventDiSkyCommand extends Event {

    static {
        // [seen by [bot] [(named|with name)]%string%]
        Skript.registerEvent("DiSky Command", SimpleEvent.class, EventDiSkyCommand.class, "[discord] disky command")
        .description("Fired when any discord command from DiSky is done.")
        .examples("on disky command:")
        .since("1.8");

        EventValues.registerEventValue(EventDiSkyCommand.class, JDA.class, new Getter<JDA, EventDiSkyCommand>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventDiSkyCommand event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventDiSkyCommand.class, Guild.class, new Getter<Guild, EventDiSkyCommand>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventDiSkyCommand event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventDiSkyCommand.class, TextChannel.class, new Getter<TextChannel, EventDiSkyCommand>() {
            @Nullable
            @Override
            public TextChannel get(final @NotNull EventDiSkyCommand event) {
                return event.getEvent().getTextChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventDiSkyCommand.class, GuildChannel.class, new Getter<GuildChannel, EventDiSkyCommand>() {
            @Nullable
            @Override
            public GuildChannel get(final @NotNull EventDiSkyCommand event) {
                return event.getEvent().getTextChannel();
            }
        }, 0);

        EventValues.registerEventValue(EventDiSkyCommand.class, Member.class, new Getter<Member, EventDiSkyCommand>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventDiSkyCommand event) {
                return event.getEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(EventDiSkyCommand.class, User.class, new Getter<User, EventDiSkyCommand>() {
            @Nullable
            @Override
            public User get(final @NotNull EventDiSkyCommand event) {
                return event.getEvent().getMember().getUser();
            }
        }, 0);

        EventValues.registerEventValue(EventDiSkyCommand.class, CommandObject.class, new Getter<CommandObject, EventDiSkyCommand>() {
            @Nullable
            @Override
            public CommandObject get(final @NotNull EventDiSkyCommand event) {
                return event.getCommand();
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final MessageReceivedEvent e;
    private final CommandObject command;

    public EventDiSkyCommand(
            final MessageReceivedEvent e,
            final CommandObject command
    ) {
        super(Utils.areEventAsync());
        this.command = command;
        this.e = e;
    }

    public MessageReceivedEvent getEvent() {
        return e;
    }

    public CommandObject getCommand() {
        return command;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}