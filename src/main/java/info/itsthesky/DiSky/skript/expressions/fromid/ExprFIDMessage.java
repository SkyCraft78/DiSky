package info.itsthesky.disky.skript.expressions.fromid;

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
import info.itsthesky.disky.managers.BotManager;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;

@Name("Message from ID")
@Description("Return a message from its id. IT SEARCH ALL OVER DISCORD & EVERY CHANNEL! I highly recommend to specify the channel the message is in!")
@Examples("set {_msg} to discord message with id \"731885527762075648\" in channel with id \"731885527762075648\"")
@Since("1.11")
public class ExprFIDMessage extends SimpleExpression<Message> {

	static {
		Skript.registerExpression(ExprFIDMessage.class, Message.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [discord] message with [the] id %string% [in [the] [channel] %channel/textchannel%]"
		);
	}

	private Expression<String> exprID;
	private Expression<GuildChannel> exprChannel;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprID = (Expression<String>) exprs[0];
		if (exprs.length != 1) exprChannel = (Expression<GuildChannel>) exprs[1];
		return true;
	}

	@Override
	protected Message[] get(Event e) {
		String id = exprID.getSingle(e);
		JDA bot = BotManager.getFirstBot();
		if (bot == null || id == null) return new Message[0];
		if (!Utils.isNumeric(id)) return new Message[0];
		if (exprChannel != null && exprChannel.getSingle(e) != null && exprChannel.getSingle(e).getType().equals(ChannelType.TEXT)) {
			return new Message[] {((TextChannel) exprChannel.getSingle(e)).retrieveMessageById(id).complete()};
		} else {
			return new Message[] {Utils.searchMessage(bot, id)};
		}
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Message> getReturnType() {
		return Message.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "message with id " + exprID.toString(e, debug);
	}

}