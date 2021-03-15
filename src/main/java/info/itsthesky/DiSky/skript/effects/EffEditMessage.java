package info.itsthesky.DiSky.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.DiSky.tools.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import org.bukkit.event.Event;

@Name("Edit Message")
@Description("Edit any message from the bot with new message or embed.")
@Examples("reply with \":v: Custom message ...\" and store it in {_msg}\n" +
        "wait 3 second\n" +
        "edit message {_msg} to show \":x: ... has been edited\"")
@Since("1.2")
public class EffEditMessage extends Effect {

    static {
        Skript.registerEffect(EffEditMessage.class,
                "["+ Utils.getPrefixName() +"] edit [discord] [message] %message% (with|to show) [new (embed|string)] %embed/string%");
    }

    private Expression<Message> exprMessage;
    private Expression<Object> exprNew;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprMessage = (Expression<Message>) exprs[0];
        exprNew = (Expression<Object>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        Message message = exprMessage.getSingle(e);
        Object newValue = exprNew.getSingle(e);
        if (message == null || newValue == null) return;
        if (newValue instanceof EmbedBuilder) {
            message.editMessage(((EmbedBuilder) newValue).build()).queue();
        } else {
            message.editMessage(newValue.toString()).queue();
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "edit message " + exprMessage.toString(e, debug) + " with " + exprNew.toString(e, debug);
    }

}
