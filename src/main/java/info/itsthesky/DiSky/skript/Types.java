package info.itsthesky.DiSky.skript;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.DiSky.managers.BotManager;
import info.itsthesky.DiSky.skript.commands.CommandEvent;
import info.itsthesky.DiSky.skript.commands.CommandFactory;
import info.itsthesky.DiSky.skript.commands.CommandObject;
import info.itsthesky.DiSky.tools.Utils;
import info.itsthesky.DiSky.tools.object.*;
import info.itsthesky.DiSky.tools.object.Emote;
import info.itsthesky.DiSky.tools.object.command.Arguments;
import info.itsthesky.DiSky.tools.object.command.Command;
import info.itsthesky.DiSky.tools.object.command.Prefix;
import info.itsthesky.DiSky.tools.object.messages.Channel;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Types {
	static  {
		Classes.registerClass(new ClassInfo<>(Category.class, "category")
				.user("categor(y|ies)")
				.name("Category")
				.description("Represent a category in a guild, which is already created.")
				.since("1.4.2")
				.parser(new Parser<Category>() {
					@SuppressWarnings("unchecked")
					@Override
					public Category parse(final String s, final ParseContext context) {
						if (context.equals(ParseContext.COMMAND)) {
							CommandEvent lastEvent = CommandEvent.lastEvent;
							return (Utils.parseLong(s, false, true) == null ? null : lastEvent.getGuild().getCategoryById(Utils.parseLong(s, false, true)));
						} else {
							Long input = Utils.parseLong(s, false, true);
							for (Map.Entry<String, JDA> entry : BotManager.getBots().entrySet()) {
								String name = entry.getKey();
								JDA jda = entry.getValue();
								for (Guild guild : jda.getGuilds()) {
									return guild.getCategoryById(input);
								}
							}
						}
						return null;
					}

					@Override
					public String toString(Category c, int flags) {
						return c.getName();
					}

					@Override
					public String toVariableNameString(Category cat) {
						return cat.getName();
					}

					@Override
					public String getVariableNamePattern() {
						return "[a-z ]+";
					}
				}));
		Classes.registerClass(new ClassInfo<>(CategoryBuilder.class, "categorybuilder")
				.user("categorybuilders?")
				.name("Category Builder")
				.description("Represent a category builder, which is not created yet in a guild.")
				.since("1.4.1")
				.parser(new Parser<CategoryBuilder>() {

					@Override
					public boolean canParse(ParseContext context) {
						return context.equals(ParseContext.COMMAND);
					}

					@Override
					public String toString(CategoryBuilder c, int flags) {
						return c.getName();
					}

					@Override
					public String toVariableNameString(CategoryBuilder cat) {
						return cat.getName();
					}

					@Override
					public String getVariableNamePattern() {
						return "[a-z ]+";
					}

					@Nullable
					@Override
					public CategoryBuilder parse(String s, ParseContext context) {
						return null;
					}
				}));
		Classes.registerClass(new ClassInfo<>(AudioSite.class, "audiosite")
				.user("audiosites?")
				.name("Audio Site")
				.description("Represent a website were audio could be found and loaded.")
				.usage("youtube, soundcloud")
				.since("1.9")
				.parser(new Parser<AudioSite>() {

					@Override
					public String toString(AudioSite c, int flags) {
						return c.name().toLowerCase(Locale.ROOT);
					}

					@Override
					public String toVariableNameString(AudioSite c) {
						return c.name().toLowerCase(Locale.ROOT);
					}

					@Override
					public String getVariableNamePattern() {
						return "[a-z ]+";
					}

					@Nullable
					@Override
					public AudioSite parse(String s, ParseContext context) {
						for (AudioSite site : AudioSite.values()) if (site.name().toLowerCase(Locale.ROOT).replace("_", " ").equalsIgnoreCase(s)) return site;
						return null;
					}
				}));
		Classes.registerClass(new ClassInfo<>(JDA.class, "bot")
				.user("bots?")
				.name("Discord Bot")
				.description("Represent a loaded Discord Bot")
				.since("1.0")
				.parser(new Parser<JDA>() {

					@Override
					public boolean canParse(ParseContext context) {
						return false;
					}

					@Override
					public String toString(JDA o, int flags) {
						return BotManager.getNameByJDA(o);
					}

					@Override
					public String toVariableNameString(JDA o) {
						return BotManager.getNameByJDA(o);
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}
					@Nullable
					@Override
					public JDA parse(String s, ParseContext context) {
						return null;
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(Emote.class, "emote")
				.user("emotes?")
				.name("Discord Emote")
				.description("Represent a discord emote")
				.since("1.8")
				.parser(new Parser<Emote>() {

					@Override
					public boolean canParse(ParseContext context) {
						return false;
					}

					@Override
					public String toString(Emote o, int flags) {
						return o.getAsMention();
					}

					@Override
					public String toVariableNameString(Emote o) {
						return o.getAsMention();
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}
					@Nullable
					@Override
					public Emote parse(String s, ParseContext context) {
						return null;
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(Badge.class, "badge")
				.user("bots?")
				.name("Discord Bot")
				.description("Represent a loaded Discord Bot")
				.since("1.0")
				.parser(new Parser<Badge>() {

					@Override
					public boolean canParse(ParseContext context) {
						return false;
					}

					@Override
					public String toString(Badge o, int flags) {
						return o.name().toLowerCase(Locale.ROOT).replace("_", "");
					}

					@Override
					public String toVariableNameString(Badge o) {
						return o.name().toLowerCase(Locale.ROOT).replace("_", "");
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}
					@Nullable
					@Override
					public Badge parse(String s, ParseContext context) {
						return null;
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(WebhookMessageBuilder.class, "webhookmessagebuilder")
				.user("webhookmessagebuilders?")
				.name("Webhook Message Builder")
				.description("Represent a webhook message builder, with multiple embed, text content AND a different appearance than the original webhook.")
				.since("1.8")
				.parser(new Parser<WebhookMessageBuilder>() {

					@Override
					public boolean canParse(ParseContext context) {
						return false;
					}

					@Override
					public String toString(WebhookMessageBuilder o, int flags) {
						return o.build().getUsername();
					}

					@Override
					public String toVariableNameString(WebhookMessageBuilder o) {
						return o.build().getUsername();
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}
					@Nullable
					@Override
					public WebhookMessageBuilder parse(String s, ParseContext context) {
						return null;
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(TextChannelBuilder.class, "textchannelbuilder")
				.user("textchannelbuilders?")
				.name("TextChannel Builder")
				.description("Represent a textchannel builder")
				.since("1.4")
				.parser(new Parser<TextChannelBuilder>() {

					@Override
					public boolean canParse(ParseContext context) {
						return false;
					}

					@Override
					public String toString(TextChannelBuilder o, int flags) {
						return o.getName();
					}

					@Override
					public String toVariableNameString(TextChannelBuilder o) {
						return o.getName();
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}
					@Nullable
					@Override
					public TextChannelBuilder parse(String s, ParseContext context) {
						return null;
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(TextChannel.class, "textchannel")
				.user("textchannels?")
				.name("Text Channel")
				.description("Represent a Discord text channel (where file and message can be sent)")
				.since("1.0")
				.parser(new Parser<TextChannel>() {

					@Override
					public boolean canParse(ParseContext context) {
						return context.equals(ParseContext.COMMAND);
					}

					@Override
					public String toString(TextChannel o, int flags) {
						return o.getName();
					}

					@Override
					public String toVariableNameString(TextChannel o) {
						return o.getName();
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}

					@SuppressWarnings("unchecked")
					@Override
					public TextChannel parse(final String s, final ParseContext context) {
						if (context.equals(ParseContext.COMMAND)) {
							CommandEvent lastEvent = CommandEvent.lastEvent;
							return (Utils.parseLong(s, false, true) == null ? null : lastEvent.getGuild().getTextChannelById(Utils.parseLong(s, false, true)));
						} else {
							Long input = Utils.parseLong(s, false, true);
							for (Map.Entry<String, JDA> entry : BotManager.getBots().entrySet()) {
								String name = entry.getKey();
								JDA jda = entry.getValue();
								for (Guild guild : jda.getGuilds()) {
									return guild.getTextChannelById(input);
								}
							}
						}
						return null;
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(User.class, "user")
				.user("users?")
				.name("Discord User")
				.description("Represent a discord user, which is not into a guild.")
				.since("1.0")
				.parser(new Parser<User>() {

					@Override
					public boolean canParse(ParseContext context) {
						return context.equals(ParseContext.COMMAND);
					}
					@Override
					public String toString(User o, int flags) {
						return o.getName();
					}

					@Override
					public String toVariableNameString(User o) {
						return o.getName() + "#" + o.getDiscriminator();
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}

					@SuppressWarnings("unchecked")
					@Override
					public User parse(final String s, final ParseContext context) {
						if (context.equals(ParseContext.COMMAND)) {
							return (Utils.parseLong(s, false, true) == null ? null : BotManager.getFirstBot().getUserById(Utils.parseLong(s, false, true)));
						} else {
							Long input = Utils.parseLong(s, false, true);
							for (Map.Entry<String, JDA> entry : BotManager.getBots().entrySet()) {
								JDA jda = entry.getValue();
								jda.getUserById(input);
							}
						}
						return null;
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(Role.class, "role")
				.user("roles?")
				.name("Discord Role")
				.description("Represent a discord role, within a guild.")
				.since("1.0")
				.parser(new Parser<Role>() {

					@Override
					public boolean canParse(ParseContext context) {
						return context.equals(ParseContext.COMMAND);
					}

					@Override
					public String toString(Role o, int flags) {
						return o.getName();
					}

					@Override
					public String toVariableNameString(Role o) {
						return o.getName();
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}

					@SuppressWarnings("unchecked")
					@Override
					public Role parse(final String s, final ParseContext context) {
						if (context.equals(ParseContext.COMMAND)) {
							return (Utils.parseLong(s, false, true) == null ? null : BotManager.getFirstBot().getRoleById(Utils.parseLong(s, false, true)));
						} else {
							Long input = Utils.parseLong(s, false, true);
							for (Map.Entry<String, JDA> entry : BotManager.getBots().entrySet()) {
								String name = entry.getKey();
								JDA jda = entry.getValue();
								for (Guild guild : jda.getGuilds()) {
									return guild.getRoleById(input);
								}
							}
						}
						return null;
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(Member.class, "member")
				.user("members?")
				.name("Discord Member")
				.description("Represent a discord user which is in any guild.")
				.since("1.0")
				.parser(new Parser<Member>() {

					@Override
					public boolean canParse(ParseContext context) {
						return context.equals(ParseContext.COMMAND);
					}

					@Override
					public String toString(Member o, int flags) {
						return o.getEffectiveName();
					}

					@Override
					public String toVariableNameString(Member o) {
						return o.getUser().getName() + "#" + o.getUser().getDiscriminator();
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}

					@SuppressWarnings("unchecked")
					@Override
					public Member parse(final String s, final ParseContext context) {
						if (context.equals(ParseContext.COMMAND)) {
							CommandEvent lastEvent = CommandEvent.lastEvent;
							return (Utils.parseLong(s, true, true) == null ? null : lastEvent.getGuild().getMemberById(Utils.parseLong(s, true, true)));
						} else {
							Long input = Utils.parseLong(s, false, true);
							for (Map.Entry<String, JDA> entry : BotManager.getBots().entrySet()) {
								String name = entry.getKey();
								JDA jda = entry.getValue();
								for (Guild guild : jda.getGuilds()) {
									return guild.getMemberById(input);
								}
							}
						}
						return null;
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(Message.class, "message")
				.user("messages?")
				.name("Discord Message")
				.description("Represent a discord message, with ID, author, reactions, etc...")
				.since("1.0")
				.parser(new Parser<Message>() {

					@Override
					public boolean canParse(ParseContext context) {
						return false;
					}

					@Override
					public String toString(Message o, int flags) {
						return o.getContentRaw();
					}

					@Override
					public String toVariableNameString(Message o) {
						return o.getContentRaw();
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}
					@Nullable
					@Override
					public Message parse(String s, ParseContext context) {
						Long input = Utils.parseLong(s, false, true);
						for (Map.Entry<String, JDA> entry : BotManager.getBots().entrySet()) {
							String name = entry.getKey();
							JDA jda = entry.getValue();
							for (Guild guild : jda.getGuilds()) {
								for (TextChannel channel : guild.getTextChannels()) {
									return channel.retrieveMessageById(input).complete();
								}
							}
						}
						return null;
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(GuildChannel.class, "channel")
				.user("channels?")
				.name("Guild Channel")
				.description("Represent a Guild discord channel. Can be both text OR voice. Action specific of a voice channel (like user limit), used on text channel will throw an error.")
				.since("1.8")
				.parser(new Parser<GuildChannel>() {

					@Override
					public boolean canParse(ParseContext context) {
						return context.equals(ParseContext.COMMAND);
					}

					@Override
					public String toString(GuildChannel o, int flags) {
						return o.getName();
					}

					@Override
					public String toVariableNameString(GuildChannel o) {
						return o.getName();
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}

					@SuppressWarnings("unchecked")
					@Override
					public GuildChannel parse(final String s, final ParseContext context) {
						if (context.equals(ParseContext.COMMAND)) {
							CommandEvent lastEvent = CommandEvent.lastEvent;
							return (Utils.parseLong(s, false, true) == null ? null : lastEvent.getGuild().getGuildChannelById(Utils.parseLong(s, false, true)));
						} else {
							Long input = Utils.parseLong(s, false, true);
							for (Map.Entry<String, JDA> entry : BotManager.getBots().entrySet()) {
								String name = entry.getKey();
								JDA jda = entry.getValue();
								for (Guild guild : jda.getGuilds()) {
									return guild.getGuildChannelById(input);
								}
							}
						}
						return null;
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(Webhook.class, "webhookbuilder")
				.user("webhookbuilders?")
				.name("Discord Webhook Builder")
				.description("Represent a webhook which is not actually made in the guild.")
				.since("1.2")
				.parser(new Parser<Webhook>() {

					@Override
					public boolean canParse(ParseContext context) {
						return false;
					}

					@Override
					public String toString(Webhook o, int flags) {
						return o.getName();
					}

					@Override
					public String toVariableNameString(Webhook o) {
						return o.getName();
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}
					@Nullable
					@Override
					public Webhook parse(String s, ParseContext context) {
						return null;
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(info.itsthesky.DiSky.tools.object.messages.Message.class, "staticmessage")
				.user("staticmessages?")
				.name("Static Discord Message")
				.description("Represent a static discord message, which mean a message / embed which is not sent yet.")
				.since("1.0")
				.parser(new Parser<info.itsthesky.DiSky.tools.object.messages.Message>() {

					@Override
					public String toString(info.itsthesky.DiSky.tools.object.messages.Message o, int flags) {
						return o.toString();
					}

					@Override
					public boolean canParse(ParseContext context) {
						return false;
					}

					@Override
					public String toVariableNameString(info.itsthesky.DiSky.tools.object.messages.Message o) {
						return o.getContent();
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}
					@Nullable
					@Override
					public info.itsthesky.DiSky.tools.object.messages.Message parse(String s, ParseContext context) {
						return null;
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(Guild.class, "guild")
				.user("guilds?")
				.name("Discord Guild (Server)")
				.description("Represent a discord guild")
				.since("1.0")
				.parser(new Parser<Guild>() {

					@Override
					public String toString(Guild o, int flags) {
						return o.getName();
					}

					@Override
					public String toVariableNameString(Guild o) {
						return o.getName();
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}

					@Override
					public boolean canParse(ParseContext context) {
						return context.equals(ParseContext.COMMAND);
					}

					@SuppressWarnings("unchecked")
					@Override
					public Guild parse(final String s, final ParseContext context) {
						if (context.equals(ParseContext.COMMAND)) {
							CommandEvent lastEvent = CommandEvent.lastEvent;
							return (Utils.parseLong(s, false, true) == null ? null : lastEvent.getBot().getGuildById(Utils.parseLong(s, false, true)));
						} else {
							Long input = Utils.parseLong(s, false, true);
							for (Map.Entry<String, JDA> entry : BotManager.getBots().entrySet()) {
								String name = entry.getKey();
								JDA jda = entry.getValue();
								return jda.getGuildById(input);
							}
						}
						return null;
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(EmbedBuilder.class, "embed")
				.user("embed")
				.name("Embed")
				.description(new String[] {
						"Represent a discord embed, with title, description, fields, author, footer, etc..."
				})
				.since("1.0")
				.parser(new Parser<EmbedBuilder>() {

					@Override
					public String toString(EmbedBuilder o, int flags) {
						return o.getDescriptionBuilder().toString();
					}

					@Override
					public String toVariableNameString(EmbedBuilder o) {
						return ToStringBuilder.reflectionToString(o);
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}
					@Nullable
					@Override
					public EmbedBuilder parse(String s, ParseContext context) {
						return null;
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(CommandObject.class, "discordcommand")
				.user("discordcommands?")
				.name("Discord Command")
				.description(new String[] {
						"Represent a discord command, with arguments, usage, description, category, etc..."
				})
				.since("1.7")
				.parser(new Parser<CommandObject>() {

					@Override
					public boolean canParse(ParseContext context) {
						return false;
					}

					@Override
					public String toString(CommandObject o, int flags) {
						return o.getName();
					}

					@Override
					public String toVariableNameString(CommandObject o) {
						return o.getName();
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}
					@Nullable
					@Override
					public CommandObject parse(String s, ParseContext context) {
						return null;
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(Invite.class, "invite")
				.user("invites?")
				.name("Discord Invite")
				.description(new String[] {
						"Represent a discord invite, with creator, used time, code, etc..."
				})
				.since("1.7")
				.parser(new Parser<Invite>() {

					@Override
					public boolean canParse(ParseContext context) {
						return false;
					}

					@Override
					public String toString(Invite o, int flags) {
						return o.getCode();
					}

					@Override
					public String toVariableNameString(Invite o) {
						return o.getCode();
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}

					@Nullable
					@Override
					public Invite parse(String s, ParseContext context) {
						if (context != ParseContext.COMMAND) return null;
						CommandEvent event = CommandEvent.lastEvent;
						String input = s
								.replace("http://discord.gg/", "");
						AtomicReference<Invite> inviteAtomicReference = new AtomicReference<>();
						event.getGuild().retrieveInvites().complete().forEach((invite) -> {
							if (invite.getCode().equalsIgnoreCase(s)) inviteAtomicReference.set(invite);
						});
						return inviteAtomicReference.get();
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(MessageBuilder.class, "messagebuilder")
				.user("messagebuilders?")
				.name("Message Builder")
				.description(new String[] {
						"Represent a discord message builder, which you can append embed and string."
				})
				.since("1.4")
				.parser(new Parser<MessageBuilder>() {

					@Override
					public String toString(MessageBuilder o, int flags) {
						return o.toString();
					}

					@Override
					public String toVariableNameString(MessageBuilder o) {
						return o.toString();
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}
					@Nullable
					@Override
					public MessageBuilder parse(String s, ParseContext context) {
						return null;
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(InviteBuilder.class, "invitebuilder")
				.user("invitebuilders?")
				.name("Invite Builder")
				.description(new String[] {
						"Represent a discord invite builder, with max use, max age, etc..."
				})
				.since("1.7")
				.parser(new Parser<InviteBuilder>() {

					@Override
					public String toString(InviteBuilder o, int flags) {
						return o.toString();
					}

					@Override
					public String toVariableNameString(InviteBuilder o) {
						return o.toString();
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}

					@Nullable
					@Override
					public InviteBuilder parse(String s, ParseContext context) {
						return null;
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(Message.Attachment.class, "attachment")
				.user("attachments?")
				.name("Message Attachment")
				.description(new String[] {
						"Represent a discord message attachment."
				})
				.since("1.7")
				.parser(new Parser<Message.Attachment>() {

					@Override
					public String toString(Message.Attachment o, int flags) {
						return o.getFileName();
					}

					@Override
					public String toVariableNameString(Message.Attachment o) {
						return o.getFileName();
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}

					@Nullable
					@Override
					public Message.Attachment parse(String s, ParseContext context) {
						return null;
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(RoleBuilder.class, "rolebuilder")
				.user("rolebuilders?")
				.name("Role Builder")
				.description(new String[] {
						"Represent a discord role builder, which is not created yet in a guild."
				})
				.since("1.4")
				.parser(new Parser<RoleBuilder>() {

					@Override
					public String toString(RoleBuilder o, int flags) {
						return o.getName();
					}

					@Override
					public String toVariableNameString(RoleBuilder o) {
						return o.getName();
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}
					@Nullable
					@Override
					public RoleBuilder parse(String s, ParseContext context) {
						return null;
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(VoiceChannelBuilder.class, "voicechannelbuilder")
				.user("voicechannelbuilders?")
				.name("Voice Channel Builder")
				.description(new String[] {
						"Represent a discord voice channel builder, which is not created yet in a guild."
				})
				.since("1.6")
				.parser(new Parser<VoiceChannelBuilder>() {

					@Override
					public String toString(VoiceChannelBuilder o, int flags) {
						return o.getName();
					}

					@Override
					public String toVariableNameString(VoiceChannelBuilder o) {
						return o.getName();
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}

					@Override
					public boolean canParse(ParseContext context) {
						return false;
					}

					@Nullable
					@Override
					public VoiceChannelBuilder parse(String s, ParseContext context) {
						return null;
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(Permission.class, "permission")
				.user("permissions?")
				.name("Discord Permission")
				.description("Permission used for a role, channel, member, etc...")
				.usage("create instant invite, kick members, ban members, administrator, manage channel, manage server, message add reaction, view audit logs, view channel, message read, message write, message tts, message manage, message embed links, message attach files, message history, message mention everyone, message ext emoji, voice connect, voice speak, voice mute others, voice deaf others, voice move others, voice use vad, nickname change, nickname manage, manage roles, manage permissions, manage webhooks, manage emotes, unknown")
				.since("1.4")
				.parser(new Parser<Permission>() {
					@Override
					public Permission parse(String input, ParseContext context) {
						for (Permission perm : Permission.values()) {
							if (perm.name().equalsIgnoreCase(input.toUpperCase(Locale.ROOT).replace(" ", "_"))) return perm;
						}
						return null;
					}

					@Override
					public String toString(Permission c, int flags) {
						return c.getName();
					}

					@Override
					public String toVariableNameString(Permission perm) {
						return perm.getName().toLowerCase(Locale.ENGLISH).replace('_', ' ');
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(PlayError.class, "playerror")
				.user("playerrors?")
				.name("Audio Play Error")
				.description("Represent an audio play error.")
				.usage("not exist - The track you're trying to play doesn't exist, or the input for youtube search doesn't found anything.",
						"")
				.since("1.6")
				.parser(new Parser<PlayError>() {
					@Override
					public PlayError parse(String input, ParseContext context) {
						for (PlayError perm : PlayError.values()) {
							if (perm.name().equalsIgnoreCase(input.replaceAll(" ", "_").toUpperCase(Locale.ROOT))) return perm;
						}
						return null;
					}

					@Override
					public String toString(PlayError c, int flags) {
						return c.name();
					}

					@Override
					public String toVariableNameString(PlayError perm) {
						return perm.name().toLowerCase(Locale.ENGLISH).replace('_', ' ');
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(VoiceChannel.class, "voicechannel")
				.user("voicechannels?")
				.name("Voice Channel")
				.description("Represent a discord voice channel.")
				.since("1.6")
				.parser(new Parser<VoiceChannel>() {

					@SuppressWarnings("unchecked")
					@Override
					public VoiceChannel parse(final String s, final ParseContext context) {
						if (context.equals(ParseContext.COMMAND)) {
							CommandEvent lastEvent = CommandEvent.lastEvent;
							return (Utils.parseLong(s, false, true) == null ? null : lastEvent.getBot().getVoiceChannelById(Utils.parseLong(s, false, true)));
						}
						return null;
					}

					@Override
					public String toString(VoiceChannel c, int flags) {
						return c.getName();
					}

					@Override
					public String toVariableNameString(VoiceChannel perm) {
						return perm.getName();
					}

					@Override
					public boolean canParse(ParseContext context) {
						return true;
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(OnlineStatus.class, "onlinestatus")
				.user("onlinestatus")
				.name("Online Status")
				.description("Represent a user / bot online status.")
				.usage("online, offline, idle, do not disturb")
				.since("1.6")
				.parser(new Parser<OnlineStatus>() {
					@Override
					public OnlineStatus parse(String input, ParseContext context) {
						for (OnlineStatus status : OnlineStatus.values()) {
							if (status.name().equalsIgnoreCase(input.replaceAll(" ", "_").toUpperCase())) return status;
						}
						return null;
					}

					@Override
					public String toString(OnlineStatus status, int flags) {
						return status.name().toLowerCase(Locale.ENGLISH).replace('_', ' ');
					}

					@Override
					public String toVariableNameString(OnlineStatus status) {
						return status.name().toLowerCase(Locale.ENGLISH).replace('_', ' ');
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(SlashCommand.class, "commandbuilder")
				.user("commandbuilders?")
				.name("Command Builder")
				.description(new String[]{
						"Represent a non-registered discord slash command."
				})
				.since("1.5")
				.parser(new Parser<SlashCommand>() {

					@Override
					public String toString(SlashCommand o, int flags) {
						return o.getName();
					}

					@Override
					public String toVariableNameString(SlashCommand o) {
						return o.getName();
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}
					@Nullable
					@Override
					public SlashCommand parse(String s, ParseContext context) {
						return null;
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(AudioTrack.class, "track")
				.user("tracks?")
				.name("Audio Track")
				.description(new String[]{
						"Represent an audio track, with duration, name, etc..."
				})
				.since("1.6")
				.parser(new Parser<AudioTrack>() {

					@Override
					public String toString(AudioTrack o, int flags) {
						return o.getInfo().title;
					}

					@Override
					public String toVariableNameString(AudioTrack o) {
						return o.getInfo().title;
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}

					@Nullable
					@Override
					public AudioTrack parse(String s, ParseContext context) {
						return null;
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(OptionType.class, "optiontype")
				.user("optiontypes?")
				.name("Option Type")
				.description(new String[] {
						"Represent a slash command option type."
				})
				.usage("STRING (a text, string, almost anythings)\n" +
						"INTEGER (rounded number, so no decimal here)\n" +
						"USER (a guild user)\n" +
						"ROLE (a guild role)\n" +
						"BOOLEAN (only two state, true or false)\n" +
						"CHANNEL (a guild text channel)")
				.since("1.5")
				.parser(new Parser<OptionType>() {

					@Override
					public String toString(OptionType o, int flags) {
						return o.name();
					}

					@Override
					public String toVariableNameString(OptionType o) {
						return o.name();
					}

					@Override
					public String getVariableNamePattern() {
						return ".+";
					}

					@Override
					public OptionType parse(String s, final ParseContext context) {
						for (OptionType op : OptionType.values()) {
							if (op.name().equalsIgnoreCase(s.toUpperCase())) {
								return op;
							}
						}
						return null;
					}
				})
		);
	}
}