package info.itsthesky.disky.skript.audio.manage;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;

@Name("Disconnect Bot From Guild")
@Description("Disconnect the bot from a specific guild.")
@Examples("discord command stop:\n" +
        "\tprefixes: *\n" +
        "\ttrigger:\n" +
        "\t\tstop current queue of event-guild\n" +
        "\t\tdisconnect bot of event-guild\n" +
        "\t\tmake embed:\n" +
        "\t\t\tset author of embed to \"Bye, See you next time \uD83D\uDC4B\"\n" +
        "\t\t\tset author icon of embed to avatar of event-member\n" +
        "\t\t\tset color of embed to red\n" +
        "\t\t\tset footer of embed to \"Executed by %discord name of event-member%\"\n" +
        "\t\treply with last embed")
@Since("1.9")
public class EffDisconnectBot extends Effect {

    static {
        Skript.registerEffect(EffDisconnectBot.class,
                "["+ Utils.getPrefixName() +"] (close connection|disconnect) [the] bot of [the] [guild] %guild%");
    }

    private Expression<Guild> exprGuild;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprGuild = (Expression<Guild>) exprs[0];
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void execute(Event e) {
        Guild guild = exprGuild.getSingle(e);
        if (guild == null) return;
        guild.getAudioManager().setSendingHandler(null);
        guild.getAudioManager().closeAudioConnection();
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "disconnect bot of guild " + exprGuild.toString(e, debug);
    }

}
