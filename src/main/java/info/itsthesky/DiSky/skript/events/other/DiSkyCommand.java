package info.itsthesky.disky.skript.events.other;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.skript.commands.CommandObject;
import info.itsthesky.disky.skript.events.guild.GuildMessage;
import info.itsthesky.disky.tools.events.BukkitEvent;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class DiSkyCommand extends SkriptEvent {

    static {
        DiSkyEvent.register("Inner Event Name", DiSkyCommand.class, EvtDiSkyCommand.class,
                "disky command")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");

        EventValues.registerEventValue(EvtDiSkyCommand.class, CommandObject.class, new Getter<CommandObject, EvtDiSkyCommand>() {
            @Override
            public CommandObject get(EvtDiSkyCommand event) {
                return event.command;
            }
        }, 0);

        EventValues.registerEventValue(EvtDiSkyCommand.class, UpdatingMessage.class, new Getter<UpdatingMessage, EvtDiSkyCommand>() {
            @Override
            public UpdatingMessage get(EvtDiSkyCommand event) {
                return UpdatingMessage.from(event.jdaEvent.getMessage());
            }
        }, 0);

        EventValues.registerEventValue(EvtDiSkyCommand.class, User.class, new Getter<User, EvtDiSkyCommand>() {
            @Override
            public User get(EvtDiSkyCommand event) {
                return event.jdaEvent.getAuthor();
            }
        }, 0);

        EventValues.registerEventValue(EvtDiSkyCommand.class, Member.class, new Getter<Member, EvtDiSkyCommand>() {
            @Override
            public Member get(EvtDiSkyCommand event) {
                return event.jdaEvent.getMember();
            }
        }, 0);

        EventValues.registerEventValue(EvtDiSkyCommand.class, TextChannel.class, new Getter<TextChannel, EvtDiSkyCommand>() {
            @Override
            public TextChannel get(EvtDiSkyCommand event) {
                return (TextChannel) event.jdaEvent.getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EvtDiSkyCommand.class, Guild.class, new Getter<Guild, EvtDiSkyCommand>() {
            @Override
            public Guild get(EvtDiSkyCommand event) {
                return event.jdaEvent.getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtDiSkyCommand.class, JDA.class, new Getter<JDA, EvtDiSkyCommand>() {
            @Override
            public JDA get(EvtDiSkyCommand event) {
                return event.jdaEvent.getJDA();
            }
        }, 0);
    }

    @Override
    public boolean init(Literal<?>[] exprs, int i, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(Event event) {
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "on disky command";
    }

    public static class EvtDiSkyCommand extends BukkitEvent implements Cancellable {
        private final CommandObject command;
        private final MessageReceivedEvent jdaEvent;
        public EvtDiSkyCommand(CommandObject command,
                               MessageReceivedEvent e) {
            this.command = command;
            jdaEvent = e;
        }

        private boolean isCancelled = false;

        @Override
        public boolean isCancelled() {
            return isCancelled;
        }

        @Override
        public void setCancelled(boolean cancel) {
            isCancelled = cancel;
        }
    }
}