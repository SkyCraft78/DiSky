package info.itsthesky.DiSky.skript.audio.manage;

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
import info.itsthesky.DiSky.managers.music.AudioUtils;
import info.itsthesky.DiSky.skript.scope.webhookmessage.ScopeWebhookMessage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

@Name("Repeating State of Guild")
@Description("Get ot set the repeating state of a guild. If it's true, the track will be repeating every time.")
@Examples("set repeating state of event-guild to true")
@Since("1.11")
public class ExprRepeatState extends SimplePropertyExpression<Guild, Boolean> {

    static {
        register(ExprRepeatState.class, Boolean.class,
                "[discord] repeating [state]",
                "guild"
        );
    }

    @Nullable
    @Override
    public Boolean convert(Guild guild) {
        return AudioUtils.getGuildAudioPlayer(guild).getScheduler().isRepeated();
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    protected String getPropertyName() {
        return "repeating state";
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
        Boolean state = Boolean.parseBoolean(delta[0].toString());
        if (mode == Changer.ChangeMode.SET) {
            for (Guild guild : getExpr().getArray(e)) {
                 AudioUtils.getGuildAudioPlayer(guild).getScheduler().setRepeated(state);
            }
        }
    }
}