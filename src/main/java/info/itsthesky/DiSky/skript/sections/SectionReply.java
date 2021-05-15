package info.itsthesky.disky.skript.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.skript.commands.CommandEvent;
import info.itsthesky.disky.skript.commands.CommandFactory;
import info.itsthesky.disky.skript.effects.messages.EffReplyWith;
import info.itsthesky.disky.skript.events.skript.EventReactSection;
import info.itsthesky.disky.skript.events.skript.EventReplySection;
import info.itsthesky.disky.skript.events.skript.messages.EventMessageReceive;
import info.itsthesky.disky.skript.events.skript.messages.EventPrivateMessage;
import info.itsthesky.disky.skript.events.skript.slashcommand.EventSlashCommand;
import info.itsthesky.disky.skript.expressions.messages.ExprLastMessage;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.EffectSection;
import info.itsthesky.disky.tools.StaticData;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.waiter.EventValue;
import info.itsthesky.disky.tools.waiter.ExprEventValues;
import info.itsthesky.disky.tools.waiter.WaiterListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import net.dv8tion.jda.api.interactions.commands.CommandHook;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

@Name("Reply and Wait")
@Description("Reply in a message, and wait until a user send another message after that. Can specify if the bot will detect it one time online or multiple.")
@Examples("reply with \"Hello world!\" to run:")
@Since("1.12")
public class SectionReply extends EffectSection {

	private static final EventValue<Message> valueMessage = new EventValue<>(Message.class, "message");
	private static final EventValue<Member> valueMember = new EventValue<>(Member.class, "member");
	private static final EventValue<User> valueUser = new EventValue<>(User.class, "user");
	private static final EventValue<Guild> valueGuild = new EventValue<>(Guild.class, "guild");
	private static final EventValue<JDA> valueBot = new EventValue<>(JDA.class, "bot");

	static {
		Skript.registerCondition(SectionReply.class,
				"["+ Utils.getPrefixName() +"] reply with [the] [message] %string/message/messagebuilder/embed% [and store it in %-object%] [and wait [the] answer from %-member%] [to run [one time]]"
		);
		ExprEventValues.values.put("message", valueMessage);
		ExprEventValues.values.put("member", valueMember);
		ExprEventValues.values.put("user", valueUser);
		ExprEventValues.values.put("guild", valueGuild);
		ExprEventValues.values.put("bot", valueBot);
	}

