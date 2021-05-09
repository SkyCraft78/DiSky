package info.itsthesky.disky.skript.expressions.guild;

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
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.event.Event;

@Name("Guild Text Channels")
@Description("Return all text channels of the specific guild")
@Examples("reply with \"This server have %size of textchannels of event-guild% text channels!\"")
@Since("1.3")
public class ExprGuildChannels extends SimpleExpression<TextChannel> {

	static {
		Skript.registerExpression(ExprGuildChannels.class, TextChannel.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] [discord] [guild] (textchannels|channels) of [the] [guild] %guild%");
	}

	private Expression<Guild> exprGuild;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprGuild = (Expression<Guild>) exprs[0];
		return true;
	}

	@Override
	protected TextChannel[] get(final Event e) {
		Guild guild = exprGuild.getSingle(e);
		if (guild == null) return new TextChannel[0];
		return guild.getTextChannels().toArray(new TextChannel[0]);
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
		return "text channels of guild " + exprGuild.toString(e, debug);
	}

}