package info.itsthesky.disky.skript.audio;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.util.Kleenean;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.managers.music.AudioUtils;
import info.itsthesky.disky.tools.Utils;
import org.bukkit.event.Event;

import java.io.File;

@Name("Load Locale Audio")
@Description("Load a locale audio as an audio track. Supported format are MP3, FLAC, WAV, Matroska/WebM (AAC, Opus or Vorbis codecs), MP4/M4A (AAC codec), OGG streams (Opus, Vorbis and FLAC codecs), AAC streams and Stream playlists (M3U and PLS)")
@Since("1.11")
@Examples("discord command locale:\n" +
        "\tprefixes: !\n" +
        "\ttrigger:\n" +
        "\t\tload locale track from \"plugins/track.ogg\" and store it in {_track}\n" +
        "\t\tif {_track} is not set:\n" +
        "\t\t\treply with \":x: **Track not found!**\"\n" +
        "\t\t\tstop\n" +
        "\t\tplay {_track} in voice channel of event-member")
public class EffLocaleLoad extends Effect {

    static {
        Skript.registerEffect(EffLocaleLoad.class, "[" + Utils.getPrefixName() + "] load [locale] track from [the] [file] [path] %string% and store (it|the track) in %objects%");
    }

    private Expression<String> exprPath;
    private boolean isVarLocal;
    private Variable exprVar;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprPath = (Expression<String>) exprs[0];
        if (exprs.length != 1) {
            Expression<?> tempVar = exprs[1];
            if (tempVar instanceof Variable) {
                if (!((Variable) tempVar).isList()) {
                    exprVar = (Variable) tempVar;
                    return true;
                }
            }
            Skript.error("DiSky can't store a track in a non variable or non single variable input!");
            return false;
        }
        return true;

    }

    @Override
    public void execute(Event e) {
        String path = exprPath.getSingle(e);
        if (path == null) return;
        File file = new File(path);
        if (!file.exists()) {
            DiSky.getInstance().getLogger().severe("DiSky can't found the right locale file '"+path+"'!");
            return;
        }
        AudioTrack track = AudioUtils.loadFromFile(file);
        if (exprVar == null) return;
        Utils.setSkriptVariable(exprVar, track, e);
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "load locale file from " + exprPath.toString(e, debug) + " and store it in " + exprVar.toString(e, debug);
    }

}