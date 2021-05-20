package info.itsthesky.disky.skript.effects.bot;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.managers.music.AudioUtils;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.internal.utils.PermissionUtil;
import org.bukkit.event.Event;

@Name("Make Bot Join Voice Channel")
@Description("Make the specific bot join a voice channel.")
@Examples("make bot named \"MyBot\" join event-channel")
@Since("1.12")
public class EffMakeBotJoinVoice extends Effect {

    static {
        Skript.registerEffect(EffMakeBotJoinVoice.class,
                "["+ Utils.getPrefixName() +"] make %bot% join [the] [channel] %channel/voicechannel%");
    }

    private Expression<JDA> exprBot;
    private Expression<GuildChannel> exprChannel;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprBot = (Expression<JDA>) exprs[0];
        exprChannel = (Expression<GuildChannel>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            JDA bot = exprBot.getSingle(e);
            GuildChannel channel = exprChannel.getSingle(e);
            if (bot == null || channel == null) return;
            if (!channel.getType().equals(ChannelType.VOICE)) return;
            if (!Utils.areJDASimilar(bot, channel.getGuild())) return;
            AudioUtils.connectToFirstVoiceChannel(channel.getGuild().getAudioManager(), (VoiceChannel) channel);
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "make " + exprBot.toString(e, debug) + " join " + exprChannel.toString(e, debug);
    }

}
