package info.itsthesky.disky.skript.scope.role;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.RoleBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.event.Event;

@Name("Create role builder in Guild")
@Description("Create the role from a role builder in a specific guild")
@Examples("create role builder in event-guild\ncreate role in event-guild and store it in {_role}")
@Since("1.4")
public class EffCreateRole extends Effect {

    static {
        Skript.registerEffect(EffCreateRole.class,
                "["+ Utils.getPrefixName() +"] create [the] [role] [builder] %rolebuilder% in [the] [guild] %guild% [and store it in %-object%]");
    }

    private Expression<RoleBuilder> exprBuilder;
    private Expression<Guild> exprGuild;
    private Expression<Object> exprVar;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprBuilder = (Expression<RoleBuilder>) exprs[0];
        exprGuild = (Expression<Guild>) exprs[1];
        if (exprs.length == 2) return true;
        exprVar = (Expression<Object>) exprs[2];
        return true;
    }

    @Override
    protected void execute(Event e) {
        RoleBuilder builder = exprBuilder.getSingle(e);
        Guild guild = exprGuild.getSingle(e);
        if (builder == null || guild == null) return;
        Role role = builder.create(guild);
        if (exprVar == null) return;
        if (!(exprVar instanceof Variable)) return;
        Variable variable = (Variable) exprVar;
        Utils.setSkriptVariable(
                variable,
                role,
                e);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "create role using builder " + exprBuilder.toString(e, debug) + " in guild " + exprGuild.toString(e, debug);
    }

}
