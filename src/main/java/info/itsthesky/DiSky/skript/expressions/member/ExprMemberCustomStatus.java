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
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.event.Event;

@Name("Member Custom Status")
@Description("Return the custom status of a specific member")
@Examples("reply with \"I love your %custom status of event-member% status!\"")
@Since("1.3")
public class ExprMemberCustomStatus extends SimpleExpression<String> {

	static {
		Skript.registerExpression(ExprMemberCustomStatus.class, String.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] [discord] custom status of [the] [member] %member%");
	}

	private Expression<Object> exprMember;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprMember = (Expression<Object>) exprs[0];
		return true;
	}

	@Override
	protected String[] get(final Event e) {
		Object entity = exprMember.getSingle(e);
		if (!(entity instanceof Member)) return new String[0];
		Member member = (Member) entity;
		for (Activity activity : member.getActivities()) {
			if (activity.getType().equals(Activity.ActivityType.CUSTOM_STATUS)) {
				return new String[] {activity.getName()};
			}
		}
		return new String[0];
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
		return "discriminator of user  " + exprMember.toString(e, debug);
	}

}