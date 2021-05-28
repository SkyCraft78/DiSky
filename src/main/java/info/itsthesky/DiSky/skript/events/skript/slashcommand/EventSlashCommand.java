package info.itsthesky.disky.skript.events.skript.slashcommand;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.StaticData;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Slash Command")
@Description("Fired when a SLASH command is executed by any user. Use %event-string% for the command name!\n See the wiki to know how to get the options :)\nWiki: https://github.com/SkyCraft78/DiSky/wiki/Slash-Commands")
@Examples("on slash command:")
@Since("1.5")
public class EventSlashCommand extends Event {

    static {
        // [seen by [bot] [(named|with name)]%string%]
        Skript.registerEvent("Slash Command", SimpleEvent.class, EventSlashCommand.class, "[discord] slash command")
        .description("Fired when a SLASH command is executed by any user. Use %event-string% for the command name!\n See the wiki to know how to get the options :)\nWiki: https://github.com/SkyCraft78/DiSky/wiki/Slash-Commands")
        .examples("on slash command:")
        .since("1.5");

        EventValues.registerEventValue(EventSlashCommand.class, TextChannel.class, new Getter<TextChannel, EventSlashCommand>() {
            @Nullable
            @Override
            public TextChannel get(final @NotNull EventSlashCommand event) {
                return event.getEvent().getTextChannel();
            }
        }, 0);


        EventValues.registerEventValue(EventSlashCommand.class, User.class, new Getter<User, EventSlashCommand>() {
            @Nullable
            @Override
            public User get(final @NotNull EventSlashCommand event) {
                return event.getEvent().getMember().getUser();
            }
        }, 0);

        EventValues.registerEventValue(EventSlashCommand.class, Guild.class, new Getter<Guild, EventSlashCommand>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventSlashCommand event) {
                return event.getEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EventSlashCommand.class, Member.class, new Getter<Member, EventSlashCommand>() {
            @Nullable
            @Override
            public Member get(final @NotNull EventSlashCommand event) {
                return event.getEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(EventSlashCommand.class, String.class, new Getter<String, EventSlashCommand>() {
            @Nullable
            @Override
            public String get(final @NotNull EventSlashCommand event) {
                return event.getEvent().getName();
            }
        }, 0);


    }

    private static final HandlerList HANDLERS = new HandlerList();
    private final SlashCommandEvent e;

    public EventSlashCommand(
            final SlashCommandEvent e
            ) {
        super(Utils.areEventAsync());
        StaticData.lastSlashCommandEvent = e;
        this.e = e;
        //e.acknowledge(true).queue();
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    public SlashCommandEvent getEvent() { return this.e; }
}