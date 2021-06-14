package info.itsthesky.disky.skript;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import info.itsthesky.disky.managers.BotManager;
import info.itsthesky.disky.skript.commands.CommandEvent;
import info.itsthesky.disky.skript.commands.CommandObject;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.Emote;
import info.itsthesky.disky.tools.object.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.StreamCorruptedException;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class Types {
	static  {
		Classes.registerClass(new ClassInfo<>(Category.class, "category")
				.user("categor(y|ies)")
				.name("Category")
				.description("Represent a category in a guild, which is already created.")
				.since("1.4.2")
				.parser(new Parser<Category>() {
					@Override
					public @NotNull Category parse(final @NotNull String s, final @NotNull ParseContext context) {
						if (context.equals(ParseContext.COMMAND)) {
							CommandEvent lastEvent = CommandEvent.lastEvent;
							return (Objects.requireNonNull(Utils.parseLong(s, false, true) == null ? null : lastEvent.getGuild().getCategoryById(Utils.parseLong(s, false, true))));
						}
						return null;
					}

					@Override
					public @NotNull String toString(@NotNull Category c, int flags) {
						return c.getName();
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull Category cat) {
						return cat.getName();
					}

					@Override
					public @NotNull String getVariableNamePattern() {
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
					public boolean canParse(@NotNull ParseContext context) {
						return context.equals(ParseContext.COMMAND);
					}

					@Override
					public @NotNull String toString(@NotNull CategoryBuilder c, int flags) {
						return c.getName();
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull CategoryBuilder cat) {
						return cat.getName();
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return "[a-z ]+";
					}

					@Override
					public @NotNull CategoryBuilder parse(@NotNull String s, @NotNull ParseContext context) {
						return null;
					}
				}));
		Classes.registerClass(new ClassInfo<>(ButtonBuilder.class, "button")
				.user("buttons?")
				.name("Message Button")
				.description("Represent a message button.")
				.since("1.12")
		);
		Classes.registerClass(new ClassInfo<>(BotBuilder.class, "botbuilder")
				.user("botbuilders?")
				.name("Bot Builder")
				.description("Represent a bot builder, allow intents, with a name etc...")
				.since("1.14")
		);
		Classes.registerClass(new ClassInfo<>(ButtonRow.class, "buttonrow")
				.user("buttonrows?")
				.name("Message Buttons Row")
				.description("Represent a message buttons row, with a maximum of 5 buttons per row.")
				.since("1.13")
		);
		Classes.registerClass(new ClassInfo<>(ButtonStyle.class, "buttonstyle")
				.user("buttonstyles?")
				.name("Button Style")
				.description("Use in button builder, a specific style of a button.")
				.usage("primary, success, secondary and danger")
				.since("1.12")
				.parser(new Parser<ButtonStyle>() {

					@Override
					public @NotNull String toString(@NotNull ButtonStyle c, int flags) {
						return c.name().toLowerCase(Locale.ROOT).replaceAll("_", " ");
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull ButtonStyle c) {
						return c.name().toLowerCase(Locale.ROOT).replaceAll("_", " ");
					}

					@Override
					public boolean canParse(@NotNull ParseContext context) {
						return true;
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return "[a-z ]+";
					}

					@Nullable
					@Override
					public ButtonStyle parse(@NotNull String s, @NotNull ParseContext context) {
						try {
							return ButtonStyle.valueOf(s.toUpperCase(Locale.ROOT).replaceAll(" ", "_"));
						} catch (Exception ignored) { }
						return null;
					}
				}));
		Classes.registerClass(new ClassInfo<>(Activity.class, "presence")
				.user("presences?")
				.name("Online Presence")
				.description("Represent a custom presence used in 'mark %bot% as %presence%' effect")
				.since("1.12")
				.parser(new Parser<Activity>() {

					@Override
					public @NotNull String toString(@NotNull Activity c, int flags) {
						return c.getName();
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull Activity c) {
						return c.getName();
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return "[a-z ]+";
					}

					@Nullable
					@Override
					public Activity parse(@NotNull String s, @NotNull ParseContext context) {
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
					public @NotNull String toString(@NotNull AudioSite c, int flags) {
						return c.name().toLowerCase(Locale.ROOT);
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull AudioSite c) {
						return c.name().toLowerCase(Locale.ROOT);
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return "[a-z ]+";
					}

					@Nullable
					@Override
					public AudioSite parse(@NotNull String s, @NotNull ParseContext context) {
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
					public boolean canParse(@NotNull ParseContext context) {
						return false;
					}

					@Override
					public @NotNull String toString(@NotNull JDA o, int flags) {
						return BotManager.getNameByJDA(o);
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull JDA o) {
						return BotManager.getNameByJDA(o);
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return ".+";
					}
					@Nullable
					@Override
					public JDA parse(@NotNull String s, @NotNull ParseContext context) {
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
					public boolean canParse(@NotNull ParseContext context) {
						return false;
					}

					@Override
					public @NotNull String toString(@NotNull Emote o, int flags) {
						return o.getName();
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull Emote o) {
						return o.getName();
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return ".+";
					}
					@Nullable
					@Override
					public Emote parse(@NotNull String s, @NotNull ParseContext context) {
						return null;
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(Badge.class, "badge")
				.user("badges?")
				.name("Discord Badge")
				.description("Represent a discord badge")
				.since("1.0")
				.parser(new Parser<Badge>() {

					@Override
					public boolean canParse(@NotNull ParseContext context) {
						return false;
					}

					@Override
					public @NotNull String toString(@NotNull Badge o, int flags) {
						return o.name().toLowerCase(Locale.ROOT).replace("_", "");
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull Badge o) {
						return o.name().toLowerCase(Locale.ROOT).replace("_", "");
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return ".+";
					}
					@Nullable
					@Override
					public Badge parse(@NotNull String s, @NotNull ParseContext context) {
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
					public boolean canParse(@NotNull ParseContext context) {
						return false;
					}

					@Override
					public @NotNull String toString(@NotNull WebhookMessageBuilder o, int flags) {
						return Objects.requireNonNull(o.build().getUsername());
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull WebhookMessageBuilder o) {
						return Objects.requireNonNull(o.build().getUsername());
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return ".+";
					}
					@Nullable
					@Override
					public WebhookMessageBuilder parse(@NotNull String s, @NotNull ParseContext context) {
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
					public boolean canParse(@NotNull ParseContext context) {
						return false;
					}

					@Override
					public @NotNull String toString(@NotNull TextChannelBuilder o, int flags) {
						return o.getName();
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull TextChannelBuilder o) {
						return o.getName();
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return ".+";
					}
					@Nullable
					@Override
					public TextChannelBuilder parse(@NotNull String s, @NotNull ParseContext context) {
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
					public boolean canParse(@NotNull ParseContext context) {
						return context.equals(ParseContext.COMMAND);
					}

					@Override
					public @NotNull String toString(@NotNull TextChannel o, int flags) {
						return o.getName();
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull TextChannel o) {
						return o.getName();
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return ".+";
					}

					@Override
					public @NotNull TextChannel parse(final @NotNull String s, final @NotNull ParseContext context) {
						if (context.equals(ParseContext.COMMAND)) {
							CommandEvent lastEvent = CommandEvent.lastEvent;
							return (Objects.requireNonNull(Utils.parseLong(s, false, true) == null ? null : lastEvent.getGuild().getTextChannelById(Utils.parseLong(s, false, true))));
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
					public boolean canParse(@NotNull ParseContext context) {
						return context.equals(ParseContext.COMMAND);
					}
					@Override
					public @NotNull String toString(@NotNull User o, int flags) {
						return o.getName();
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull User o) {
						return o.getName() + "#" + o.getDiscriminator();
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return ".+";
					}

					@Override
					public @NotNull User parse(final @NotNull String s, final @NotNull ParseContext context) {
						if (context.equals(ParseContext.COMMAND)) {
							return (Objects.requireNonNull(Utils.parseLong(s, false, true) == null ? null : BotManager.getFirstBot().getUserById(Utils.parseLong(s, false, true))));
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
					public boolean canParse(@NotNull ParseContext context) {
						return context.equals(ParseContext.COMMAND);
					}

					@Override
					public @NotNull String toString(@NotNull Role o, int flags) {
						return o.getName();
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull Role o) {
						return o.getName();
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return ".+";
					}

					@Override
					public @NotNull Role parse(final @NotNull String s, final @NotNull ParseContext context) {
						if (context.equals(ParseContext.COMMAND)) {
							return (Objects.requireNonNull(Utils.parseLong(s, false, true) == null ? null : BotManager.getFirstBot().getRoleById(Utils.parseLong(s, false, true))));
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
					public boolean canParse(@NotNull ParseContext context) {
						return context.equals(ParseContext.COMMAND);
					}

					@Override
					public @NotNull String toString(@NotNull Member o, int flags) {
						return o.getEffectiveName();
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull Member o) {
						return o.getUser().getName() + "#" + o.getUser().getDiscriminator();
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return ".+";
					}

					@Override
					public @NotNull Member parse(final @NotNull String s, final @NotNull ParseContext context) {
						if (context.equals(ParseContext.COMMAND)) {
							CommandEvent lastEvent = CommandEvent.lastEvent;
							return (Objects.requireNonNull(Utils.parseLong(s, true, true) == null ? null : lastEvent.getGuild().getMemberById(Utils.parseLong(s, true, true))));
						}
						return null;
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(UpdatingMessage.class, "message")
				.user("messages?")
				.name("Discord Message")
				.description("Represent a discord message, with ID, author, reactions, etc...")
				.since("1.0")
				.parser(new Parser<UpdatingMessage>() {

					@Override
					public boolean canParse(@NotNull ParseContext context) {
						return false;
					}

					@Override
					public @NotNull String toString(@NotNull UpdatingMessage o, int flags) {
						return o.getMessage().getContentRaw();
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull UpdatingMessage o) {
						return o.getMessage().getContentRaw();
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return ".+";
					}
					@Nullable
					@Override
					public UpdatingMessage parse(@NotNull String s, @NotNull ParseContext context) {
						if (context.equals(ParseContext.COMMAND)) {
							Long input = Utils.parseLong(s, false, true);
							for (Map.Entry<String, JDA> entry : BotManager.getBots().entrySet()) {
								JDA jda = entry.getValue();
								for (Guild guild : jda.getGuilds()) {
									for (TextChannel channel : guild.getTextChannels()) {
										return UpdatingMessage.from(channel.retrieveMessageById(input).complete());
									}
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
					public boolean canParse(@NotNull ParseContext context) {
						return context.equals(ParseContext.COMMAND);
					}

					@Override
					public @NotNull String toString(@NotNull GuildChannel o, int flags) {
						return o.getName();
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull GuildChannel o) {
						return o.getName();
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return ".+";
					}

					@Override
					public @NotNull GuildChannel parse(final @NotNull String s, final @NotNull ParseContext context) {
						if (context.equals(ParseContext.COMMAND)) {
							CommandEvent lastEvent = CommandEvent.lastEvent;
							return (Objects.requireNonNull(Utils.parseLong(s, false, true) == null ? null : lastEvent.getGuild().getGuildChannelById(Utils.parseLong(s, false, true))));
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
					public boolean canParse(@NotNull ParseContext context) {
						return false;
					}

					@Override
					public @NotNull String toString(@NotNull Webhook o, int flags) {
						return o.getName();
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull Webhook o) {
						return o.getName();
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return ".+";
					}
					@Nullable
					@Override
					public Webhook parse(@NotNull String s, @NotNull ParseContext context) {
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
					public @NotNull String toString(@NotNull Guild o, int flags) {
						return o.getName();
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull Guild o) {
						return o.getName();
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return ".+";
					}

					@Override
					public boolean canParse(@NotNull ParseContext context) {
						return context.equals(ParseContext.COMMAND);
					}

					@Override
					public @NotNull Guild parse(final @NotNull String s, final @NotNull ParseContext context) {
						if (context.equals(ParseContext.COMMAND)) {
							CommandEvent lastEvent = CommandEvent.lastEvent;
							return (Objects.requireNonNull(Utils.parseLong(s, false, true) == null ? null : lastEvent.getBot().getGuildById(Utils.parseLong(s, false, true))));
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
					public @NotNull String toString(@NotNull EmbedBuilder o, int flags) {
						return o.getDescriptionBuilder().toString();
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull EmbedBuilder o) {
						return ToStringBuilder.reflectionToString(o);
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return ".+";
					}
					@Nullable
					@Override
					public EmbedBuilder parse(@NotNull String s, @NotNull ParseContext context) {
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
					public boolean canParse(@NotNull ParseContext context) {
						return false;
					}

					@Override
					public @NotNull String toString(@NotNull CommandObject o, int flags) {
						return o.getName();
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull CommandObject o) {
						return o.getName();
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return ".+";
					}
					@Nullable
					@Override
					public CommandObject parse(@NotNull String s, @NotNull ParseContext context) {
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
					public boolean canParse(@NotNull ParseContext context) {
						return false;
					}

					@Override
					public @NotNull String toString(@NotNull Invite o, int flags) {
						return o.getCode();
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull Invite o) {
						return o.getCode();
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return ".+";
					}

					@Nullable
					@Override
					public Invite parse(@NotNull String s, @NotNull ParseContext context) {
						if (context != ParseContext.COMMAND) return null;
						CommandEvent event = CommandEvent.lastEvent;
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
					public @NotNull String toString(@NotNull MessageBuilder o, int flags) {
						return o.toString();
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull MessageBuilder o) {
						return o.toString();
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return ".+";
					}
					@Nullable
					@Override
					public MessageBuilder parse(@NotNull String s, @NotNull ParseContext context) {
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
					public @NotNull String toString(@NotNull InviteBuilder o, int flags) {
						return o.toString();
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull InviteBuilder o) {
						return o.toString();
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return ".+";
					}

					@Nullable
					@Override
					public InviteBuilder parse(@NotNull String s, @NotNull ParseContext context) {
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
					public @NotNull String toString(Message.@NotNull Attachment o, int flags) {
						return o.getFileName();
					}

					@Override
					public @NotNull String toVariableNameString(Message.@NotNull Attachment o) {
						return o.getFileName();
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return ".+";
					}

					@Nullable
					@Override
					public Message.Attachment parse(@NotNull String s, @NotNull ParseContext context) {
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
					public @NotNull String toString(@NotNull RoleBuilder o, int flags) {
						return o.getName();
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull RoleBuilder o) {
						return o.getName();
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return ".+";
					}
					@Nullable
					@Override
					public RoleBuilder parse(@NotNull String s, @NotNull ParseContext context) {
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
					public @NotNull String toString(@NotNull VoiceChannelBuilder o, int flags) {
						return o.getName();
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull VoiceChannelBuilder o) {
						return o.getName();
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return ".+";
					}

					@Override
					public boolean canParse(@NotNull ParseContext context) {
						return false;
					}

					@Nullable
					@Override
					public VoiceChannelBuilder parse(@NotNull String s, @NotNull ParseContext context) {
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
					public @NotNull Permission parse(@NotNull String input, @NotNull ParseContext context) {
						for (Permission perm : Permission.values()) {
							if (perm.name().equalsIgnoreCase(input.toUpperCase(Locale.ROOT).replace(" ", "_"))) return perm;
						}
						return null;
					}

					@Override
					public @NotNull String toString(@NotNull Permission c, int flags) {
						return c.getName();
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull Permission perm) {
						return perm.getName().toLowerCase(Locale.ENGLISH).replace('_', ' ');
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return ".+";
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(GatewayIntent.class, "intent")
				.user("intents?")
				.name("Gateway Intents")
				.description("Gateway intent used in the bot builder scope.")
				.usage("guild members, guild bans, guild emojis, guild webhooks, guild invites, guild voice states, guild presences, guild messages, guild message reactions, guild message typing, direct messages, direct message reactions, direct message typing")
				.since("1.14")
				.parser(new Parser<GatewayIntent>() {
					@Override
					public @NotNull GatewayIntent parse(@NotNull String input, @NotNull ParseContext context) {
						for (GatewayIntent intent : GatewayIntent.values()) if (intent.name().equalsIgnoreCase(input.toUpperCase(Locale.ROOT).replace(" ", "_"))) return intent;
						return null;
					}

					@Override
					public @NotNull String toString(@NotNull GatewayIntent intent, int flags) {
						return intent.name().toLowerCase(Locale.ENGLISH).replace('_', ' ');
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull GatewayIntent intent) {
						return intent.name().toLowerCase(Locale.ENGLISH).replace('_', ' ');
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return ".+";
					}
				})
		);
		Classes.registerClass(new ClassInfo<>(CacheFlag.class, "cache")
				.user("caches?")
				.name("Cache")
				.description("Cache used in the bot builder scope. They determine which entity will be stored as cache.")
				.usage("activity, voice state, emote, client status, member overrides, role tags, online status")
				.since("1.14")
				.parser(new Parser<CacheFlag>() {
					@Override
					public @NotNull CacheFlag parse(@NotNull String input, @NotNull ParseContext context) {
						for (CacheFlag flag : CacheFlag.values()) if (flag.name().equalsIgnoreCase(input.toUpperCase(Locale.ROOT).replace(" ", "_"))) return flag;
						return null;
					}

					@Override
					public @NotNull String toString(@NotNull CacheFlag flag, int flags) {
						return flag.name().toLowerCase(Locale.ENGLISH).replace('_', ' ');
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull CacheFlag flag) {
						return flag.name().toLowerCase(Locale.ENGLISH).replace('_', ' ');
					}

					@Override
					public @NotNull String getVariableNamePattern() {
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
					public @NotNull PlayError parse(@NotNull String input, @NotNull ParseContext context) {
						for (PlayError perm : PlayError.values()) {
							if (perm.name().equalsIgnoreCase(input.replaceAll(" ", "_").toUpperCase(Locale.ROOT))) return perm;
						}
						return null;
					}

					@Override
					public @NotNull String toString(@NotNull PlayError c, int flags) {
						return c.name();
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull PlayError perm) {
						return perm.name().toLowerCase(Locale.ENGLISH).replace('_', ' ');
					}

					@Override
					public @NotNull String getVariableNamePattern() {
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

					@Override
					public @NotNull VoiceChannel parse(final @NotNull String s, final @NotNull ParseContext context) {
						if (context.equals(ParseContext.COMMAND)) {
							CommandEvent lastEvent = CommandEvent.lastEvent;
							return (Objects.requireNonNull(Utils.parseLong(s, false, true) == null ? null : lastEvent.getBot().getVoiceChannelById(Utils.parseLong(s, false, true))));
						}
						return null;
					}

					@Override
					public @NotNull String toString(@NotNull VoiceChannel c, int flags) {
						return c.getName();
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull VoiceChannel perm) {
						return perm.getName();
					}

					@Override
					public boolean canParse(@NotNull ParseContext context) {
						return true;
					}

					@Override
					public @NotNull String getVariableNamePattern() {
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
					public @NotNull OnlineStatus parse(@NotNull String input, @NotNull ParseContext context) {
						for (OnlineStatus status : OnlineStatus.values()) {
							if (status.name().equalsIgnoreCase(input.replaceAll(" ", "_").toUpperCase())) return status;
						}
						return null;
					}

					@Override
					public @NotNull String toString(@NotNull OnlineStatus status, int flags) {
						return status.name().toLowerCase(Locale.ENGLISH).replace('_', ' ');
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull OnlineStatus status) {
						return status.name().toLowerCase(Locale.ENGLISH).replace('_', ' ');
					}

					@Override
					public @NotNull String getVariableNamePattern() {
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
					public @NotNull String toString(@NotNull SlashCommand o, int flags) {
						return o.getName();
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull SlashCommand o) {
						return o.getName();
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return ".+";
					}
					@Nullable
					@Override
					public SlashCommand parse(@NotNull String s, @NotNull ParseContext context) {
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
					public @NotNull String toString(@NotNull AudioTrack o, int flags) {
						return o.getInfo().title;
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull AudioTrack o) {
						return o.getInfo().title;
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return ".+";
					}

					@Nullable
					@Override
					public AudioTrack parse(@NotNull String s, @NotNull ParseContext context) {
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
					public @NotNull String toString(@NotNull OptionType o, int flags) {
						return o.name();
					}

					@Override
					public @NotNull String toVariableNameString(@NotNull OptionType o) {
						return o.name();
					}

					@Override
					public @NotNull String getVariableNamePattern() {
						return ".+";
					}

					@Override
					public @NotNull OptionType parse(@NotNull String s, final @NotNull ParseContext context) {
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