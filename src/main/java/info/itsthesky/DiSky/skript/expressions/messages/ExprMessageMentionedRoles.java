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
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.Message;
import org.bukkit.event.Event;

@Name("Mentioned Roles")
@Description("Return all roles which are mentioned in a message.")
@Examples("set {_roles::*} to mentioned roles in event-message")
@Since("1.4.2")
public class ExprMessageMentionedRoles extends SimpleExpression<Role> {

	static {
		Skript.registerExpression(ExprMessageMentionedRoles.class, Role.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] mentioned role[s] in [the] [message] %message%"
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
	protected Role[] get(Event e) {
		UpdatingMessage message = exprMessage.getSingle(e);
		if (message == null) return new Role[0];
		return message.getMessage().getMentionedRoles().toArray(new Role[0]);
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends Role> getReturnType() {
		return Role.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "mentioned roles of message " + exprMessage.toString(e, debug);
	}

}