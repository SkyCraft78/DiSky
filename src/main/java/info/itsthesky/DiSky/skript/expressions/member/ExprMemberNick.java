package info.itsthesky.DiSky.skript.expressions.member;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import info.itsthesky.DiSky.DiSky;
import info.itsthesky.DiSky.managers.BotManager;
import info.itsthesky.DiSky.skript.scope.webhookmessage.ScopeWebhookMessage;
import info.itsthesky.DiSky.tools.DiSkyErrorHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

@Name("Nickname of Member")
@Description("Get, set or reset the nickname of a specific member.")
@Examples("set discord nickname of event-member to \"Hello world !\"")
@Since("1.11")
public class ExprMemberNick extends SimplePropertyExpression<Member, String> {

    static {
        register(ExprMemberNick.class, String.class,
                "[discord] nick[name]",
                "member"
        );
    }

    @Nullable
    @Override
    public String convert(Member entity) {
        return entity.getNickname();
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "discord nickname";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET || mode.equals(Changer.ChangeMode.RESET)) {
            return CollectionUtils.array(String.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            if (delta == null || delta.length == 0) return;
            String nick = delta[0].toString();
            for (Member member : getExpr().getArray(e)) {
                member.modifyNickname(nick).queue(null, DiSkyErrorHandler::logException);
            }
        } else if (mode.equals(Changer.ChangeMode.RESET)) {
            for (Member member : getExpr().getArray(e)) {
                member.modifyNickname(null).queue(null, DiSkyErrorHandler::logException);
            }
        }
    }
}