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
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.Message;
import org.bukkit.event.Event;

@Name("Mentioned Channels")
@Description("Return all (text) channels which are mentioned in a message.")
@Examples("set {_channels::*} to mentioned channels in event-message")
@Since("1.4.2")
public class ExprMessageMentionedChannels extends SimpleExpression<TextChannel> {

	static {
		Skript.registerExpression(ExprMessageMentionedChannels.class, TextChannel.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] mentioned [text] channel[s] of [the] [message] %message%"
		);
	}

	private Expression<UpdatingMessage> exprMessage;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprMessage = (Expression<UpdatingMessage>) exprs[0];
		return true;
	}

	@Override
	protected TextChannel[] get(Event e) {
		UpdatingMessage message = exprMessage.getSingle(e);
		if (message == null) return new TextChannel[0];
		return message.getMessage().getMentionedChannels().toArray(new TextChannel[0]);
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends TextChannel> getReturnType() {
		return TextChannel.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "mentioned text channels of message " + exprMessage.toString(e, debug);
	}

}