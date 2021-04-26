package info.itsthesky.DiSky.skript.expressions.messagebuilder;

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
import info.itsthesky.DiSky.tools.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import org.bukkit.event.Event;

@Name("New Message Builder")
@Description("Return an empty discord message builder.")
@Examples("set {_m} to new message builder")
@Since("1.4")
public class ExprMessageBuilder extends SimpleExpression<MessageBuilder> {

	static {
		Skript.registerExpression(ExprMessageBuilder.class, MessageBuilder.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [a] new [discord] message builder"
		);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		return true;
	}

	@Override
	protected MessageBuilder[] get(Event e) {
		return new MessageBuilder[] {
				new MessageBuilder()};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends MessageBuilder> getReturnType() {
		return MessageBuilder.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "new message builder";
	}

}