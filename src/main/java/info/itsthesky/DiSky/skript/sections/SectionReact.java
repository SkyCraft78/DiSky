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
import info.itsthesky.disky.skript.events.BitrateEvent;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.events.BukkitEvent;
import info.itsthesky.disky.tools.events.MessageEvent;
import info.itsthesky.disky.tools.object.Emote;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import info.itsthesky.disky.tools.section.DiSkySection;
import info.itsthesky.disky.tools.section.WaiterListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class SectionReact extends DiSkySection {

    private Expression<Emote> exprReact;
    private Expression<UpdatingMessage> exprMessage;
    private Expression<JDA> exprBot;
    private static SectionReactEvent lastEvent;
    private static GuildMessageReactionAddEvent lastJDAEvent;

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

    @Nullable
    @Override
    protected TriggerItem walk(Event e) {
        debug(e, true);

        Delay.addDelayedEvent(e); // Mark this event as delayed
        Object localVars = Variables.removeLocals(e); // Back up local variables

        if (!Skript.getInstance().isEnabled()) // See https://github.com/SkriptLang/Skript/issues/3702
            return null;

        Bukkit.getScheduler().runTask(Skript.getInstance(), () -> { // Walk to next item synchronously
            Object timing = null;
            if (SkriptTimings.enabled()) { // getTrigger call is not free, do it only if we must
                Trigger trigger = getTrigger();
                if (trigger != null) {
                    timing = SkriptTimings.start(trigger.getDebugLabel());
                }
            }

            DiSkyErrorHandler.executeHandleCode(e, event -> {
                Emote emote = exprReact.getSingle(e);
                UpdatingMessage tempMessage = exprMessage.getSingle(e);
                JDA bot = Utils.verifyVar(e, exprBot);
                if (tempMessage == null || emote == null) return;
                Message message = tempMessage.getMessage();

                if (bot != null)
                    message = bot.getTextChannelById(message.getId()).getHistory().getMessageById(message.getId());

                if (emote.isEmote()) {
                    message.addReaction(emote.getEmote()).queue(null, DiSkyErrorHandler::logException);
                } else {
                    message.addReaction(emote.getName()).queue(null, DiSkyErrorHandler::logException);
                }

                String idToCompare = message.getJDA().getSelfUser().getId();
                Message finalMessage = message;
                WaiterListener.events.add(new WaiterListener.WaitingEvent<>(
                        GuildMessageReactionAddEvent.class,
                        ev -> new Emote(ev.getReaction().getReactionEmote()).getName().equalsIgnoreCase(emote.getName())
                                && finalMessage.getId().equalsIgnoreCase(ev.getMessageId())
                                && !idToCompare.equalsIgnoreCase(ev.getUser().getId()),
                        ev -> {
                            lastEvent = new SectionReactEvent(ev);
                            lastJDAEvent = ev;
                            if (!(event instanceof Cancellable) || !((Cancellable) event).isCancelled()) {
                                ev.getReaction().removeReaction(ev.getUser()).queue(null, DiSkyErrorHandler::logException);
                            }
                            Variables.setLocalVariables(e, localVars);
                            runSection(e);
                        }
                ));
            });

            Variables.removeLocals(e); // Clean up local vars, we may be exiting now
            SkriptTimings.stop(timing); // Stop timing if it was even started
        });
        return null;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "react to message" + exprMessage.toString(e, debug) + " with reaction " + exprReact.toString(e, debug);
    }

    public static class SectionReactEvent extends BukkitEvent implements MessageEvent, Cancellable {

        static {
            EventValues.registerEventValue(SectionReactEvent.class, Member.class, new Getter<Member, SectionReactEvent>() {
                @Override
                public Member get(SectionReactEvent event) {
                    return event.JDAEvent.getMember();
                }
            }, 0);
        }

        private final GuildMessageReactionAddEvent JDAEvent;

        public SectionReactEvent(GuildMessageReactionAddEvent event) {
            this.JDAEvent = event;
        }

        @Override
        public MessageChannel getMessageChannel() {
            return lastEvent.JDAEvent.getChannel();
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
