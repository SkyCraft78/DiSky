package info.itsthesky.disky.skript.audio;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.*;
import ch.njol.util.Kleenean;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky.managers.music.AudioUtils;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.AudioSite;
import org.bukkit.event.Event;

@Name("Search Audio")
@Description("Search audio track in an audio website (see Audio Site).")
@Since("1.9")
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
        "\t\tsearch in youtube for arg-1 and store it in {_r::*}\n" +
        "\t\t# It mean it's a playlist and not a single track\n" +
        "\t\tif arg-1 contain \"list=\":\n" +
        "\t\t\tif {_r::*} is not set:\n" +
        "\t\t\t\treply with \":x: **Can't found that playlist!**\"\n" +
        "\t\t\t\tstop\n" +
        "\t\t\t# We play all tracks listed on the playlist inputted\n" +
        "\t\t\tplay {_r::*} in voice channel of event-member\n" +
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
        "\t\t\tplay {_r::1} in voice channel of event-member\n" +
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
public class EffSearch extends Effect {

    static {
        Skript.registerEffect(EffSearch.class, "[" + Utils.getPrefixName() + "] search [in] [[web]site] %audiosite% for [the] [input] %string% and store (it|the results) in %objects%");
    }

    private Expression<AudioSite> exprSite;
    private Expression<String> exprQueries;
    private boolean isVarLocal;
    private VariableString exprVar;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprSite = (Expression<AudioSite>) exprs[0];
        exprQueries = (Expression<String>) exprs[1];
        if (exprs[2] != null) {
            Expression<?> expr = exprs[2];
            if (expr instanceof Variable) {
                Variable<?> varExpr = (Variable<?>) expr;
                if (varExpr.isList()) {
                    exprVar = Utils.getVariableName(varExpr);
                    isVarLocal = varExpr.isLocal();
                    return true;
                }
            }
            Skript.error("DiSky need a list for storing results, however, " + expr + " is not a list variable");
            return false;
        }
        return true;

    }

    @Override
    public void execute(Event e) {
        AudioSite site = exprSite.getSingle(e);
        if (site == null) return;
        String query = exprQueries.getSingle(e);
        AudioTrack[] results = AudioUtils.search(query, site);
        if (exprVar == null) return;
        Utils.setList(exprVar.toString(e), e, isVarLocal, (Object[]) results);
    }

    @Override
    public String toString(Event event, boolean debug) {
        return "search " + exprSite.toString(event, debug) + " for " + exprQueries.toString(event, debug) + " and store the results in " + exprVar.toString(event, debug);
    }

}