package info.itsthesky.disky.skript.effects.messages;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.skript.expressions.ExprEmoji;
import info.itsthesky.disky.tools.async.AsyncEffect;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.Emote;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.event.Event;

import java.util.function.Predicate;

@Name("Remove Reaction")
@Description("Remove a specific reaction, from user on message.")
@Examples({
        "on reaction add:",
        "\tremove event-emote added by event-user from event-message"
})
@Since("1.3")
public class EffRemoveReaction extends AsyncEffect {

    static {
        Skript.registerEffect(EffRemoveReaction.class,
                "["+ Utils.getPrefixName() +"] (remove|delete) %emotes% added by %user/member/bot% from %message%");
    }

    private Expression<Emote> exprEmote;
    private Expression<Object> exprUser;
    private Expression<UpdatingMessage> exprUpdatingMessage;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.exprEmote = (Expression<Emote>) exprs[0];
        this.exprUser = (Expression<Object>) exprs[1];
        this.exprUpdatingMessage = (Expression<UpdatingMessage>) exprs[2];
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            Object entity = exprUser.getSingle(e);
            UpdatingMessage message = exprUpdatingMessage.getSingle(e);
            if (entity == null || message == null) return;

            User user;
            if (entity instanceof User) {
                user = (User) entity;
            } else if (entity instanceof Member) {
                user = ((Member) entity).getUser();
            } else {
                user = message.getMessage().getGuild().getMemberById(((JDA) entity).getSelfUser().getId()).getUser();
            }

            for (MessageReaction messageReaction : message.getMessage().getReactions()) {
                for (Emote emote : this.exprEmote.getAll(e)) {
                    if (Utils.areEmojiSimilar(messageReaction.getReactionEmote(), emote)) {
                        messageReaction.removeReaction(user).queue(null, DiSkyErrorHandler::logException);
                    }
                }
            }
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "remove "+ exprEmote.toString(e, debug) +" added by " + exprUser.toString(e, debug) + " from " + exprUpdatingMessage.toString(e, debug);
    }

}
