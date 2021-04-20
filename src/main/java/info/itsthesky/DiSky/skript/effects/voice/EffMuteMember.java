package info.itsthesky.DiSky.skript.effects.voice;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.DiSky.tools.DiSkyErrorHandler;
import info.itsthesky.DiSky.tools.Utils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import org.bukkit.event.Event;

@Name("Mute / Unmute Member")
@Description("Mute or unmute a member in a guild.")
@Examples("mute event-member")
@Since("1.9")
public class EffMuteMember extends Effect {

    static {
        Skript.registerEffect(EffMuteMember.class,
                "["+ Utils.getPrefixName() +"] mute [discord] %member% [in guild] [(with|via|using) [bot] %-string/bot%]",
                "["+ Utils.getPrefixName() +"] unmute [discord] %member% [in guild] [(with|via|using) [bot] %-string/bot%]"
        );
    }

    private Expression<Member> exprMember;
    private Expression<Object> exprBot;
    private int pattern;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprMember = (Expression<Member>) exprs[0];
        if (exprs.length != 1) exprBot = (Expression<Object>) exprs[1];
        pattern = matchedPattern;
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            Member member = exprMember.getSingle(e);
            if (!Utils.areJDASimilar(member.getJDA(), exprBot == null ? null : exprBot.getSingle(e))) return;
            if (member == null) return;
            if (pattern == 0) {
                member.mute(true).queue(null, DiSkyErrorHandler::logException);
            } else {
                member.mute(false).queue(null, DiSkyErrorHandler::logException);
            }
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "mute member " + exprMember.toString(e, debug) + exprBot == null ? "" : " with bot " + exprBot.toString(e, debug);
    }

}
