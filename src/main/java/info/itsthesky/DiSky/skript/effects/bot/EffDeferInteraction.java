package info.itsthesky.disky.skript.effects.bot;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.events.InteractionEvent;
import info.itsthesky.disky.tools.async.AsyncEffect;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.bukkit.event.Event;

import java.util.Arrays;

@Name("Defer Interaction")
@Description({"Let discord you've been receiving the interaction (work in buttons and slash commands events only) ONE TIME!",
"If you're replying in one of these event, the deferring is useless and you'll get an error.",
"Replying will automatically deferring the interaction!"})
@Examples("defer the interaction")
@Since("1.13")
public class EffDeferInteraction extends AsyncEffect {

    static {
        Skript.registerEffect(EffDeferInteraction.class,
                "["+ Utils.getPrefixName() +"] (defer|acknowledge) [the] interaction [event]");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!Arrays.asList(ScriptLoader.getCurrentEvents()[0].getInterfaces()).contains(InteractionEvent.class)) {
            Skript.error("Cannot defer an interaction in a non-interaction event! (slash command / button)");
            return false;
        }
        return true;
    }

    @Override
    protected void execute(Event e) {
        GenericInteractionCreateEvent interaction;
        try  {
            interaction = ((InteractionEvent) e).getInteractionEvent();
        } catch (ClassCastException exception) {
            Skript.error("[DiSky] Cannot cast a non-interaction event in a defer effect! Excepted slash command or button event, but got "+e.getEventName()+" event!");
            return;
        }
        if (interaction instanceof SlashCommandEvent) {
            ((GenericComponentInteractionCreateEvent) interaction).deferEdit().queue(null, DiSkyErrorHandler::logException);
        } else {
            interaction.deferReply().queue(null, DiSkyErrorHandler::logException);
        }
        if (interaction instanceof ButtonClickEvent) {
            ((ButtonClickEvent) interaction).deferEdit().queue();
        } else {
            interaction.deferReply().queue();
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "defer the interaction event";
    }

}
