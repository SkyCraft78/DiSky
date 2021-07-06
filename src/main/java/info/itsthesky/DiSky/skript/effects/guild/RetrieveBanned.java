package info.itsthesky.disky.skript.effects.guild;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.WaiterEffect;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Name("Guild Banned Users")
@Description("Retrieve every banned user and store them in a list.")
@Examples("retrieve banned users of event-guild and store them in {_l::*}")
@Since("2.0")
public class RetrieveBanned extends WaiterEffect {

    static {
        Skript.registerEffect(RetrieveBanned.class,
                "["+ Utils.getPrefixName() +"] retrieve [ban[ned]] user[s] from [the] [guild] %guild% and store (them|the [banned] users) in %-objects%");
    }

    private Expression<Number> exprAmount;
    private Expression<Guild> exprGuild;
    private Variable<?> variable;

    @SuppressWarnings("unchecked")
    @Override
    public boolean initEffect(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprAmount = (Expression<Number>) exprs[0];
        exprGuild = (Expression<Guild>) exprs[1];
        Expression<?> var = exprs[2];
        if (var != null && !(var instanceof Variable)) {
            Skript.error("Cannot store the message in a non-variable expression");
            return false;
        } else {
            variable = (Variable<?>) var;
            if (!variable.isList()) {
                Skript.error("Cannot store a list of messages into a non-list variable!");
                return false;
            }
        }
        return true;
    }

    @Override
    public void runEffect(Event e) {
        Number amount = exprAmount.getSingle(e);
        Guild guild = exprGuild.getSingle(e);
        if (amount == null || guild == null) return;

        pause(); // We pause the trigger execution
        Utils.handleRestAction(
                guild.retrieveBanList(),
                bans -> {
                    List<User> users = bans.stream().map(Guild.Ban::getUser).collect(Collectors.toList());
                    if (variable != null)
                        variable.change(e, users.toArray(new User[0]), Changer.ChangeMode.SET);
                    restart(); // We change the next trigger item and resume the trigger execution
                },
                new ArrayList<>()
        );
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "retrieve banned users from guild " + exprGuild.toString(e, debug) +" and store them in " + variable.toString(e, debug);
    }

    @Override
    protected void execute(Event e) { }
}
