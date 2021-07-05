package info.itsthesky.disky.skript;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Comparator;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.registrations.Comparators;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky.managers.BotManager;
import info.itsthesky.disky.skript.commands.CommandEvent;
import info.itsthesky.disky.skript.commands.CommandObject;
import info.itsthesky.disky.tools.DiSkyComparator;
import info.itsthesky.disky.tools.DiSkyType;
import net.dv8tion.jda.api.MessageBuilder;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.Emote;
import info.itsthesky.disky.tools.object.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.internal.entities.TextChannelImpl;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Types {

	public static class DiSkyComparators {

		static {
			/*
			 * Basics ISnowflakes entities which can be handled by the disky comparator
			 */
			Comparators.registerComparator(Member.class, Member.class, new DiSkyComparator<>());
			Comparators.registerComparator(User.class, User.class, new DiSkyComparator<>());
			Comparators.registerComparator(Emote.class, Emote.class, new DiSkyComparator<>());
			Comparators.registerComparator(GuildChannel.class, GuildChannel.class, new DiSkyComparator<>());
			Comparators.registerComparator(TextChannel.class, TextChannel.class, new DiSkyComparator<>());
			Comparators.registerComparator(VoiceChannel.class, VoiceChannel.class, new DiSkyComparator<>());
			Comparators.registerComparator(Guild.class, Guild.class, new DiSkyComparator<>());
			Comparators.registerComparator(UpdatingMessage.class, UpdatingMessage.class, new DiSkyComparator<>());
			Comparators.registerComparator(Category.class, Category.class, new DiSkyComparator<>());
			Comparators.registerComparator(Role.class, Role.class, new DiSkyComparator<>());
			Comparators.registerComparator(Webhook.class, Webhook.class, new DiSkyComparator<>());
			Comparators.registerComparator(ButtonBuilder.class, ButtonBuilder.class, new DiSkyComparator<>());

			/*
			 * Custom entities which need a precise comparator
			 */
			Comparators.registerComparator(JDA.class, JDA.class, new Comparator<JDA, JDA>() {
				@Override
				public Relation compare(JDA jda, JDA jda2) {
					if (jda.getSelfUser().getId().equals(jda2.getSelfUser().getId()))
						return Relation.EQUAL;
					return Relation.NOT_EQUAL;
				}

				@Override
				public boolean supportsOrdering() {
					return false;
				}
			});
		}

	}

	static {
		new DiSkyType<>(
				Category.class,
				"category",
				"categor(y|ies)",
				input -> BotManager.search(bot -> bot.getCategoryById(input))
		).register();
		new DiSkyType<>(
				User.class,
				"user",
				"users?",
				input -> BotManager.search(bot -> bot.getUserById(input))
		).register();
		new DiSkyType<>(
				Member.class,
				"member",
				"members?",
				Member::getEffectiveName,
				input -> BotManager.search(bot -> Utils.searchMember(bot, input)),
				false
		).register();
		new DiSkyType<>(
				Role.class,
				"role",
				"roles?",
				input -> BotManager.search(bot -> bot.getRoleById(input))
		).register();
		new DiSkyType<>(
				VoiceChannel.class,
				"voicechannel",
				"voicechannels?",
				input -> BotManager.search(bot -> bot.getVoiceChannelById(input))
		).register();
		new DiSkyType<>(
				TextChannel.class,
				"textchannel",
				"textchannels?",
				input -> BotManager.search(bot -> bot.getTextChannelById(input))
		).register();
		new DiSkyType<>(
				GuildChannel.class,
				"channel",
				"channels?",
				input -> BotManager.search(bot -> bot.getGuildChannelById(input))
		).register();
		new DiSkyType<>(
				Guild.class,
				"guild",
				"guilds?",
				input -> BotManager.search(bot -> bot.getGuildById(input))
		).register();
		new DiSkyType<>(
				CategoryBuilder.class,
				"categorybuilder",
				"categorybuilders?"
		).register();
		new DiSkyType<>(
				ButtonBuilder.class,
				"button",
				"buttons?"
		).register();
		new DiSkyType<>(
				SelectionMenu.Builder.class,
				"selectbuilder",
				"selectbuilders?"
		).register();
		new DiSkyType<>(
				SelectOption.class,
				"selectchoice",
				"selectchoices?"
		).register();
		new DiSkyType<>(
				ButtonRow.class,
				"buttonrow",
				"buttonrows?"
		).register();
		DiSkyType.fromEnum(
				ButtonStyle.class,
				"buttonstyle",
				"buttonstyles?"
		).register();
		new DiSkyType<>(
				Activity.class,
				"presence",
				"presences?"
		).register();
		DiSkyType.fromEnum(
				AudioSite.class,
				"audiosite",
				"audiosites?"
		).register();
		new DiSkyType<>(
				JDA.class,
				"bot",
				"bots?",
				BotManager::getNameByJDA,
				null,
				false
		).register();
		new DiSkyType<>(
				Emote.class,
				"emote",
				"emotes?"
		).register();
		DiSkyType.fromEnum(
				Badge.class,
				"badge",
				"badges?"
		).register();
		new DiSkyType<>(
				WebhookMessageBuilder.class,
				"webhookmessagebuilder",
				"webhookmessagebuilders?",
				null
		).register();
		new DiSkyType<>(
				TextChannelBuilder.class,
				"textchannelbuilder",
				"textchannelbuilders?",
				null
		).register();
		new DiSkyType<>(
				UpdatingMessage.class,
				"message",
				"messages?",
				msg -> msg.getMessage().getContentRaw(),
				null,
				false
		).register();
		new DiSkyType<>(
				Webhook.class,
				"webhookbuilder",
				"webhookbuilders?",
				null
		).register();
		new DiSkyType<>(
				EmbedBuilder.class,
				"embed",
				"embeds?",
				null
		).register();
		new DiSkyType<>(
				CommandObject.class,
				"discordcommand",
				"discordcommands?",
				null
		).register();
		new DiSkyType<>(
				Invite.class,
				"invite",
				"invites?",
				null
		).register();
		new DiSkyType<>(
				MessageBuilder.class,
				"messagebuilder",
				"messagebuilders?",
				msg -> msg.getStringBuilder().toString(),
				null,
				false
		).register();
		new DiSkyType<>(
				InviteBuilder.class,
				"invitebuilder",
				"invitebuilders?",
				null
		).register();
		new DiSkyType<>(
				Message.Attachment.class,
				"attachment",
				"attachments?",
				Message.Attachment::getFileName,
				null,
				false
		).register();
		new DiSkyType<>(
				RoleBuilder.class,
				"rolebuilder",
				"rolebuilders?",
				null
		).register();
		new DiSkyType<>(
				VoiceChannelBuilder.class,
				"voicechannelbuilder",
				"voicechannelbuilders?",
				null
		).register();
		DiSkyType.fromEnum(
				Permission.class,
				"permission",
				"permissions?"
		).register();
		DiSkyType.fromEnum(
				GatewayIntent.class,
				"intent",
				"intents?"
		).register();
		DiSkyType.fromEnum(
				OnlineStatus.class,
				"onlinestatus",
				"onlinestatus"
		).register();
		new DiSkyType<>(
				SlashCommand.class,
				"commandbuilder",
				"commandbuilders?",
				null
		).register();
		new DiSkyType<>(
				AudioTrack.class,
				"track",
				"tracks?",
				track -> track.getInfo().title,
				null,
				false
		).register();
		DiSkyType.fromEnum(
				OptionType.class,
				"optiontype",
				"optiontypes?"
		).register();
	}
}