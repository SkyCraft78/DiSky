package info.itsthesky.disky.skript.effects.guild;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.AsyncEffect;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

@Name("Unban Member")
@Description("Unban an user from a specific guild.")
@Examples("unban user with id \"388744165443371009\" from event-guild")
@Since("1.13")
public class EffUnbanUser extends AsyncEffect {

    static {
        Skript.registerEffect(EffUnbanUser.class,
                "["+ Utils.getPrefixName() +"] unban [the] [user] %user% from [the] [guild] %guild%");
    }

    private Expression<User> exprUser;
    private Expression<Guild> exprGuild;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprUser = (Expression<User>) exprs[0];
        exprGuild = (Expression<Guild>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            User user = exprUser.getSingle(e);
            Guild guild = exprGuild.getSingle(e);
            if (user == null || guild == null) return;
            guild.unban(user).queue();
        });
    }

    @Override
    public @NotNull String toString(Event e, boolean debug) {
        return "unban user " + exprUser.toString(e, debug) + " from guild " + exprGuild.toString(e, debug);
    }

}
