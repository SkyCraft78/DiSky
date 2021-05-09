package info.itsthesky.disky.skript.expressions.member;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;

@Name("Members of Voice Channel")
@Description("Get all current members of a voice channel.")
@Examples("members of voice channel with id \"818182473502294073\"")
@Since("1.10")
public class ExprMembersVoice extends SimpleExpression<Member> {

    static {
        Skript.registerExpression(ExprMembersVoice.class, Member.class, ExpressionType.SIMPLE,
                "members (in|of) [the] [voice] [channel] %channel/voicechannel%");
    }

    private Expression<GuildChannel> exprChannel;

    @Override
    protected Member[] get(Event e) {
        GuildChannel channel = exprChannel.getSingle(e);
        if (channel == null || !channel.getType().equals(ChannelType.VOICE)) return new Member[0];
        return channel.getMembers().toArray(new Member[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends Member> getReturnType() {
        return Member.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "members of voice channel " + exprChannel.toString(e, debug);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprChannel = (Expression<GuildChannel>) exprs[0];
        return true;
    }
}