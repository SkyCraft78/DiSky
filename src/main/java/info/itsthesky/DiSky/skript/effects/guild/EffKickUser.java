package info.itsthesky.disky.skript.effects.guild;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

@Name("Kick Member")
@Description("Kick a specific member from his guild.")
@Examples("kick event-member because of \"ur so bad bro v2\"")
@Since("1.12")
public class EffKickUser extends Effect {

    static {
        Skript.registerEffect(EffKickUser.class,
                "["+ Utils.getPrefixName() +"] kick [the] [member] %member% [((for|with) [reason]|[be]cause [of]) %-string%]");
    }

    private Expression<Member> exprMember;
    private Expression<String> exprReason;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprMember = (Expression<Member>) exprs[0];
        if (exprs.length >= 2) exprReason = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            Member member = exprMember.getSingle(e);
            if (member == null) return;
            if (exprReason != null && exprReason.getSingle(e) != null) {
                member.kick(exprReason.getSingle(e)).queue(null, DiSkyErrorHandler::logException);
            } else {
                member.kick().queue(null, DiSkyErrorHandler::logException);
            }
        });
    }

    @Override
    public @NotNull String toString(Event e, boolean debug) {
        return "kick member " + exprMember.toString(e, debug) + (exprReason == null ? "" : " with reason " + exprReason.toString(e, debug));
    }

}
