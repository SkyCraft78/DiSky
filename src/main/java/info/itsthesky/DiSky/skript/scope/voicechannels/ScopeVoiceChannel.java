package info.itsthesky.disky.skript.scope.voicechannels;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.EffectSection;
import info.itsthesky.disky.tools.object.VoiceChannelBuilder;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Voice Channel Builder")
@Description("This builder allow you to make voice channel easily")
@Since("1.6")
@Examples("")
public class ScopeVoiceChannel extends EffectSection {

    public static VoiceChannelBuilder lastBuilder;

    static {
        Skript.registerCondition(ScopeVoiceChannel.class, "make [new] [discord] voice[ ][-][ ]channel");
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
        lastBuilder = new VoiceChannelBuilder();
        runSection(e);

    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "make new text channel";
    }

}
