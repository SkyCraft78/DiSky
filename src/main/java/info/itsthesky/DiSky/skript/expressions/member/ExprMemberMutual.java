package info.itsthesky.disky.skript.expressions.member;

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

@Name("Member Mutual Guild")
@Description("Return all mutual guilds of specific member")
@Examples("reply with \"We are both in %size of mutual guilds of event-member%\" guilds!")
@Since("1.3")
public class ExprMemberMutual extends SimpleExpression<Guild> {

	static {
		Skript.registerExpression(ExprMemberMutual.class, Guild.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] [discord] mutual guild[s] of [the] [member] %member%");
	}

	private Expression<Member> exprMember;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprMember = (Expression<Member>) exprs[0];
		return true;
	}

	@Override
	protected Guild[] get(final Event e) {
		Member member = exprMember.getSingle(e);
		if (member == null) return new Guild[0];
		return member.getUser().getMutualGuilds().toArray(new Guild[0]);
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends Guild> getReturnType() {
		return Guild.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "mutual guilds of member  " + exprMember.toString(e, debug);
	}

}