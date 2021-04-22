package info.itsthesky.DiSky.skript.expressions.member;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import info.itsthesky.DiSky.DiSky;
import info.itsthesky.DiSky.managers.BotManager;
import info.itsthesky.DiSky.skript.expressions.ExprEmoji;
import info.itsthesky.DiSky.skript.scope.webhookmessage.ScopeWebhookMessage;
import info.itsthesky.DiSky.tools.Utils;
import info.itsthesky.DiSky.tools.object.Emote;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

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