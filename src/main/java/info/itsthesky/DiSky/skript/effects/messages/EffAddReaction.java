package info.itsthesky.DiSky.skript.effects.messages;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.DiSky.managers.BotManager;
import info.itsthesky.DiSky.tools.DiSkyErrorHandler;
import info.itsthesky.DiSky.tools.Utils;
import info.itsthesky.DiSky.tools.object.Emote;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import org.bukkit.event.Event;

@Name("Add Reaction")
@Description("Add a specific reaction, on message.")
@Examples("add reaction \"smile\" to event-message")
@Since("1.3")
public class EffAddReaction extends Effect {

    static {
        Skript.registerEffect(EffAddReaction.class,
                "["+ Utils.getPrefixName() +"] (add|append) %emotes% to [(message|reactions of)] %message% with [bot] %string/bot%");
    }

    private Expression<Emote> exprEmote;
    private Expression<Message> exprMessage;
    private Expression<Object> exprBot;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.exprEmote = (Expression<Emote>) exprs[0];
        this.exprMessage = (Expression<Message>) exprs[1];
        this.exprBot = (Expression<Object>) exprs[2];
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            Emote[] emotes = exprEmote.getAll(e);
            Object bot = exprBot.getSingle(e);
            Message message = exprMessage.getSingle(e);
            if (emotes == null || bot == null || message == null) return;
            if (!Utils.areJDASimilar(message.getJDA(), bot)) return;

            for (Emote emote : emotes) {
                if (emote.isEmote()) {
                    message.addReaction(emote.getEmote()).queue();
                } else {
                    message.addReaction(emote.getName()).queue();
                }
            }
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "add reaction " + exprEmote.toString(e, debug) + " to message " + exprMessage.toString(e, debug) + " with bot " + exprBot.toString(e, debug);
    }

}
