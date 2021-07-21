package info.itsthesky.disky.skript.effects.grab;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky.tools.async.AsyncEffect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.event.Event;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;

@Name("Purge Amount of Message")
@Description("Grab all X latest message of a text channel and purge (= delete) them.")
@Examples("discord command purge [<number>]:\n" +
        "\tprefixes: !\n" +
        "\tpermissions: manage server\n" +
        "\tpermission message: **:x: You can't use that command!**\n" +
        "\ttrigger:\n" +
        "\t\tif arg-1 is not set:\n" +
        "\t\t\treply with \"**:x: You must define an amount!**\"\n" +
        "\t\t\tstop\n" +
        "\t\tpurge last arg messages from event-channel\n" +
        "\t\treply with \"**✅ Purged %arg-1% messages!**\"")
@Since("1.5.2")
public class EffPurgeMessages extends AsyncEffect {

    public static final HashMap<String, Boolean> PURGED_MESSAGES = new HashMap<>();

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

            TextChannel channel = null;
            if (entity instanceof TextChannel) channel = (TextChannel) entity;
            if (entity instanceof GuildChannel && ((GuildChannel) entity).getType().equals(ChannelType.TEXT)) channel = (TextChannel) entity;

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
            for (Message message : messages) PURGED_MESSAGES.put(message.getId(), true);
            channel.deleteMessages(messages).queue();
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "purge last " + exprAmount.toString(e, debug) + " messages from channel " + exprEntity.toString(e, debug);
    }

}
