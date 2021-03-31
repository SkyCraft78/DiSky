package info.itsthesky.DiSky.skript.effects.grab;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.DiSky.DiSky;
import info.itsthesky.DiSky.tools.DiSkyErrorHandler;
import info.itsthesky.DiSky.tools.Utils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.event.Event;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Name("Purge Amount of Message")
@Description("Grab all X latest message of a text channel and purge (= delete) them.")
@Examples("purge last 50 messages from event-channel")
@Since("1.5.2")
public class EffPurgeMessages extends Effect {

    static {
        Skript.registerEffect(EffPurgeMessages.class,
                "["+ Utils.getPrefixName() +"] purge [all] last[est] %number% messages from [the] [channel] %channel/textchannel%");
    }

    private Expression<Number> exprAmount;
    private Expression<Object> exprEntity;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprAmount = (Expression<Number>) exprs[0];
        exprEntity = (Expression<Object>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            Object entity = exprEntity.getSingle(e);
            Number amount = exprAmount.getSingle(e);
            if (entity == null || amount == null) return;
            TextChannel channel = Utils.checkChannel(entity);
            if (channel == null) return;
            if (Utils.round(amount.doubleValue()) < 0 && Utils.round(amount.doubleValue()) > 100) {
                DiSky.getInstance().getLogger()
                        .warning("DiSky can't purge more than 100 messages, and you're trying to purge "+Utils.round(amount.doubleValue())+"! Set to 100.");
                amount = 100;
            }
            int finalValue = Utils.round(amount.doubleValue());
            OffsetDateTime twoWeeksAgo = OffsetDateTime.now().minus(2, ChronoUnit.WEEKS);
            List<Message> messages = channel.getHistory().retrievePast(finalValue).complete();
            messages.removeIf(m -> m.getTimeCreated().isBefore(twoWeeksAgo));
            if (messages.isEmpty()) {
                return;
            }
            channel.deleteMessages(messages).complete();
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "purge last " + exprAmount.toString(e, debug) + " messages from channel " + exprEntity.toString(e, debug);
    }

}
