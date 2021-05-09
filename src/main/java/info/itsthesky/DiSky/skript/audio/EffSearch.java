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
@Examples("search youtube for \"food battle start\" in event-guild and store the results in {_r::*} # Best epic music ever >:D")
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