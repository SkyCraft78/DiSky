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
import info.itsthesky.disky.tools.object.Emote;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import info.itsthesky.disky.tools.section.DiSkySection;
import info.itsthesky.disky.tools.section.WaiterListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class SectionReact extends DiSkySection {

    private Expression<Emote> exprReact;
    private Expression<UpdatingMessage> exprMessage;
    private Expression<JDA> exprBot;

    static {
        //register("react to [the] [message] %message% with [emote] %emote% [using %-bot%] [to run]");
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprMessage = (Expression<UpdatingMessage>) exprs[0];
        exprReact = (Expression<Emote>) exprs[1];
        exprBot = (Expression<JDA>) exprs[2];
        return init("react section", SectionReactEvent.class);
    }

    @Nullable
    @Override
    protected TriggerItem walk(Event e) {
        debug(e, true);

        Delay.addDelayedEvent(e); // Mark this event as delayed
        Object localVars = Variables.removeLocals(e); // Back up local variables
        System.out.println("test 2");

        if (!Skript.getInstance().isEnabled()) // See https://github.com/SkriptLang/Skript/issues/3702
            return null;
        System.out.println("test 1");

        if (getNext() != null) {
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
                    System.out.println("debug 1");
                    if (tempMessage == null || emote == null) return;
                    Message message = tempMessage.getMessage();

                    if (bot != null)
                        message = bot.getTextChannelById(message.getId()).getHistory().getMessageById(message.getId());

                    System.out.println("debug 2");
                    if (emote.isEmote()) {
                        message.addReaction(emote.getEmote()).queue(null, DiSkyErrorHandler::logException);
                    } else {
                        message.addReaction(emote.getName()).queue(null, DiSkyErrorHandler::logException);
                    }
                    System.out.println("debug 3");

                    Message finalMessage = message;
                    System.out.println("debug 4");
                    WaiterListener.events.add(new WaiterListener.WaitingEvent<>(
                            GuildMessageReactionAddEvent.class,
                            ev -> new Emote(ev.getReaction().getReactionEmote()).equals(emote)
                                    && finalMessage.getId().equals(ev.getMessageId()),
                            ev -> {
                                System.out.println("debug 5");
                                runSection(e);
                            }
                    ));
                });

                Variables.removeLocals(e); // Clean up local vars, we may be exiting now
                SkriptTimings.stop(timing); // Stop timing if it was even started
            });
        } else {
            Variables.removeLocals(e);
        }
        return null;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "react to message" + exprMessage.toString(e, debug) + " with reaction " + exprReact.toString(e, debug);
    }

    public static class SectionReactEvent extends BukkitEvent {

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
    }
}
