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
import info.itsthesky.disky.skript.effects.messages.EffReplyWith;
import info.itsthesky.disky.skript.events.skript.EventButtonsSection;
import info.itsthesky.disky.skript.events.skript.EventReplySection;
import info.itsthesky.disky.skript.expressions.messages.ExprLastMessage;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.EffectSection;
import info.itsthesky.disky.tools.StaticData;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.ButtonBuilder;
import info.itsthesky.disky.tools.waiter.EventValue;
import info.itsthesky.disky.tools.waiter.ExprEventValues;
import info.itsthesky.disky.tools.waiter.WaiterListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.RateLimitedException;
import net.dv8tion.jda.api.interactions.ActionRow;
import net.dv8tion.jda.api.interactions.Component;
import net.dv8tion.jda.api.interactions.button.Button;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static info.itsthesky.disky.skript.effects.messages.EffReplyWith.*;

@Name("Button Section")
@Description("Add a new button to a message and wait the click event. the event values represent the value inside the new event, not the main event !")
@Examples("add last button to message {_msg} to run:")
@Since("1.12")
public class SectionButtons extends EffectSection {

	private static final EventValue<Message> valueMessage = new EventValue<>(Message.class, "message");
	private static final EventValue<Member> valueMember = new EventValue<>(Member.class, "member");
	private static final EventValue<User> valueUser = new EventValue<>(User.class, "user");
	private static final EventValue<Guild> valueGuild = new EventValue<>(Guild.class, "guild");
	private static final EventValue<JDA> valueBot = new EventValue<>(JDA.class, "bot");
	private static final EventValue<GuildChannel> valueChannel = new EventValue<>(GuildChannel.class, "channel");
	private static final EventValue<TextChannel> valueText = new EventValue<>(TextChannel.class, "textchannel");

	static {
		Skript.registerCondition(SectionButtons.class,
				"["+ Utils.getPrefixName() +"] (add|append) [button] %button% to [(message|buttons of)] %message% [to run [one time]]"
		);
		ExprEventValues.eventValues.put(EventButtonsSection.class, Arrays.asList(
				valueMessage,
				valueMember,
				valueUser,
				valueGuild,
				valueBot,
				valueChannel,
				valueText
		));
	}

	private Expression<ButtonBuilder> exprButton;
	private Expression<Message> exprMessage;
	private boolean oneTime = false;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		exprButton = (Expression<ButtonBuilder>) exprs[0];
		exprMessage = (Expression<Message>) exprs[1];
		if (checkIfCondition()) return false;
		StaticData.lastArguments = CommandFactory.getInstance().currentArguments;
		if (hasSection()) loadSection("reply section", false, EventButtonsSection.class);
		if (parseResult.expr.contains("to run one time")) oneTime = true;
		return true;
	}

	@Override
	public void execute(Event event) {
		DiSkyErrorHandler.executeHandleCode(event, Event -> {
			Message message = exprMessage.getSingle(event);
			ButtonBuilder button = exprButton.getSingle(event);
			if (message == null || button == null) return;

			if (button.build() == null) return;
			List<Button> current = StaticData.actions.containsKey(message.getIdLong()) ? StaticData.actions.get(message.getIdLong()) : new ArrayList<>();
			current.add(button.build());
			StaticData.actions.put(message.getIdLong(), current);
			message
					.editMessage(new MessageBuilder(message).build())
					.setActionRows(ActionRow.of(current.toArray(new Component[0])))
					.queue(null, DiSkyErrorHandler::logException);

			Object map = Variables.removeLocals(event);
			VariablesMaps.map.put(event, map);
			Variables.setLocalVariables(event, map);

			final boolean[] alreadyExecuted = {false};
			final TextChannel cha = message.getTextChannel();
			WaiterListener.events.add(
					new WaiterListener.WaitingEvent<>(
							ButtonClickEvent.class,

							ev -> ev.getChannel().equals(cha)
									&& message.getId().equals(ev.getMessageId())
									&& ev.getButton().getId().equals(button.getId()),

							e -> {
								if (VariablesMaps.map.get(event) != null) Variables.setLocalVariables(event, VariablesMaps.map.get(event));

								valueMessage.setObject(e.getMessage());
								valueGuild.setObject(e.getGuild());
								valueMember.setObject(e.getMember());
								valueUser.setObject(e.getMember().getUser());
								valueBot.setObject(e.getJDA());
								valueChannel.setObject(e.getTextChannel());
								valueText.setObject(e.getTextChannel());
								if (oneTime){
									if (alreadyExecuted[0])
										return;
								}

								alreadyExecuted[0] = true;
								LAST_CHANNEL = e.getChannel();
								IS_HOOK = false;
								runSection(event);
							}
					));
		});
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "add button " + exprButton.toString(e, debug) + " to message " + exprMessage.toString(e, debug) + " to run";
	}
}