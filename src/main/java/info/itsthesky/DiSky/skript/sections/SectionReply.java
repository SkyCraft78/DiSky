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
import info.itsthesky.disky.skript.commands.CommandFactory;
import info.itsthesky.disky.skript.events.skript.EventReplySection;
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
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import java.util.Arrays;

import static info.itsthesky.disky.skript.effects.messages.EffReplyWith.*;

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
		ExprEventValues.eventValues.put(EventReplySection.class, Arrays.asList(
				valueMessage,
				valueMember,
				valueUser,
				valueGuild,
				valueBot
		));
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

				/* Message cast */
				MessageBuilder toSend = null;
				switch (message.getClass().getSimpleName()) {
					case "EmbedBuilder":
						toSend = new MessageBuilder().setEmbed(((EmbedBuilder) message).build());
						break;
					case "String":
						toSend = new MessageBuilder(message.toString());
						break;
					case "MessageBuilder":
						toSend = (MessageBuilder) message;
						break;
					case "Message":
						toSend = new MessageBuilder((Message) message);
						break;
				}
				if (toSend == null) {
					Skript.error("[DiSky] Cannot parse or cast the message in the send effect!");
					return;
				}

				Message storedMessage;
				if (IS_HOOK) {
					storedMessage = LAST_INTERACTION.getHook().setEphemeral(true).sendMessage(toSend.build()).complete(true);
					IS_HOOK = false;
				} else {
					storedMessage = LAST_CHANNEL.sendMessage(toSend.build()).complete(true);
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