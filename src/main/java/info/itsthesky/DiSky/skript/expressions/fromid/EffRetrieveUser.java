package info.itsthesky.disky.skript.expressions.fromid;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.effects.Delay;
import ch.njol.skript.lang.*;
import ch.njol.skript.timings.SkriptTimings;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.managers.BotManager;
import info.itsthesky.disky.skript.expressions.messages.ExprLastMessage;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.RestAction;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Retrieve User")
@Description("Retrieve a user from its ID, and store it in a variable.")
@Examples("retrieve user with id \"388744165443371009\" and store it in {_user}")
@Since("1.13")
public class EffRetrieveUser extends Effect {

    static {
        Skript.registerEffect(EffRetrieveUser.class,
                "["+ Utils.getPrefixName() +"] retrieve [the] user with [the] id %string% and store (it|the user) in %-object%");
    }

    private Expression<String> exprID;
    private Variable<?> variable;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        Utils.setHasDelayBefore(Kleenean.TRUE);
        exprID = (Expression<String>) exprs[0];
        Expression<?> var = exprs[1];
        if (var != null && !(var instanceof Variable)) {
            Skript.error("Cannot store the message in a non-variable expression");
            return false;
        } else {
            variable = (Variable<?>) var;
        }
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(Event e) {
        String input = exprID.getSingle(e);
        if (input == null || !Utils.isNumeric(input)) return getNext();
        debug(e, true);

        Object localVars = Variables.removeLocals(e); // Back up local variables
        Delay.addDelayedEvent(e); // Mark this event as delayed

        if (!Skript.getInstance().isEnabled()) // See https://github.com/SkriptLang/Skript/issues/3702
            return null;

        BotManager.getFirstBot().retrieveUserById(input).queue(
                user -> runConsumer(user, e, localVars),
                ex -> runConsumer(null, e, localVars)
        );

        return null;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "retrieve user with id " + exprID.toString(e, debug) + " and store it in " + variable.toString(e, debug);
    }

    private void runConsumer(@Nullable final User user, final Event e, final Object localVars) {
        if (localVars != null)
            Variables.setLocalVariables(e, localVars);

        if (variable != null && user != null) {
            variable.change(e, new Object[] {user}, Changer.ChangeMode.SET);
        }

        if (getNext() != null) {
            Object vars = Variables.removeLocals(e); // Back up local variables
            Bukkit.getScheduler().runTask(Skript.getInstance(), () -> { // Walk to next item synchronously
                Object timing = null;
                if (SkriptTimings.enabled()) { // getTrigger call is not free, do it only if we must
                    Trigger trigger = getTrigger();
                    if (trigger != null) {
                        timing = SkriptTimings.start(trigger.getDebugLabel());
                    }
                }
                Variables.setLocalVariables(e, vars);
                TriggerItem.walk(getNext(), e);
                Variables.removeLocals(e); // Clean up local vars, we may be exiting now
                SkriptTimings.stop(timing); // Stop timing if it was even started
            });
        } else {
            Variables.removeLocals(e);
        }
    }

    @Override
    protected void execute(Event e) { }
}
