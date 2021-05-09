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
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.bukkit.event.Event;

@Name("Member Voice Channel")
@Description("Get the voice channel where the member currently is in.")
@Examples("")
@Since("1.6")
public class ExprMemberVoiceChannel extends SimpleExpression<VoiceChannel> {

	static {
		Skript.registerExpression(ExprMemberVoiceChannel.class, VoiceChannel.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [discord] [voice] channel of [the] [member] %member%");
	}

	private Expression<Member> exprMember;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprMember = (Expression<Member>) exprs[0];
		return true;
	}

	@Override
	protected VoiceChannel[] get(final Event e) {
		Member entity = exprMember.getSingle(e);
		if (entity == null) return new VoiceChannel[0];
		return new VoiceChannel[] {entity.getVoiceState().getChannel()};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends VoiceChannel> getReturnType() {
		return VoiceChannel.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "voice channel of member " + exprMember.toString(e, debug);
	}

}