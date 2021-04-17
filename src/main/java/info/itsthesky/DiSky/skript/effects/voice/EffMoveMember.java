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
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;

@Name("Move Member")
@Description("Move a member to another voice channel.")
@Examples("move event-member to voice channel with id \"818182473502294073\"")
@Since("1.8.1")
public class EffMoveMember extends Effect {

    static {
        Skript.registerEffect(EffMoveMember.class,
                "["+ Utils.getPrefixName() +"] move [discord] %member% to [the] [voice] [channel] %channel/voicechannel% [(with|via|using) [bot] %-string/bot%]"
        );
    }

    private Expression<Member> exprMember;
    private Expression<Object> exprChannel;
    private Expression<Object> exprBot;
    private int pattern;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprMember = (Expression<Member>) exprs[0];
        exprChannel = (Expression<Object>) exprs[1];
        if (exprs.length != 1) exprBot = (Expression<Object>) exprs[2];
        pattern = matchedPattern;
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            Member member = exprMember.getSingle(e);
            Object entity = exprChannel.getSingle(e);
            if (!Utils.areJDASimilar(member.getJDA(), exprBot == null ? null : exprBot.getSingle(e))) return;
            if (member == null || entity == null) return;
            if (entity instanceof VoiceChannel) {
                member.getGuild().moveVoiceMember(member, (VoiceChannel) entity).queue(null, DiSkyErrorHandler::logException);
            } else if (entity instanceof GuildChannel && ((GuildChannel) entity).getType().equals(ChannelType.VOICE)) {
                member.getGuild().moveVoiceMember(member, (VoiceChannel) entity).queue(null, DiSkyErrorHandler::logException);
            }
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "move member " + exprMember.toString(e, debug) + " to channel " + exprChannel.toString(e, debug) + exprBot == null ? "" : " with bot " + exprBot.toString(e, debug);
    }

}
