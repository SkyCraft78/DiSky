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
import org.bukkit.event.Event;

import java.time.format.DateTimeFormatter;

@Name("Guild Date Creation")
@Description("Return the date when the guild were created.")
@Examples("reply with \"This guild was made in %creation date of event-guild%\"")
@Since("1.3")
public class ExprGuildCreation extends SimpleExpression<String> {

	static {
		Skript.registerExpression(ExprGuildCreation.class, String.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] [discord] [guild] creation date of [the] [guild] %guild%");
	}

	private Expression<Guild> exprGuild;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprGuild = (Expression<Guild>) exprs[0];
		return true;
	}

	@Override
	protected String[] get(final Event e) {
		Guild guild = exprGuild.getSingle(e);
		if (guild == null) return new String[0];
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:s");
		return new String[] {guild.getTimeCreated().format(dtf)};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "creation date of guild " + exprGuild.toString(e, debug);
	}

}