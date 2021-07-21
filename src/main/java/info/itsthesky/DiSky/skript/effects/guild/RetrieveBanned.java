package info.itsthesky.disky.skript.effects.guild;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.async.MultipleWaiterEffect;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

@Name("Guild Banned Users")
@Description("Retrieve every banned user and store them in a list.")
@Examples("retrieve banned users of event-guild and store them in {_l::*}")
@Since("2.0")
public class RetrieveBanned extends MultipleWaiterEffect<User> {

    static {
        Skript.registerEffect(RetrieveBanned.class,
                "["+ Utils.getPrefixName() +"] retrieve [ban[ned]] user[s] from [the] [guild] %guild% and store (them|the [banned] users) in %-objects%");
    }

    private Expression<Guild> exprGuild;

    @SuppressWarnings("unchecked")
    @Override
    public boolean initEffect(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprGuild = (Expression<Guild>) exprs[0];
        Expression<?> var = exprs[1];
        if (var != null && !(var instanceof Variable)) {
            Skript.error("Cannot store the message in a non-variable expression");
            return false;
        } else {
            setChangedVariable((Variable) var);
            if (!changedVariable.isList()) {
                Skript.error("Cannot store a list of messages into a non-list variable!");
                return false;
            }
        }
        return true;
    }

    @Override
    public void runEffect(Event e) {
        Guild guild = exprGuild.getSingle(e);
        if (guild == null) return;

        Utils.handleRestAction(
                guild.retrieveBanList(),
                bans -> restart(bans.stream().map(Guild.Ban::getUser).toArray(User[]::new)),
                new ArrayList<>()
        );
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "retrieve banned users from guild " + exprGuild.toString(e, debug) +" and store them in " + changedVariable.toString(e, debug);
    }

    @Override
    protected void execute(Event e) { }
}
