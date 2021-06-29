package info.itsthesky.disky.skript.audio;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky.managers.music.AudioUtils;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.bukkit.event.Event;

@Name("Connect Bot")
@Description("Connect a bot to a specific voice channel. Then, you can use the play effect to play something. Using the force part, it will simply disconnect the bot if it already connected.")
@Examples("discord command play [<string>]:\n" +
        "\tprefixes: *\n" +
        "\taliases: p\n" +
        "\ttrigger:\n" +
        "\t\tif arg 1 is not set:\n" +
        "\t\t\treply with \":x: **You __must__ specify an URL or a YouTube input!**\"\n" +
        "\t\t\tstop\n" +
        "\t\tif voice channel of event-member is not set:\n" +
        "\t\t\treply with \":x: **You __must__ be in a voice channel in order to join you!**\"\n" +
        "\t\t\tstop\n" +
        "\t\t\n" +
        "\t\t# Check if the event member who made the command is in the voice channel of the bot.\n" +
        "\t\tset {_bot} to self member of event-bot in event-guild\n" +
        "\t\tset {_force} to false\n" +
        "\t\tif voice channel of {_bot} is set:\n" +
        "\t\t\tdiscord id of voice channel of event-member is not discord id of voice channel of {_bot}\n" +
        "\t\t\treply with \":x: **Error, I'm already connected to another voice channel.**\"\n" +
        "\t\t\tstop\n" +
        "\t\t\n" +
        "\t\tconnect event-bot to voice channel of event-member\n" +
        "\t\t\n" +
        "\t\tsearch in youtube for arg-1 and store it in {_r::*}\n" +
        "\t\t# It mean it's a playlist and not a single track\n" +
        "\t\tif arg-1 contain \"list=\":\n" +
        "\t\t\tif {_r::*} is not set:\n" +
        "\t\t\t\treply with \":x: **Can't found that playlist!**\"\n" +
        "\t\t\t\tstop\n" +
        "\t\t\t# We play all tracks listed on the playlist inputted\n" +
        "\t\t\tplay {_r::*} in event-guild\n" +
        "\t\t\tmake embed:\n" +
        "\t\t\t\tset author of embed to \"Successfully added **%size of {_r::*}% tracks** to your queue!\"\n" +
        "\t\t\t\tset author icon of embed to avatar of event-member\n" +
        "\t\t\t\tset color of embed to orange\n" +
        "\t\t\t\tset author url of embed to arg-1\n" +
        "\t\t\treply with last embed\n" +
        "\t\telse:\n" +
        "\t\t\tif {_r::1} is not set:\n" +
        "\t\t\t\treply with \":x: **Can't found anything for the input '%arg-1%'!**\"\n" +
        "\t\t\t\tstop\n" +
        "\t\t\tplay {_r::1} in event-guild\n" +
        "\t\t\tset {_track} to {_r::1}\n" +
        "\t\t\tmake embed:\n" +
        "\t\t\t\tset title of embed to \"%{_track}%\"\n" +
        "\t\t\t\tset title url of embed to track url of {_track}\n" +
        "\t\t\t\tset color of embed to lime\n" +
        "\t\t\t\tset footer of embed to \"Executed by %discord name of event-member%\"\n" +
        "\t\t\t\tset thumbnail of embed to track thumbnail of {_track}\n" +
        "\t\t\t\tadd \"`•` Duration: %track duration of {_track}%\" to {_l::*}\n" +
        "\t\t\t\tadd \"`•` Author: %track author of {_track}%\" to {_l::*}\n" +
        "\t\t\t\tset description of embed to join {_l::*} with nl\n" +
        "\t\t\treply with last embed")
@Since("2.0")
public class EffConnect extends Effect {

    static {
        Skript.registerEffect(EffConnect.class, // [the] [bot] [(named|with name)] %string%
                "["+ Utils.getPrefixName() +"] [force] connect [the] %bot% to [the] [voice] [channel] %channel/voicechannel%",
                "["+ Utils.getPrefixName() +"] [force] make [the] %bot% join [the] [voice] [channel] %channel/voicechannel%"
                );
    }

    private Expression<JDA> exprBot;
    private Expression<GuildChannel> exprChannel;
    private boolean force;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprBot = (Expression<JDA>) exprs[0];
        exprChannel = (Expression<GuildChannel>) exprs[1];
        force = parseResult.expr.contains("force");
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void execute(Event e) {
        GuildChannel channel = exprChannel.getSingle(e);
        JDA bot = exprBot.getSingle(e);
        if (channel == null || bot == null) return;

        // Be sure the channel is got by the right JDA
        channel = bot.getVoiceChannelById(channel.getId());

        if (!channel.getType().equals(ChannelType.VOICE)) return;

        AudioUtils.connectToVoiceChannel(channel.getGuild().getAudioManager(), (VoiceChannel) channel, force);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return (force ? "force " : "") + "connect the bot " + exprBot.toString(e, debug) + " to channel " + exprChannel.toString(e, debug);
    }

}
