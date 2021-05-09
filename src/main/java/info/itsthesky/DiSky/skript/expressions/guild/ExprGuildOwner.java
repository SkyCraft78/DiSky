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
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.event.Event;

@Name("Guild Owner")
@Description("Return the owner of the specific guild")
@Examples("reply with mention tag of owner of event-guild")
@Since("1.3")
public class ExprGuildOwner extends SimpleExpression<Member> {

	static {
		Skript.registerExpression(ExprGuildOwner.class, Member.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] [discord] [guild] owner of [the] [guild] %guild%");
	}

	private Expression<Guild> exprGuild;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprGuild = (Expression<Guild>) exprs[0];
		return true;
	}

	@Override
	protected Member[] get(final Event e) {
		Guild guild = exprGuild.getSingle(e);
		if (guild == null) return new Member[0];
		return new Member[] {guild.getOwner()};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Member> getReturnType() {
		return Member.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "owner of guild " + exprGuild.toString(e, debug);
	}

}