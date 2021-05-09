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
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.event.Event;

@Name("Member Boost State")
@Description("Return if a member boost the guild or not.")
@Examples("discord command isBooster <member>:\n" +
		"\tprefixes: !\n" +
		"\ttrigger:\n" +
		"\t\tif arg-1 is a booster:\n" +
		"\t\t\treply with \":v: Yay! %mention tag of arg 1% is boosting the server, say a big thanks :heart:\"\n" +
		"\t\telse:\n" +
		"\t\t\treply with \":x: Oh no, %mention tag of arg 1% is not boosting the server! What a bad guy :<\"")
@Since("1.5.3")
public class ExprMemberIsNitro extends SimpleExpression<Boolean> {

	static {
		Skript.registerExpression(ExprMemberIsNitro.class, Boolean.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] [discord] boost[ing] state of [the] [member] %member%",
				"["+ Utils.getPrefixName() +"] [the] [member] %member% is [a] boost[er]"
				);
	}

	private Expression<Object> exprMember;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprMember = (Expression<Object>) exprs[0];
		return true;
	}

	@Override
	protected Boolean[] get(final Event e) {
		Object entity = exprMember.getSingle(e);
		if (!(entity instanceof Member)) return new Boolean[0];
		Member member = (Member) entity;
		return new Boolean[] {
				member.getRoles().contains(
						member.getGuild().getBoostRole()
				)
		};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Boolean> getReturnType() {
		return Boolean.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "member " + exprMember.toString(e, debug) + " is boosting";
	}

}