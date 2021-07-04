package info.itsthesky.disky.skript.sections;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import net.dv8tion.jda.api.MessageBuilder;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.events.BukkitEvent;
import info.itsthesky.disky.tools.events.InteractionEvent;
import info.itsthesky.disky.tools.events.MessageEvent;
import info.itsthesky.disky.tools.object.ButtonBuilder;
import info.itsthesky.disky.tools.object.ButtonRow;
import info.itsthesky.disky.tools.object.Emote;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import info.itsthesky.disky.tools.section.DiSkySection;
import info.itsthesky.disky.tools.section.WaiterListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SectionReply extends DiSkySection {

    private Expression<Object> exprMessage;
    private Expression<ButtonRow> exprRow;
    private boolean ephemeral = false;
    private Variable<?> variable;

    static {
        register(
                "reply with [(personal|hidden)] [the] [message] %string/message/messagebuilder/embed% [with [row[s]] %-buttonrows%] [and store it in %-object%] [to run]",
                SectionReply.class
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {

        if (
                !(Arrays.asList(ScriptLoader.getCurrentEvents()[0].getInterfaces()).contains(MessageEvent.class)) &&
                        !(Arrays.asList(ScriptLoader.getCurrentEvents()[0].getInterfaces()).contains(InteractionEvent.class))
        ) {
            Skript.error("The reply effect cannot be used in a non channel-related event!");
            return false;
        }

        exprMessage = (Expression<Object>) exprs[0];
        exprRow = (Expression<ButtonRow>) exprs[1];
        Expression<?> var = exprs[2];
        if (var != null && !(var instanceof Variable)) {
            Skript.error("Cannot store the message in a non-variable expression");
            return false;
        }
        variable = (Variable<?>) var;
        ephemeral = parseResult.expr.contains("reply with personal") || parseResult.expr.contains("reply with hidden");

        return init("reply section", SectionReplyEvent.class, parseResult);
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, event -> {
            Object content = exprMessage.getSingle(e);
            ButtonRow[] rows = exprRow == null ? new ButtonRow[0] : exprRow.getAll(e);
            if (content == null) return;

            /* Message cast */
            MessageBuilder toSend;
            switch (content.getClass().getSimpleName()) {
                case "EmbedBuilder":
                    toSend = new MessageBuilder().setEmbed(((EmbedBuilder) content).build());
                    break;
                case "String":
                    toSend = new MessageBuilder(content.toString());
                    break;
                case "MessageBuilder":
                    toSend = (MessageBuilder) content;
                    break;
                case "Message":
                    toSend = new MessageBuilder((Message) content);
                    break;
                default:
                    Skript.error("[DiSky] Cannot parse or cast the message in the reply effect!");
                    return;
            }

            if (event instanceof InteractionEvent) {
                GenericInteractionCreateEvent ev = ((InteractionEvent) event).getInteractionEvent();
                ReplyAction action = ev.reply(toSend.build());
                if (rows != null || rows.length == 0) {
                    List<ActionRow> rows1 = new ArrayList<>();
                    for (ButtonRow row : rows) {
                        List<Button> buttons = new ArrayList<>();

                        for (ButtonBuilder buttonBuilder : row.getButtons()) {
                            if (buttonBuilder.build() != null)
                                buttons.add(buttonBuilder.build());
                        }
                        if (buttons.size() > 0) rows1.add(ActionRow.of(buttons.toArray(new Component[0])));
                    }
                    action = action.addActionRows(rows1);
                }
                action.setEphemeral(ephemeral).queue();
                return;
            }

            MessageChannel channel = null;
            if (e instanceof MessageEvent)
                channel = ((MessageEvent) e).getMessageChannel();

            if (channel == null) return;
            MessageAction action = channel
                    .sendMessage(toSend.build());
            if (rows != null || rows.length == 0) {
                List<ActionRow> rows1 = new ArrayList<>();
                for (ButtonRow row : rows) {
                    List<Button> buttons = new ArrayList<>();

                    for (ButtonBuilder buttonBuilder : row.getButtons()) {
                        if (buttonBuilder.build() != null)
                            buttons.add(buttonBuilder.build());
                    }
                    if (buttons.size() > 0) rows1.add(ActionRow.of(buttons.toArray(new Component[0])));
                }
                action = action.setActionRows(rows1);
            }
            Object vars = Variables.removeLocals(event);
            Variables.setLocalVariables(event, vars);
            action.queue(
                    message -> {

                        String idToCompare = message.getId();
                        WaiterListener.events.add(new WaiterListener.WaitingEvent<>(
                                ButtonClickEvent.class,
                                ev -> ev.getMessageId().equals(idToCompare),
                                ev -> {
                                    SectionReplyEvent sectionEvent = new SectionReplyEvent(ev);
                                    if (sectionEvent.isCancelled()) {
                                        sectionEvent.JDAEvent.deferEdit().queue();
                                    }
                                    Variables.setLocalVariables(sectionEvent, vars);
                                    runSection(sectionEvent);
                                }
                        ));
                    }
            );

        });
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "reply with message " + exprMessage.toString(e, debug) + (variable != null ? " and store it in " + variable.toString(e, debug) : "") + " to run:";
    }

    static {
        EventValues.registerEventValue(SectionReplyEvent.class, Member.class, new Getter<Member, SectionReplyEvent>() {
            @Override
            public Member get(SectionReplyEvent event) {
                return event.JDAEvent.getMember();
            }
        }, 0);

        EventValues.registerEventValue(SectionReplyEvent.class, User.class, new Getter<User, SectionReplyEvent>() {
            @Override
            public User get(SectionReplyEvent event) {
                return event.JDAEvent.getUser();
            }
        }, 0);

        EventValues.registerEventValue(SectionReplyEvent.class, JDA.class, new Getter<JDA, SectionReplyEvent>() {
            @Override
            public JDA get(SectionReplyEvent event) {
                return event.JDAEvent.getJDA();
            }
        }, 0);

        EventValues.registerEventValue(SectionReplyEvent.class, GuildChannel.class, new Getter<GuildChannel, SectionReplyEvent>() {
            @Override
            public GuildChannel get(SectionReplyEvent event) {
                return (GuildChannel) event.JDAEvent.getChannel();
            }
        }, 0);

        EventValues.registerEventValue(SectionReplyEvent.class, TextChannel.class, new Getter<TextChannel, SectionReplyEvent>() {
            @Override
            public TextChannel get(SectionReplyEvent event) {
                return (TextChannel) event.JDAEvent.getChannel();
            }
        }, 0);

        EventValues.registerEventValue(SectionReplyEvent.class, Guild.class, new Getter<Guild, SectionReplyEvent>() {
            @Override
            public Guild get(SectionReplyEvent event) {
                return event.JDAEvent.getGuild();
            }
        }, 0);

        EventValues.registerEventValue(SectionReplyEvent.class, UpdatingMessage.class, new Getter<UpdatingMessage, SectionReplyEvent>() {
            @Override
            public UpdatingMessage get(SectionReplyEvent event) {
                return UpdatingMessage.from(event.JDAEvent.getMessageId());
            }
        }, 0);

        EventValues.registerEventValue(SectionReplyEvent.class, ButtonBuilder.class, new Getter<ButtonBuilder, SectionReplyEvent>() {
            @Override
            public ButtonBuilder get(SectionReplyEvent event) {
                return ButtonBuilder.fromButton(event.JDAEvent.getButton());
            }
        }, 0);
    }

    public static class SectionReplyEvent extends BukkitEvent implements InteractionEvent, Cancellable {

        private final ButtonClickEvent JDAEvent;

        public SectionReplyEvent(ButtonClickEvent event) {
            this.JDAEvent = event;
        }

        @Override
        public GenericInteractionCreateEvent getInteractionEvent() {
            return JDAEvent;
        }

        private boolean cancelled;

        @Override
        public boolean isCancelled() {
            return cancelled;
        }

        @Override
        public void setCancelled(boolean cancel) {
            cancelled = cancel;
        }
    }
}
