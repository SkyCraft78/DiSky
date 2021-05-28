package info.itsthesky.disky.skript.events.skript.slashcommand;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.tools.StaticData;
import info.itsthesky.disky.tools.object.messages.Channel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Slash Command Option")
@Description("Get an option, from a Slash command event, with the specific id")
@Since("1.5")
public class ExprCommandOption extends SimpleExpression<Object> {

    public static Object commandCore;

    static {
        Skript.registerExpression(ExprCommandOption.class, Object.class, ExpressionType.SIMPLE,
                "[the] [slash] [command] option [with id] %string%"
        );
    }

    private Expression<String> exprID;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.exprID = (Expression<String>) exprs[0];
        return true;
    }

    @Nullable
    @Override
    protected Object[] get(Event e) {
        String id = exprID.getSingle(e);
        if (id == null) return new Object[0];
        if (e instanceof EventSlashCommand) {
            OptionMapping option = StaticData.lastSlashCommandEvent.getOption(id);
            if (option == null) return new Object[0];
            switch (option.getType()) {
                case CHANNEL:
                    return new Channel[] {new Channel(option.getAsMessageChannel())};
                case USER:
                    return new User[] {option.getAsUser()};
                case ROLE:
                    return new Role[] {option.getAsRole()};
                case BOOLEAN:
                    return new Boolean[] {option.getAsBoolean()};
                case INTEGER:
                    return new Long[] {option.getAsLong()};
                default:
                    return new String[] {option.getAsString()};
            }
        }
        DiSky.getInstance().getLogger()
                .warning("DiSky can't get option of slash command from a non slash command event!");
        return new Object[0];
    }
    @Override
    public Class<?> getReturnType() { return Object.class; }
    @Override
    public boolean isSingle() { return true; }
    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the command core";
    }
}