	private Expression<Object> exprMessage;
	private Expression<Object> exprVar;
	private Expression<Member> exprWaiter;
	private boolean oneTime = false;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		if (exprs.length == 1) {
			exprMessage = (Expression<Object>) exprs[0];
		} else if (exprs.length == 2) {
			exprMessage = (Expression<Object>) exprs[0];
			exprVar = (Expression<Object>) exprs[1];
		} else {
			exprMessage = (Expression<Object>) exprs[0];
			exprVar = (Expression<Object>) exprs[1];
			exprWaiter = (Expression<Member>) exprs[2];
		}
		if (checkIfCondition()) return false;
		StaticData.lastArguments = CommandFactory.getInstance().currentArguments;
		if (hasSection()) loadSection("reply section", false, EventReplySection.class);
		if (parseResult.expr.contains("to run one time")) oneTime = true;
		return true;
	}

	@Override
	public void execute(Event e) {
		DiSkyErrorHandler.executeHandleCode(e, Event -> {
			try {
				Object message = exprMessage.getSingle(e);
				if (message == null) return;
				if (!EffReplyWith.allowedEvents.contains(e.getEventName())) {
					DiSky.getInstance().getLogger().severe("You can't use 'reply with' effect without a discord guild based event!");
					return;
				}
				TextChannel channel = null;
				Message storedMessage;

				EventPrivateMessage eventPrivate = null;
				if (e.getEventName().equalsIgnoreCase("EventPrivateMessage")) {
					eventPrivate = (EventPrivateMessage) e;
				} else if (e instanceof EventMessageReceive) {
					channel = ((EventMessageReceive) e).getEvent().getTextChannel();
				} else if (e instanceof CommandEvent) {
					channel = (TextChannel) ((CommandEvent) e).getMessageChannel();
				} else if (e instanceof EventSlashCommand) {
            /* Slash command have their own reply system
            They're using webhook instead of user, and we
            need to use that, however Discord will wait forever for an answer :)
             */
					SlashCommandEvent event = ((EventSlashCommand) e).getEvent();
					CommandHook hook = event.getHook();
					hook.setEphemeral(true);
					if (message instanceof Message) {
						hook.sendMessage((Message) message).queue();
						return;
					} else if (message instanceof EmbedBuilder) {
						// Because of a JDA's bug, we can't send embed currently via ephemeral message :c
                /* MessageEmbed embed = ((EmbedBuilder) message).build();
                System.out.println(embed);
                hook.sendMessage(embed).queue(); */
						DiSky.getInstance().getLogger()
								.warning("Replying with Embed in slash command are currently not supported! See our discord for more information :)");
						hook.sendMessage(":warning: Error, see console for more information!").queue();
						return;
					} else if (message instanceof MessageBuilder) {
						hook.sendMessage(((MessageBuilder) message).build()).queue(null, DiSkyErrorHandler::logException);
						return;
					} else {
						hook.sendMessage(message.toString()).queue(null, DiSkyErrorHandler::logException);
						return;
					}
				}

				boolean isFromPrivate = false;
				if (eventPrivate != null) isFromPrivate = true;

				if (message instanceof Message) {
					if (isFromPrivate) {
						storedMessage = eventPrivate
								.getEvent()
								.getPrivateChannel()
								.sendMessage((Message) message).complete(true);
					} else {
						storedMessage = channel.getJDA()
								.getTextChannelById(
										channel.getId()
								).sendMessage((Message) message).complete(true);
					}
				} else if (message instanceof EmbedBuilder) {
					if (isFromPrivate) {
						storedMessage = eventPrivate
								.getEvent()
								.getPrivateChannel()
								.sendMessage(((EmbedBuilder) message).build()).complete(true);
					} else {
						storedMessage = channel.getJDA()
								.getTextChannelById(
										channel.getId()
								).sendMessage(((EmbedBuilder) message).build()).complete(true);
					}
				}  else if (message instanceof MessageBuilder) {
					if (isFromPrivate) {
						storedMessage = eventPrivate
								.getEvent()
								.getPrivateChannel()
								.sendMessage(((MessageBuilder) message).build()).complete(true);
					} else {
						storedMessage = channel.getJDA()
								.getTextChannelById(
										channel.getId()
								).sendMessage(((MessageBuilder) message).build()).complete(true);
					}
				} else {
					if (isFromPrivate) {
						storedMessage = eventPrivate
								.getEvent()
								.getPrivateChannel()
								.sendMessage(message.toString()).complete(true);
					} else {
						storedMessage = channel.getJDA()
								.getTextChannelById(
										channel.getId()
								).sendMessage(message.toString()).complete(true);
					}
				}
				ExprLastMessage.lastMessage = storedMessage;
				if (exprVar != null) {
					if (!exprVar.getClass().getName().equalsIgnoreCase("ch.njol.skript.lang.Variable")) return;
					Variable var = (Variable) exprVar;
					Utils.setSkriptVariable(var, storedMessage, e);
				}

				final boolean[] alreadyExecuted = {false};
				final TextChannel cha = storedMessage.getTextChannel();
				WaiterListener.events.add(
						new WaiterListener.WaitingEvent<>(
								GuildMessageReceivedEvent.class,

								ev -> ev.getChannel().equals(cha)
										&& !storedMessage.getJDA().getSelfUser().getId().equals(ev.getMember().getId()),

								ev -> {
									if (VariablesMaps.map.get(e) != null) Variables.setLocalVariables(e, VariablesMaps.map.get(e));

									valueMessage.setObject(ev.getMessage());
									valueGuild.setObject(ev.getGuild());
									valueMember.setObject(ev.getMember());
									valueUser.setObject(ev.getMember().getUser());
									valueBot.setObject(ev.getJDA());
									if (oneTime){
										if (alreadyExecuted[0])
											return;
									}

									alreadyExecuted[0] = true;
									runSection(e);
									try {
										if (((Cancellable) e).isCancelled()) ev.getMessage().delete().queue(null, DiSkyErrorHandler::logException);
									} catch(ClassCastException ignored) { }
								}
						));
			} catch (RateLimitedException ex) {
				DiSky.getInstance().getLogger().severe("DiSky tried to get a message, but was rate limited.");
			}
		});
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "reply with " + exprMessage.toString(e, debug) + " to run";
	}
}