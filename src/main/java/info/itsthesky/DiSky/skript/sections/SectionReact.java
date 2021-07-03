package info.itsthesky.disky.skript.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.effects.Delay;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.timings.SkriptTimings;
import ch.njol.skript.util.Getter;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.events.BukkitEvent;
import info.itsthesky.disky.tools.events.MessageEvent;
import info.itsthesky.disky.tools.object.Emote;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import info.itsthesky.disky.tools.section.DiSkySection;
import info.itsthesky.disky.tools.section.WaiterListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class SectionReact extends DiSkySection {

    private Expression<Emote> exprReact;
    private Expression<UpdatingMessage> exprMessage;
    private Expression<JDA> exprBot;

    static {
        register(
                "react to [the] [message] %message% with [emote] %emote% [using %-bot%] [to run]",
                SectionReact.class
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprMessage = (Expression<UpdatingMessage>) exprs[0];
        exprReact = (Expression<Emote>) exprs[1];
        exprBot = (Expression<JDA>) exprs[2];
        return init("react section", SectionReactEvent.class, parseResult);
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, event -> {
            Emote emote = exprReact.getSingle(e);
            UpdatingMessage tempMessage = exprMessage.getSingle(e);
            JDA bot = Utils.verifyVar(e, exprBot);
            if (tempMessage == null || emote == null) return;
            Message message = tempMessage.getMessage();

            if (bot != null)
                message = bot.getTextChannelById(message.getId()).retrieveMessageById(message.getId()).complete();

            if (emote.isEmote()) {
                message.addReaction(emote.getEmote()).queue(null, DiSkyErrorHandler::logException);
            } else {
                message.addReaction(emote.getName()).queue(null, DiSkyErrorHandler::logException);
            }

            String idToCompare = message.getJDA().getSelfUser().getId();
            Message finalMessage = message;
            Object vars = Variables.removeLocals(event);
            Variables.setLocalVariables(event, vars);
            WaiterListener.events.add(new WaiterListener.WaitingEvent<>(
                    GuildMessageReactionAddEvent.class,
                    ev -> Emote.fromReaction(ev.getReaction().getReactionEmote()).getName().equalsIgnoreCase(emote.getName())
                            && finalMessage.getId().equalsIgnoreCase(ev.getMessageId())
                            && !idToCompare.equalsIgnoreCase(ev.getUser().getId()),
                    ev -> {
                        SectionReactEvent sectionEvent = new SectionReactEvent(ev);
                        System.out.println(sectionEvent.isCancelled());
                        if (sectionEvent.isCancelled())
                            ev.getReaction().removeReaction(ev.getUser()).queue(null, DiSkyErrorHandler::logException);
                        Variables.setLocalVariables(sectionEvent, vars);
                        runSection(sectionEvent);
                    }
            ));
        });
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "react to message" + exprMessage.toString(e, debug) + " with reaction " + exprReact.toString(e, debug);
    }

    static {
        EventValues.registerEventValue(SectionReactEvent.class, Member.class, new Getter<Member, SectionReactEvent>() {
            @Override
            public Member get(SectionReactEvent event) {
                return event.JDAEvent.getMember();
            }
        }, 0);

        EventValues.registerEventValue(SectionReactEvent.class, User.class, new Getter<User, SectionReactEvent>() {
            @Override
            public User get(SectionReactEvent event) {
                return event.JDAEvent.getUser();
            }
        }, 0);

        EventValues.registerEventValue(SectionReactEvent.class, JDA.class, new Getter<JDA, SectionReactEvent>() {
            @Override
            public JDA get(SectionReactEvent event) {
                return event.JDAEvent.getJDA();
            }
        }, 0);

        EventValues.registerEventValue(SectionReactEvent.class, GuildChannel.class, new Getter<GuildChannel, SectionReactEvent>() {
            @Override
            public GuildChannel get(SectionReactEvent event) {
                return event.JDAEvent.getChannel();
            }
        }, 0);

        EventValues.registerEventValue(SectionReactEvent.class, TextChannel.class, new Getter<TextChannel, SectionReactEvent>() {
            @Override
            public TextChannel get(SectionReactEvent event) {
                return event.JDAEvent.getChannel();
            }
        }, 0);

        EventValues.registerEventValue(SectionReactEvent.class, UpdatingMessage.class, new Getter<UpdatingMessage, SectionReactEvent>() {
            @Override
            public UpdatingMessage get(SectionReactEvent event) {
                return UpdatingMessage.from(event.JDAEvent.getMessageId());
            }
        }, 0);
    }

    public static class SectionReactEvent extends BukkitEvent implements MessageEvent, Cancellable {

        private final GuildMessageReactionAddEvent JDAEvent;

        public SectionReactEvent(GuildMessageReactionAddEvent event) {
            this.JDAEvent = event;
        }

        @Override
        public MessageChannel getMessageChannel() {
            return JDAEvent.getChannel();
        }

        private boolean cancelled;

        @Override
        public boolean isCancelled() {
            return cancelled;
        }

        @Override
        public void setCancelled(boolean cancel) {
            cancelled = cancel;
        }
    }
}
