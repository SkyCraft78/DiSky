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
import info.itsthesky.disky.tools.object.Emote;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Name("Guild Emotes")
@Description("Return all emotes of the specific guild")
@Examples("reply with \"This server have %size of emotes of event-guild% emotes!\"")
@Since("1.3")
public class ExprGuildEmotes extends SimpleExpression<Emote> {

	static {
		Skript.registerExpression(ExprGuildEmotes.class, Emote.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] [discord] [guild] emotes of [the] [guild] %guild%");
	}

	private Expression<Guild> exprGuild;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprGuild = (Expression<Guild>) exprs[0];
		return true;
	}

	@Override
	protected Emote[] get(final Event e) {
		Guild guild = exprGuild.getSingle(e);
		if (guild == null) return new Emote[0];
		final List<Emote> emotes = new ArrayList<>();
		for (net.dv8tion.jda.api.entities.Emote emote : guild.getEmotes()) emotes.add(Utils.unicodeFrom(emote.getId(), guild));
		return emotes.toArray(new Emote[0]);
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public @NotNull Class<? extends Emote> getReturnType() {
		return Emote.class;
	}

	@Override
	public @NotNull String toString(Event e, boolean debug) {
		return "emotes of guild " + exprGuild.toString(e, debug);
	}

}