package info.itsthesky.disky.skript.expressions.member;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
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
import net.dv8tion.jda.api.entities.User;
import org.bukkit.event.Event;

@Name("User Banned")
@Description("See if the specific user is banned from the specific guild")
@Since("1.4")
public class ExprUserIsBanned extends SimpleExpression<Boolean> {

	static {
		Skript.registerExpression(ExprUserIsBanned.class, Boolean.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [(user|member)] %user/member% is ban[ned] from [the] [guild] %guild%");
	}

	private Expression<Object> exprMember;
	private Expression<Guild> exprGuild;
	private int pattern;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprMember = (Expression<Object>) exprs[0];
		exprGuild = (Expression<Guild>) exprs[1];
		this.pattern = matchedPattern;
		return true;
	}

	@Override
	protected Boolean[] get(final Event e) {
		Object entity = exprMember.getSingle(e);
		Guild guild = exprGuild.getSingle(e);
		if (entity == null || guild == null) return new Boolean[0];
		User finalUser = null;
		if (entity instanceof User) {
			finalUser = (User) entity;
		} else if (entity instanceof Member) {
			finalUser = ((Member) entity).getUser();
		}
		if (finalUser == null) return new Boolean[0];
		boolean banned = guild.retrieveBan(finalUser).complete() != null;
		if (this.pattern == 0) {
			return new Boolean[] {banned};
		} else {
			return new Boolean[] {!banned};
		}
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
		return "user " + exprMember.toString(e, debug) + " is banned from guild " + exprGuild.toString(e, debug);
	}

}