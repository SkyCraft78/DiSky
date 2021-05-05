package info.itsthesky.DiSky.skript.expressions;

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
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

@Name("Avatar of user / member / webhook / bot")
@Description("Get user, webhook, bot, member avatar url, and set only webhook ones.")
@Examples("set avatar of last webhook to \"https://image.jeuxvideo.com/medias-md/158685/1586849646-1530-card.jpg\"")
@Since("1.2")
public class ExprAvatarOf extends SimplePropertyExpression<Object, String> {

    static {
        register(ExprAvatarOf.class, String.class,
                "[discord] avatar",
                "user/member/guild/webhookmessagebuilder/bot"
        );
    }

    @Nullable
    @Override
    public String convert(Object entity) {
        if (entity instanceof Member) {
            return ((Member) entity).getUser().getAvatarUrl();
        } else if (entity instanceof User) {
            return ((User) entity).getAvatarUrl();
        } else if (entity instanceof Webhook) {
            return ((Webhook) entity).getDefaultUser().getAvatarUrl();
        }  else if (entity instanceof JDA) {
            return ((JDA) entity).getSelfUser().getAvatarUrl();
        } else if (entity instanceof Guild) {
            return ((Guild) entity).getIconUrl();
        }
        return null;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "avatar";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(String.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (delta == null || delta.length == 0) return;
        if (mode == Changer.ChangeMode.SET) {
            for (Object entity : getExpr().getArray(e)) {
                 if (entity instanceof WebhookMessageBuilder) {
                    WebhookMessageBuilder webhook = (WebhookMessageBuilder) entity;
                    webhook.setAvatarUrl(delta[0].toString());
                    ScopeWebhookMessage.lastBuilder = webhook;
                    return;
                } else if (entity instanceof Guild) {
                    URLConnection co = null;
                    Guild guild = (Guild) entity;
                    try {
                        co = new URL(delta[0].toString()).openConnection();
                        co.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36 OPR/55.0.2994.61");
                        guild.getManager().setIcon(Icon.from(co.getInputStream())).queue();
                    } catch (IOException ioException) {
                        DiSky
                                .getInstance()
                                .getLogger()
                                .severe("Oh no :( DiSky cannot found the right avatar image for url '"+delta[0].toString()+"' ! Error: " + ioException.getMessage());
                    }
                }
            }
        }
    }
}