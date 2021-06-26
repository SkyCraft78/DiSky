package info.itsthesky.disky.skript.effects;

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
import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Clone Discord Entity")
@Description("Clone an existing discord entity (channel, category or role) in another guild.")
@Examples("discord command clone <textchannel>:\n" +
        "\tprefixes: !\n" +
        "\tpermissions: administrator\n" +
        "\tpermission message: :x: You don't have enough permission!\n" +
        "\ttrigger:\n" +
        "\t\tclone arg-1 and store it in {_c}\n" +
        "\t\treply with \"Channel cloned: %mention tag of {_c}%\"")
@Since("1.5.3")
public class EffCloneEntity extends Effect {

    static {
        Skript.registerEffect(EffCloneEntity.class,
                "["+ Utils.getPrefixName() +"] clone [discord] [entity] %role/textchannel/channel/category/voicechannel% [in [the] [guild] %-guild%] [and store (it|the entity) in %-object%]");
    }

    private Expression<Object> exprEntity;
    private Expression<Guild> exprGuild;
    private Variable<?> var;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        Utils.setHasDelayBefore(Kleenean.TRUE);
        exprEntity = (Expression<Object>) exprs[0];
        exprGuild = (Expression<Guild>) exprs[1];
        Expression<?> var = exprs[2];
        if (var != null && !(var instanceof Variable)) {
            Skript.error("Cannot store the cloned entity in a non-variable expression");
            return false;
        } else {
            this.var = (Variable<?>) var;
            if (this.var.isList()) {
                Skript.error("Cannot store a single entity in a list variable!");
                return false;
            }
        }
        return true;
    }

    @Override
    protected @Nullable TriggerItem walk(Event e) {
        Object entity = exprEntity.getSingle(e);
        Guild guild = Utils.verifyVar(e, exprGuild);
        if (entity == null) return getNext();
        debug(e, true);

        Object _localVars = null;
        if (DiSky.SkriptUtils.MANAGE_LOCALES)
            _localVars = Variables.removeLocals(e); // Back up local variables
        Object localVars = _localVars;

        Delay.addDelayedEvent(e); // Mark this event as delayed

        if (!Skript.getInstance().isEnabled()) // See https://github.com/SkriptLang/Skript/issues/3702
            return null;

        if (entity instanceof GuildChannel) {
            ((GuildChannel) entity).createCopy().queue(
                    cloned -> runConsumer(cloned, e, localVars),
                    ex -> runConsumer(null, e, localVars));
        } else {
            ((Role) entity).createCopy().queue(
                    cloned -> runConsumer(cloned, e, localVars),
                    ex -> runConsumer(null, e, localVars));
        }

        return null;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "clone discord entity " + exprEntity.toString(e, debug);
    }

    private void runConsumer(@Nullable final Object entity, final Event e, final Object localVars) {
        if (DiSky.SkriptUtils.MANAGE_LOCALES && localVars != null)
            Variables.setLocalVariables(e, localVars);

        if (DiSky.SkriptUtils.MANAGE_LOCALES && var != null && entity != null) {
            var.change(e, new Object[] {entity}, Changer.ChangeMode.SET);
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
                if (DiSky.SkriptUtils.MANAGE_LOCALES)
                    Variables.setLocalVariables(e, vars);
                TriggerItem.walk(getNext(), e);
                if (DiSky.SkriptUtils.MANAGE_LOCALES)
                    Variables.removeLocals(e); // Clean up local vars, we may be exiting now
                SkriptTimings.stop(timing); // Stop timing if it was even started
            });
        } else {
            if (DiSky.SkriptUtils.MANAGE_LOCALES)
                Variables.removeLocals(e);
        }
    }

    @Override
    protected void execute(Event e) { }
}
