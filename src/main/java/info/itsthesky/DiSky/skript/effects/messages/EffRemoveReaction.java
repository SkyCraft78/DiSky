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
import info.itsthesky.DiSky.tools.DiSkyErrorHandler;
import info.itsthesky.DiSky.tools.Utils;
import info.itsthesky.DiSky.tools.object.Emote;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.event.Event;

@Name("Remove Reaction")
@Description("Remove a specific reaction, from user on message.")
@Examples({
        "on reaction add:",
        "\tremove event-emote added by event-user from event-message"
})
@Since("1.3")
public class EffRemoveReaction extends Effect {

    static {
        Skript.registerEffect(EffRemoveReaction.class,
                "["+ Utils.getPrefixName() +"] (remove|delete) %emotes% added by %user/member% from %message%");
    }

    private Expression<Emote> exprEmote;
    private Expression<Object> exprUser;
    private Expression<Message> exprMessage;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.exprEmote = (Expression<Emote>) exprs[0];
        this.exprUser = (Expression<Object>) exprs[1];
        this.exprMessage = (Expression<Message>) exprs[2];
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            Object entity = exprUser.getSingle(e);
            Message message = exprMessage.getSingle(e);
            if (entity == null || message == null) return;

            User user;
            if (entity instanceof User) {
                user = (User) entity;
            } else {
                user = ((Member) entity).getUser();
            }

            for (MessageReaction messageReaction : message.getReactions()) {
                for (Emote emote : this.exprEmote.getAll(e)) {
                    if (messageReaction.getReactionEmote().getName().equalsIgnoreCase(emote.getName())) {
                        messageReaction.removeReaction(user).queue();
                    }
                }
            }
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "remove the event-reaction from the user";
    }

}
