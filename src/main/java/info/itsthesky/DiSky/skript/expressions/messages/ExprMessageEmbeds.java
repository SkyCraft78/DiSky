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
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

@Name("Embeds of Message")
@Description("Return all embeds from a message.")
@Examples("embeds of event-message")
@Since("1.14")
public class ExprMessageEmbeds extends SimpleExpression<EmbedBuilder> {

	static {
		Skript.registerExpression(ExprMessageEmbeds.class, EmbedBuilder.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] embed[s] of [the] [message] %message%"
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
	protected EmbedBuilder[] get(Event e) {
		final UpdatingMessage message = exprMessage.getSingle(e);
		if (message == null) return new EmbedBuilder[0];
		final List<EmbedBuilder> builders = new ArrayList<>();
		for (MessageEmbed embed : message.getMessage().getEmbeds()) builders.add(new EmbedBuilder(embed));
		return builders.toArray(new EmbedBuilder[0]);
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends EmbedBuilder> getReturnType() {
		return EmbedBuilder.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "embeds of message " + exprMessage.toString(e, debug);
	}

}