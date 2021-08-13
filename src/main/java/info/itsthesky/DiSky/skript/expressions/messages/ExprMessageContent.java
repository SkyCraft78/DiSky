package info.itsthesky.disky.skript.expressions.messages;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import info.itsthesky.disky.skript.scope.webhookmessage.ScopeWebhookMessage;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.Message;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Content of Message (or Webhook Message Builder")
@Description("Return the content (message itself) of a message instance.")
@Examples("set {_content} to message content of event-message")
@Since("1.0, 1.8 (Webhook message builder)")
public class ExprMessageContent extends SimplePropertyExpression<Object, String> {

	static {
		register(ExprMessageContent.class, String.class,
				"[the] [message] content",
				"message/webhookmessagebuilder"
		);
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@javax.annotation.Nullable
	@Override
	public Class<?>[] acceptChange(Changer.ChangeMode mode) {
		if (mode == Changer.ChangeMode.SET) {
			return CollectionUtils.array(String.class);
		}
		return CollectionUtils.array();
	}

	@Override
	protected String getPropertyName() {
		return "message content";
	}

	@Nullable
	@Override
	public String convert(Object o) {
		if (o instanceof UpdatingMessage) return ((UpdatingMessage) o).getMessage().getContentRaw();
		if (o instanceof WebhookMessageBuilder) return ((WebhookMessageBuilder) o).build().getContent();
		return null;
	}

	@Override
	public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
		if (delta == null || delta.length == 0 || delta[0] == null || !mode.equals(Changer.ChangeMode.SET)) return;
		String content = delta[0].toString();
		for (Object entity : getExpr().getAll(e)) {

			if (entity instanceof WebhookMessageBuilder) {
				WebhookMessageBuilder builder = (WebhookMessageBuilder) entity;
				builder.setContent(content);
				ScopeWebhookMessage.lastBuilder.setContent(content);
			}

		}
	}

}