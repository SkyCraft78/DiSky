package info.itsthesky.disky.skript.scope;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.EffectSection;
import net.dv8tion.jda.api.JDABuilder;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Bot Builder")
@Description("This scope allow you to create a new bot with advanced options, such as intents.")
@Since("1.14")
public class ScopeBotBuilder extends EffectSection {

    public static JDABuilder lastBuilder;

    static {
        Skript.registerCondition(ScopeBotBuilder.class, "make [new] [discord] bot");
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (checkIfCondition()) {
            return false;
        }
        if (!hasSection()) {
            return false;
        }
        loadSection(true);
        return true;
    }

    @Override
    protected void execute(Event e) {
        lastBuilder = JDABuilder.createDefault("dsq");
        runSection(e);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "make new bot";
    }

}
