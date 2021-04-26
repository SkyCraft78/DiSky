package info.itsthesky.DiSky.skript.expressions.messagebuilder;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.DiSky.managers.BotManager;
import info.itsthesky.DiSky.tools.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import org.bukkit.event.Event;

@Name("Append string / embed")
@Description("Append a string or an embed to a message builder (message builder can't have two and more embed!)")
@Examples("append \"My String\" to {_m}")
@Since("1.4")
public class EffAppend extends Effect {

    static {
        Skript.registerEffect(EffAppend.class,
                "["+ Utils.getPrefixName() +"] append %string/embed% to [message] [builder] %messagebuilder%");
    }

    private Expression<Object> exprEntity;
    private Expression<MessageBuilder> exprBuilder;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprEntity = (Expression<Object>) exprs[0];
        exprBuilder = (Expression<MessageBuilder>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        Object entity = exprEntity.getSingle(e);
        MessageBuilder builder = exprBuilder.getSingle(e);
        if (entity == null || builder == null) return;
        if (entity instanceof EmbedBuilder) {
            builder.setEmbed(
                    ((EmbedBuilder) entity).build()
            );
        } else {
            builder.append(entity.toString());
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "append " + exprEntity.toString(e, debug) + " to message builder " + exprBuilder.toString(e, debug);
    }

}
