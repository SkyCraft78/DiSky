package info.itsthesky.disky.skript.expressions.messages;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.skript.effects.grab.EffPurgeMessages;
import info.itsthesky.disky.skript.events.message.MessageDelete;
import info.itsthesky.disky.tools.Utils;
import org.bukkit.event.Event;

@Name("Is Deleted message Purged")
@Description("Check if the deleted message in a message delete event was purged by the purge effect or not.")
@Examples({"if the message was purged:"})
@Since("1.0")
public class CondMessageWasPurged extends Condition {

	public static Long lastMessageID;
	static {
		Skript.registerCondition(CondMessageWasPurged.class,
				"["+ Utils.getPrefixName() +"] [the] message (is|was) purged",
				"["+ Utils.getPrefixName() +"] [the] message (is not|isn't|was|wasn't) purged"
		);
	}

	private boolean negate;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		negate = matchedPattern == 1;
		return ScriptLoader.isCurrentEvent(MessageDelete.EvtMessageDelete.class);
	}

	@Override
	public boolean check(Event event) {
		if (lastMessageID == null) return false;
		if (negate) {
			return EffPurgeMessages.PURGED_MESSAGES.get(lastMessageID.toString()) != null &&
					EffPurgeMessages.PURGED_MESSAGES.get(lastMessageID.toString());
		} else {
			return !(EffPurgeMessages.PURGED_MESSAGES.get(lastMessageID.toString()) != null &&
					EffPurgeMessages.PURGED_MESSAGES.get(lastMessageID.toString()));
		}
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "the message was purged";
	}

}