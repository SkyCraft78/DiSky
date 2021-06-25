package info.itsthesky.disky.skript.effects;

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
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Guild of Discord entity")
@Description("Get the guild of a discord entity (channel, member, message, etc...)")
@Examples("guild of event-member")
@Since("1.8")
public class ExprGuildOf extends SimpleExpression<Guild> {

    static {
        Skript.registerExpression(ExprGuildOf.class, Guild.class, ExpressionType.SIMPLE,
                "[the] [discord] guild of [discord] [entity] %member/textchannel/message/voicechannel/role/channel/category%"
        );
    }

    Expression<Object> exprEntity;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprEntity = (Expression<Object>) exprs[0];
        return true;
    }

    @Nullable
    @Override
    protected Guild[] get(Event e) {
        Object entity = exprEntity.getSingle(e);
        if (entity == null) return new Guild[0];
        if (entity instanceof Member) return new Guild[] {((Member) entity).getGuild()};
        if (entity instanceof TextChannel) return new Guild[] {((TextChannel) entity).getGuild()};
        if (entity instanceof VoiceChannel) return new Guild[] {((VoiceChannel) entity).getGuild()};
        if (entity instanceof Role) return new Guild[] {((Role) entity).getGuild()};
        if (entity instanceof Category) return new Guild[] {((Category) entity).getGuild()};
        if (entity instanceof Invite) return new Guild[] {(Guild) ((Invite) entity).getGuild()};
        if (entity instanceof UpdatingMessage) return new Guild[] {((UpdatingMessage) entity).getMessage().getGuild()};
        return new Guild[0];
    }

    @Override
    public Class<? extends Guild> getReturnType() {
        return Guild.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "guild of " + exprEntity.toString(e, debug);
    }
}
