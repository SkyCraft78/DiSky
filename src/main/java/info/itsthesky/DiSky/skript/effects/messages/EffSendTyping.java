package info.itsthesky.disky.skript.effects.messages;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky.tools.AsyncEffect;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.event.Event;

@Name("Send Typing")
@Description("Make a bot \"typing\" in a text channel. You can enable it one time, and it automatically expire after 10 seconds.")
@Examples("discord command typing:\n" +
        "\tprefixes: %\n" +
        "\ttrigger:\n" +
        "\t\tsend bot typing in event-channel")
@Since("1.5.2")
public class EffSendTyping extends AsyncEffect {

    static {
        Skript.registerEffect(EffSendTyping.class,
                "["+ Utils.getPrefixName() +"] (make|send) [bot] [%-bot%] typing in [the] [channel] %channel/textchannel%");
    }

    private Expression<Object> exprEntity;
    private Expression<JDA> exprBot;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprBot = (Expression<JDA>) exprs[0];
        exprEntity = (Expression<Object>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            Object entity = exprEntity.getSingle(e);
            if (entity == null) return;
            if (exprBot != null) if (!Utils.areJDASimilar(((GuildChannel) entity).getJDA(), exprBot.getSingle(e))) return;
            if (entity instanceof TextChannel) ((TextChannel) entity).sendTyping().queue();
            if (entity instanceof GuildChannel && ((GuildChannel) entity).getType().equals(ChannelType.TEXT)) ((TextChannel) entity).sendTyping().queue();
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "make the bot typing in channel " +exprEntity.toString(e, debug);
    }

}
