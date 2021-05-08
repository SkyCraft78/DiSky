package info.itsthesky.DiSky.skript.effects.guild;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.DiSky.tools.DiSkyErrorHandler;
import info.itsthesky.DiSky.tools.Utils;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

@Name("Ban Member")
@Description("Ban a member from a specific guild. With an optional reason. The optional amount of days specified will be removed every messages.")
@Examples("ban event-member because of \"ur so bad bro\"")
@Since("1.12")
public class EffBanUser extends Effect {

    static {
        Skript.registerEffect(EffBanUser.class,
                "["+ Utils.getPrefixName() +"] ban [the] [member] %member% [removing %-number% [days] of message[s]] [((for|with) [reason]|[be]cause [of]) %-string%]");
    }

    private Expression<Member> exprMember;
    private Expression<Number> exprDays;
    private Expression<String> exprReason;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprMember = (Expression<Member>) exprs[0];
        if (exprs.length >= 2) exprDays = (Expression<Number>) exprs[1];
        if (exprs.length >= 3) exprReason = (Expression<String>) exprs[2];
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            Member member = exprMember.getSingle(e);
            if (member == null) return;
            if (exprReason != null && exprReason.getSingle(e) != null) {
                member.ban(exprDays != null && exprDays.getSingle(e) != null ? Utils.round(exprDays.getSingle(e).doubleValue()) : 0, exprReason.getSingle(e)).queue(null, DiSkyErrorHandler::logException);
            } else {
                member.ban(exprDays != null && exprDays.getSingle(e) != null ? Utils.round(exprDays.getSingle(e).doubleValue()) : 0).queue(null, DiSkyErrorHandler::logException);
            }
        });
    }

    @Override
    public @NotNull String toString(Event e, boolean debug) {
        return "ban member " + exprMember.toString(e, debug) + (exprDays != null ? " with " + exprDays.toString(e, debug) + " days removing messages " : "") + (exprReason == null ? "" : " with reason " + exprReason.toString(e, debug));
    }

}
