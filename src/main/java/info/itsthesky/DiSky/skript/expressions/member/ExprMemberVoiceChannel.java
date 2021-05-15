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
import info.itsthesky.disky.managers.music.AudioUtils;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.bukkit.event.Event;

@Name("Member Voice Channel")
@Description("Get the voice channel where the member currently is in. Must specify the guild if you're using the bot pattern.")
@Since("1.6")
public class ExprMemberVoiceChannel extends SimpleExpression<VoiceChannel> {

	static {
		Skript.registerExpression(ExprMemberVoiceChannel.class, VoiceChannel.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [discord] [voice] channel of [the] [(member|bot)] %member/bot% [in [the] [guild] %-guild%]");
	}

	private Expression<Object> exprEntity;
	private Expression<Guild> exprGuild;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprEntity = (Expression<Object>) exprs[0];
		if (exprs.length != 1) exprGuild = (Expression<Guild>) exprs[1];
		return true;
	}

	@Override
	protected VoiceChannel[] get(final Event e) {
		Object entity = exprEntity.getSingle(e);
		if (entity == null) return new VoiceChannel[0];
		if (entity instanceof Member) {
			return new VoiceChannel[] {((Member) entity).getVoiceState().getChannel()};
		} else {
			if (exprGuild == null || exprGuild.getSingle(e) == null) return new VoiceChannel[0];
			Guild guild = exprGuild.getSingle(e);
			JDA bot = (JDA) exprEntity.getSingle(e);
			if (!Utils.areJDASimilar(guild.getJDA(), bot)) return new VoiceChannel[0];
			return new VoiceChannel[] {guild.getSelfMember().getVoiceState().getChannel()};
		}
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
		return "voice channel of member " + exprEntity.toString(e, debug) + (exprGuild != null ? " in guild " + exprGuild.toString(e, debug) : "");
	}

}