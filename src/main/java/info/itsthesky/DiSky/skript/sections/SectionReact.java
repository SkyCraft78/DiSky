package info.itsthesky.disky.skript.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.managers.BotManager;
import info.itsthesky.disky.skript.commands.CommandFactory;
import info.itsthesky.disky.skript.events.skript.EventReactSection;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.EffectSection;
import info.itsthesky.disky.tools.StaticData;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.Emote;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import info.itsthesky.disky.tools.waiter.EventValue;
import info.itsthesky.disky.tools.waiter.ExprEventValues;
import info.itsthesky.disky.tools.waiter.WaiterListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import java.util.Arrays;

@Name("React to Message")
@Description("React to a message with an emote like the 'add reaction' effect. However, this section will be fired when someone react to this emote too.")
@Examples("react to event-message with reaction \"smile\"")
@Since("1.8")
public class SectionReact extends EffectSection {

	private static final EventValue<Message> valueMessage = new EventValue<>(Message.class, "message");
	private static final EventValue<Member> valueMember = new EventValue<>(Member.class, "member");
	private static final EventValue<User> valueUser = new EventValue<>(User.class, "user");
	private static final EventValue<Guild> valueGuild = new EventValue<>(Guild.class, "guild");
	private static final EventValue<JDA> valueBot = new EventValue<>(JDA.class, "bot");
	private static final EventValue<Emote> valueEmote = new EventValue<>(Emote.class, "emote");

	static {
		Skript.registerCondition(SectionReact.class,
				"["+ Utils.getPrefixName() +"] react to [the] [message] %message% with [emote] %emote% [using %-bot%] [to run]"
		);
		ExprEventValues.eventValues.put(EventReactSection.class, Arrays.asList(
				valueMessage,
				valueMember,
				valueUser,
				valueGuild,
				valueBot,
				valueEmote
		));
	}

	private Expression<Emote> exprReact;
	private Expression<UpdatingMessage> exprMessage;
	private Expression<JDA> exprName;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		exprMessage = (Expression<UpdatingMessage>) exprs[0];
		exprReact = (Expression<Emote>) exprs[1];
		if (exprs.length != 2) exprName = (Expression<JDA>) exprs[2];
		if (checkIfCondition()) return false;
		StaticData.lastArguments = CommandFactory.getInstance().currentArguments;

		if (hasSection()) loadSection("react effect", false, EventReactSection.class);
		return true;
	}

	@Override
	public void execute(Event event) {
		DiSkyErrorHandler.executeHandleCode(event, e -> {
			UpdatingMessage message = exprMessage.getSingle(event);
			Emote emote = exprReact.getSingle(event);
			if (message == null || emote == null) return;
			if (exprName != null) {
				JDA msgJDA = message.getMessage().getJDA();
				JDA botJDA = exprName.getSingle(event);
				if (botJDA == null) return;
				if (msgJDA != botJDA) return;
			}

			Object map = Variables.removeLocals(event);
			VariablesMaps.map.put(event, map);
			Variables.setLocalVariables(event, map);

			if (emote.isEmote()) {
				message.getMessage().addReaction(emote.getEmote()).queue(null, DiSkyErrorHandler::logException);
			} else {
				message.getMessage().addReaction(emote.getName()).queue(null, DiSkyErrorHandler::logException);
			}

			JDA botJDA = message.getMessage().getJDA();

			final Long msgID = message.getMessage().getIdLong();
			final TextChannel channel = message.getMessage().getTextChannel();
			WaiterListener.events.add(
					new WaiterListener.WaitingEvent<>(
							GuildMessageReactionAddEvent.class,

							ev -> msgID.equals(ev.getMessageIdLong())
									&& ev.getChannel().equals(channel)
									&& !botJDA.getSelfUser().getId().equals(ev.getUser().getId())
									&& Utils.areEmojiSimilar(ev.getReactionEmote(), emote),

							ev -> {
								if (VariablesMaps.map.get(event) != null) Variables.setLocalVariables(event, VariablesMaps.map.get(event));

								valueMessage.setObject(ev.getChannel().retrieveMessageById(ev.getMessageIdLong()).complete());
								valueGuild.setObject(ev.getGuild());
								valueMember.setObject(ev.getMember());
								valueUser.setObject(ev.getUser());
								valueBot.setObject(ev.getJDA());
								valueEmote.setObject(new Emote(ev.getReactionEmote()));

								runSection(event);

								try {
									if (((Cancellable) event).isCancelled()) ev.getReaction().removeReaction(ev.getUser()).queue(null, DiSkyErrorHandler::logException);
								} catch(ClassCastException ignored) { }
							}
					));
		});
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "react to message" + exprMessage.toString(e, debug) + " with reaction " + exprReact.toString(e, debug);
	}
}