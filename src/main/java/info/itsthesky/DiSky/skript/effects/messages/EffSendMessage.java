package info.itsthesky.disky.skript.effects.messages;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.WaiterEffect;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.bukkit.event.Event;

@Name("Send discord Message")
@Description("Send a message in a specific channel, with a specific bot. Use that syntax only for non-textchannel event.")
@Examples("on load:\n" +
        "\tmake embed:\n" +
        "\t\tset title of embed to \"The bot has been started!\"\n" +
        "\t\tset color of embed to green\n" +
        "\t\tset timestamp of embed to now\n" +
        "\tsend last embed to text channel with id \"818182473502294066\"")
@Since("1.0")
public class EffSendMessage extends WaiterEffect {

    static {
        Skript.registerEffect(EffSendMessage.class,
                "["+ Utils.getPrefixName() +"] send [message] %string/message/embed/messagebuilder% to [the] [(user|channel)] %user/member/textchannel/channel% [with [(component|row)[s]] %-buttonrows/selectbuilder%] [with [the] %-bot%] [and store it in %-object%]");
    }

    private Expression<Object> exprMessage;
    private Expression<Object> exprChannel;
    private Expression<Object> exprComponents;
    private Variable<?> variable;
    private Expression<JDA> exprBot;

    @Override
    @SuppressWarnings("unchecked")
    public boolean initEffect(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprMessage = (Expression<Object>) exprs[0];
        exprChannel = (Expression<Object>) exprs[1];
        exprComponents = (Expression<Object>) exprs[2];
        exprBot = (Expression<JDA>) exprs[3];
        Expression<?> var = exprs[4];
        if (var != null && !(var instanceof Variable)) {
            Skript.error("Cannot store the message in a non-variable expression");
            return false;
        } else {
            variable = (Variable<?>) var;
        }
        return true;
    }

    @Override
    public void runEffect(Event e) {
        Object entity = exprChannel.getSingle(e);
        Object content = exprMessage.getSingle(e);
        Object component = Utils.verifyVars(e, exprComponents);
        JDA bot = Utils.verifyVar(e, exprBot);
        if (entity == null || content == null) return;

        /* Message cast */
        MessageBuilder builder = null;
        if (content instanceof EmbedBuilder) builder = new MessageBuilder().setEmbeds(((EmbedBuilder) content).build());
        if (content instanceof MessageBuilder) builder = (MessageBuilder) content;
        if (content instanceof String) builder = new MessageBuilder(content.toString());
        if (builder == null) {
            Skript.error("[DiSky] Cannot parse or cast the message in the send effect!");
            return;
        }

        /* Channel cast with consumer */
        if (entity instanceof GuildChannel) {
            runChannel((MessageChannel) entity, e, builder, component);
            return;
        } else if (entity instanceof User || entity instanceof Member) {
            User user = (entity instanceof User) ? (User) entity : ((Member) entity).getUser();
            MessageBuilder finalBuilder = builder;
            Utils.handleRestAction(
                    user.openPrivateChannel(),
                    channel -> runChannel(channel, e, finalBuilder, component),
                    null
            );
            return;
        }
        restart();
    }

    private void runChannel(MessageChannel channel, Event event, MessageBuilder builder, Object components) {

        if (channel == null) {
            restart();
            return;
        }

        MessageAction action = channel.sendMessage(builder.build());
        action = Utils.parseComponents(action, components);
        action.queue(
                message -> {
                    if (variable != null)
                        variable.change(event, new UpdatingMessage[] {UpdatingMessage.from(message)}, Changer.ChangeMode.SET);
                    restart();
                }
        );

    }

    @Override
    public String toString(Event e, boolean debug) {
        return "send discord message " + exprMessage.toString(e, debug) + " to channel or user " + exprChannel.toString(e, debug);
    }
}
