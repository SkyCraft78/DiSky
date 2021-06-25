package info.itsthesky.disky.skript.effects.messages;

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
import info.itsthesky.disky.tools.object.Emote;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import org.bukkit.event.Event;

@Name("Add Reaction")
@Description("Add a specific reaction, on message.")
@Examples("add reaction \"smile\" to event-message")
@Since("1.3")
public class EffAddReaction extends AsyncEffect {

    static {
        Skript.registerEffect(EffAddReaction.class,
                "["+ Utils.getPrefixName() +"] (add|append) %emotes% to [(message|reactions of)] %message% [with %-bot%]");
    }

    private Expression<Emote> exprEmote;
    private Expression<UpdatingMessage> exprMessage;
    private Expression<JDA> exprBot;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.exprEmote = (Expression<Emote>) exprs[0];
        this.exprMessage = (Expression<UpdatingMessage>) exprs[1];
        this.exprBot = (Expression<JDA>) exprs[2];
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            UpdatingMessage message = exprMessage.getSingle(e);
            Emote[] emotes = exprEmote.getAll(e);
            JDA bot = Utils.verifyVar(e, exprBot);
            if (message == null || emotes.length == 0) return;
            Message message1 = message.getMessage();
            if (bot != null)
                message1 = bot.getTextChannelById(message.getMessage().getId()).getHistory().getMessageById(message1.getId());

            for (Emote emote : emotes) {
                if (emote.isEmote()) {
                    message1.addReaction(emote.getEmote()).queue(null, DiSkyErrorHandler::logException);
                } else {
                    message1.addReaction(emote.getName()).queue(null, DiSkyErrorHandler::logException);
                }
            }
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "add reaction " + exprEmote.toString(e, debug) + " to message " + exprMessage.toString(e, debug) + " with " + exprBot.toString(e, debug);
    }

}
