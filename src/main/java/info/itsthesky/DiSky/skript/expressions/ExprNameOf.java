package info.itsthesky.disky.skript.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.managers.BotManager;
import info.itsthesky.disky.skript.commands.CommandObject;
import info.itsthesky.disky.skript.scope.category.ScopeCategory;
import info.itsthesky.disky.skript.scope.commands.ScopeCommand;
import info.itsthesky.disky.skript.scope.role.ScopeRole;
import info.itsthesky.disky.skript.scope.textchannels.ScopeTextChannel;
import info.itsthesky.disky.skript.scope.voicechannels.ScopeVoiceChannel;
import info.itsthesky.disky.skript.scope.webhookmessage.ScopeWebhookMessage;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.object.*;
import info.itsthesky.disky.tools.object.Emote;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Name of Discord Entity")
@Description("Get or set the discord name of any channel, member, role, etc...")
@Examples("set discord name of event-channel to \"Iron Monger\"")
@Since("1.0")
public class ExprNameOf extends SimplePropertyExpression<Object, String> {

    static {
        register(ExprNameOf.class, String.class,
                "discord name",
                "member/role/rolebuilder/commandbuilder/voicechannel/voicechannelbuilder/webhookmessagebuilder/discordcommand/category/categorybuilder/channel/textchannel/textchannelbuilder/bot/guild/user/emote"
        );
    }

    @Nullable
    @Override
    public String convert(Object entity) {
        String finalName = null;
        if (entity instanceof JDA) {
            final JDA bot = BotManager.getBot(entity.toString());
            if (bot == null) return null;
            entity = bot.getSelfUser();
            return ((SelfUser) entity).getName();
        }
        try {
            finalName = (String) entity.getClass().getDeclaredMethod("getName").invoke(entity);
        } catch (final Exception ignored) { }
        if (finalName == null) {
            if (entity instanceof Member) finalName = ((Member) entity).getUser().getName();
            if (entity instanceof TextChannel) finalName = ((TextChannel) entity).getName();
            if (entity instanceof Webhook) finalName = ((Webhook) entity).getName();
            if (entity instanceof TextChannelBuilder) finalName = ((TextChannelBuilder) entity).getName();
            if (entity instanceof RoleBuilder) finalName = ((RoleBuilder) entity).getName();
            if (entity instanceof Category) finalName = ((Category) entity).getName();
            if (entity instanceof CategoryBuilder) finalName = ((CategoryBuilder) entity).getName();
            if (entity instanceof SlashCommand) finalName = ((SlashCommand) entity).getName();
            if (entity instanceof User) finalName = ((User) entity).getName();
            if (entity instanceof VoiceChannel) finalName = ((VoiceChannel) entity).getName();
            if (entity instanceof GuildChannel) finalName = ((GuildChannel) entity).getName();
            if (entity instanceof Guild) finalName = ((Guild) entity).getName();
            if (entity instanceof VoiceChannelBuilder) finalName = ((VoiceChannelBuilder) entity).getName();
            if (entity instanceof JDA) finalName = ((JDA) entity).getSelfUser().getName();
            if (entity instanceof WebhookMessageBuilder) finalName = ((WebhookMessageBuilder) entity).build().getContent();
            if (entity instanceof Emote) finalName = ((Emote) entity).getName();
            if (entity instanceof CommandObject) finalName = ((CommandObject) entity).getName();
        }
        return finalName;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "discord name";
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
                if (entity instanceof TextChannel) {
                    TextChannel channel = (TextChannel) entity;
                    channel.getManager().setName(delta[0].toString()).queue(null, DiSkyErrorHandler::logException);
                    return;
                } else if (entity instanceof GuildChannel) {
                    GuildChannel channel = (GuildChannel) entity;
                    channel.getManager().setName(delta[0].toString()).queue(null, DiSkyErrorHandler::logException);
                    return;
                } else if (entity instanceof Member) {
                    Member member = (Member) entity;
                    member.modifyNickname(delta[0].toString()).queue(null, DiSkyErrorHandler::logException);
                    return;
                } else if (entity instanceof WebhookMessageBuilder) {
                    WebhookMessageBuilder webhook = (WebhookMessageBuilder) entity;
                    webhook.setUsername(delta[0].toString());
                    ScopeWebhookMessage.lastBuilder = webhook;
                    return;
                } else if (entity instanceof SlashCommand) {
                    SlashCommand cmd = (SlashCommand) entity;
                    cmd.setName(delta[0].toString());
                    ScopeCommand.lastBuilder.setName(delta[0].toString());
                    return;
                } else if (entity instanceof Role) {
                    Role role = (Role) entity;
                    role.getManager().setName(delta[0].toString()).queue(null, DiSkyErrorHandler::logException);
                    return;
                } else if (entity instanceof Guild) {
                    Guild guild = (Guild) entity;
                    guild.getManager().setName(delta[0].toString()).queue(null, DiSkyErrorHandler::logException);
                    return;
                } else if (entity instanceof RoleBuilder) {
                    RoleBuilder role = (RoleBuilder) entity;
                    role.setName(delta[0].toString());
                    ScopeRole.lastBuilder.setName(delta[0].toString());
                    return;
                } else if (entity instanceof MessageReaction.ReactionEmote) {
                    MessageReaction.ReactionEmote emote = (MessageReaction.ReactionEmote) entity;
                    if (!emote.isEmote()) return;
                    emote.getEmote().getManager().setName(delta[0].toString()).queue(null, DiSkyErrorHandler::logException);
                    return;
                } else if (entity instanceof TextChannelBuilder) {
                    TextChannelBuilder channel = (TextChannelBuilder) entity;
                    channel.setName(delta[0].toString());
                    ScopeTextChannel
                            .lastBuilder
                            .setName(delta[0].toString());
                    return;
                } else if (entity instanceof VoiceChannelBuilder) {
                    VoiceChannelBuilder channel = (VoiceChannelBuilder) entity;
                    channel.setName(delta[0].toString());
                    ScopeVoiceChannel
                            .lastBuilder
                            .setName(delta[0].toString());
                    return;
                } else if (entity instanceof CategoryBuilder) {
                    CategoryBuilder category = (CategoryBuilder) entity;
                    category.setName(delta[0].toString());
                    ScopeCategory
                            .lastBuilder
                            .setName(delta[0].toString());
                    return;
                } else if (entity instanceof Category) {
                    Category category = (Category) entity;
                    category.getManager().setName(delta[0].toString()).queue(null, DiSkyErrorHandler::logException);
                    return;
                } else if (entity instanceof VoiceChannel) {
                    VoiceChannel voice = (VoiceChannel) entity;
                    voice.getManager().setName(delta[0].toString()).queue(null, DiSkyErrorHandler::logException);
                    return;
                }
                DiSky.getInstance().getLogger().severe("Cannot change the discord name of entity '"+entity.getClass().getName()+"', since that's not a Discord entity!");
            }
        }
    }
}