package info.itsthesky.disky.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.async.WaiterEffect;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;

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
public class EffCloneEntity extends WaiterEffect<Object> {

    static {
        Skript.registerEffect(EffCloneEntity.class,
                "["+ Utils.getPrefixName() +"] clone [discord] [entity] %role/textchannel/channel/category/voicechannel% [in [the] [guild] %-guild%] [and store (it|the entity) in %-object%]");
    }

    private Expression<Object> exprEntity;
    private Expression<Guild> exprGuild;

    @SuppressWarnings("unchecked")
    @Override
    public boolean initEffect(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprEntity = (Expression<Object>) exprs[0];
        exprGuild = (Expression<Guild>) exprs[1];
        Expression<?> var = exprs[2];
        if (var != null && !(var instanceof Variable)) {
            Skript.error("Cannot store the cloned entity in a non-variable expression");
            return false;
        } else {
            setChangedVariable((Variable<Object>) var);
            if (changedVariable.isList()) {
                Skript.error("Cannot store a single entity in a list variable!");
                return false;
            }
        }
        return true;
    }

    @Override
    public void runEffect(Event e) {
        Object entity = exprEntity.getSingle(e);
        Guild guild = Utils.verifyVar(e, exprGuild);
        if (entity == null) return;

        if (entity instanceof GuildChannel) {
            Utils.handleRestAction(
                    ((GuildChannel) entity).createCopy(guild == null ? ((GuildChannel) entity).getGuild() : guild),
                    channel -> restart(new GuildChannel[] {channel}),
                    null
            );
        } else {
            Utils.handleRestAction(
                    ((Role) entity).createCopy(guild == null ? ((Role) entity).getGuild() : guild),
                    role -> restart(new Role[] {role}),
                    null
            );
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "clone discord entity " + exprEntity.toString(e, debug) + " and store it in " + changedVariable.toString(e, debug);
    }
}
