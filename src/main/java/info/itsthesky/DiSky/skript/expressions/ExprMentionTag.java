package info.itsthesky.DiSky.skript.expressions;

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
import info.itsthesky.DiSky.managers.BotManager;
import info.itsthesky.DiSky.tools.Utils;
import info.itsthesky.DiSky.tools.object.Emote;
import info.itsthesky.DiSky.tools.object.messages.Channel;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;

@Name("Mention Tag of Discord entity")
@Description("Return the mention tag (@me) from a channel, user, role, etc...")
@Examples("reply with mention tag of event-user")
@Since("1.1")
public class ExprMentionTag extends SimpleExpression<String> {

	static {
		Skript.registerExpression(ExprMentionTag.class, String.class, ExpressionType.SIMPLE,
				"["+ Utils.getPrefixName() +"] [the] [discord] mention [tag] of [the] [discord] [entity] %role/emote/user/bot/member/channel/textchannel%");
	}

	private Expression<Object> exprEntity;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		exprEntity = (Expression<Object>) exprs[0];
		return true;
	}

	@Override
	protected String[] get(final Event e) {
		Object entity = exprEntity.getSingle(e);
		if (entity instanceof JDA) {
			final JDA bot = BotManager.getBot(entity.toString());
			if (bot == null)
				return new String[0];
			entity = bot.getSelfUser();
		}

		if (entity instanceof User) {
			return new String[] {((User) entity).getAsMention()};
		} else if (entity instanceof TextChannel) {
			return new String[] {((TextChannel) entity).getAsMention()};
		} else if (entity instanceof GuildChannel) {
			GuildChannel channel = (GuildChannel) entity;
			if (channel.getType().equals(ChannelType.TEXT)) return new String[] {((TextChannel) channel).getAsMention()};
			return new String[] {channel.getName()};
		} else if (entity instanceof Role) {
			return new String[] {((Role) entity).getAsMention()};
		} else if (entity instanceof Member) {
			return new String[] {((Member) entity).getAsMention()};
		} else if (entity instanceof Emote) {
			return new String[] {((Emote) entity).getAsMention()};
		} else if (entity instanceof JDA) {
			return new String[] {((JDA) entity).getSelfUser().getAsMention()};
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
		return "mention tag of " + exprEntity.toString(e, debug);
	}

}