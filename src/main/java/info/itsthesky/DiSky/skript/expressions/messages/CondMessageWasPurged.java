package info.itsthesky.disky.skript.expressions.messages;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.skript.effects.grab.EffPurgeMessages;
import info.itsthesky.disky.skript.events.skript.messages.EventMessageDelete;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import org.bukkit.event.Event;

@Name("Is Deleted message Purged")
@Description("Check if the deleted message in a message delete event was purged by the purge effect or not.")
@Examples({"if the message was purged:"})
@Since("1.0")
public class CondMessageWasPurged extends Condition {

	public static Long lastMessageID;
	static {
		Skript.registerCondition(CondMessageWasPurged.class,
				"["+ Utils.getPrefixName() +"] [the] message (is|was) purged"
		);
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		return ScriptLoader.isCurrentEvent(EventMessageDelete.class);
	}

	@Override
	public boolean check(Event event) {
		if (lastMessageID == null) return false;
		return EffPurgeMessages.PURGED_MESSAGES.get(lastMessageID.toString()) != null &&
				EffPurgeMessages.PURGED_MESSAGES.get(lastMessageID.toString());
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "the message was purged";
	}

}