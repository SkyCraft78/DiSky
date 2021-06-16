package info.itsthesky.disky.skript.sections;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.effects.Delay;
import ch.njol.skript.lang.*;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.skript.commands.CommandFactory;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.EffectSection;
import info.itsthesky.disky.tools.StaticData;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import info.itsthesky.disky.tools.events.EventValue;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Reply and Wait")
@Description("Reply in a message, and wait until a user send another message after that. Can specify if the bot will detect it one time online or multiple.")
@Examples("reply with \"Hello world!\" to run:")
@Since("1.12")
public class SectionReply extends EffectSection {

	private static final EventValue<UpdatingMessage> valueMessage = new EventValue<>(UpdatingMessage.class, "message");
	private static final EventValue<Member> valueMember = new EventValue<>(Member.class, "member");
	private static final EventValue<User> valueUser = new EventValue<>(User.class, "user");
	private static final EventValue<Guild> valueGuild = new EventValue<>(Guild.class, "guild");
	private static final EventValue<JDA> valueBot = new EventValue<>(JDA.class, "bot");

	static {
		//Skript.registerCondition(SectionReply.class,
		//		"["+ Utils.getPrefixName() +"] reply with [the] [message] %string/message/messagebuilder/embed% [and store it in %-object%] [and wait [the] answer from %-member%] [to run [one time]]"
		//);
		//ExprEventValues.eventValues.put(EventReplySection.class, Arrays.asList(
		//		valueMessage,
		//		valueMember,
		//		valueUser,
		//		valueGuild,
		//		valueBot
		//));
	}

	private Expression<Object> exprMessage;
	private Variable<?> variable;
	private Expression<Member> exprWaiter;
	private boolean oneTime = false;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		exprMessage = (Expression<Object>) exprs[0];
		exprWaiter = (Expression<Member>) exprs[2];

		Utils.setHasDelayBefore(Kleenean.TRUE);

		Expression<?> var = exprs[1];
		if (var != null && !(var instanceof Variable)) {
			Skript.error("Cannot store the message in a non-variable expression");
			return false;
		} else {
			variable = (Variable<?>) var;
		}
		if (checkIfCondition()) return false;
		StaticData.lastArguments = CommandFactory.getInstance().currentArguments;
		//if (hasSection()) loadSection("reply section", false, EventReplySection.class);
		if (parseResult.expr.contains("to run one time")) oneTime = true;
		return true;
	}

	@Override
	protected void execute(Event e) {
		walk(e);
	}

	@Override
	protected @Nullable TriggerItem walk(Event e) {
		DiSkyErrorHandler.executeHandleCode(e, event -> {
			Object message = exprMessage.getSingle(e);
			if (message == null) return;

			debug(e, true);

			Delay.addDelayedEvent(e); // Mark this event as delayed
			Object localVars = Variables.removeLocals(e); // Back up local variables

			if (!Skript.getInstance().isEnabled()) // See https://github.com/SkriptLang/Skript/issues/3702
				return;

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
				case "UpdatingMessage":
					toSend = new MessageBuilder(((UpdatingMessage) message).getMessage());
					break;
			}
			if (toSend == null) {
				Skript.error("[DiSky] Cannot parse or cast the message in the reply section!");
				return;
			}

			Member waiter = exprWaiter == null ? null : exprWaiter.getSingle(e);
			// TODO: 15/06/2021 Rework every section :'(
		});
		return null;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "reply with " + exprMessage.toString(e, debug) + " to run";
	}
}