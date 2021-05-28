package info.itsthesky.disky.skript.expressions.messages;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.ButtonBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.ActionRow;
import net.dv8tion.jda.api.interactions.button.Button;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

@Name("Message Buttons")
@Description("Return the all buttons a message have.")
@Examples("buttons of event-message")
@Since("1.0")
public class ExprMessageButtons extends SimpleExpression<ButtonBuilder> {

	static {
		Skript.registerExpression(ExprMessageButtons.class, ButtonBuilder.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] button[s] of [the] [message] %message%"
		);
	}

	private Expression<Message> exprMessage;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprMessage = (Expression<Message>) exprs[0];
		return true;
	}

	@Override
	protected ButtonBuilder[] get(Event e) {
		Message message = exprMessage.getSingle(e);
		if (message == null) return new ButtonBuilder[0];
		List<ButtonBuilder> buttons = new ArrayList<>();
		for (ActionRow action : message.getActionRows()) {
			for (Button button : action.getButtons()) {
				buttons.add(ButtonBuilder.fromButton(button));
			}
		}
		return buttons.toArray(new ButtonBuilder[0]);
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends ButtonBuilder> getReturnType() {
		return ButtonBuilder.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "buttons of message " + exprMessage.toString(e, debug);
	}

}