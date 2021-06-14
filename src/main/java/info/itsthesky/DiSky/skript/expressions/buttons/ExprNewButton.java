package info.itsthesky.disky.skript.expressions.buttons;

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
import info.itsthesky.disky.tools.object.ButtonBuilder;
import info.itsthesky.disky.tools.object.ButtonBuilder;
import info.itsthesky.disky.tools.object.Emote;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.internal.interactions.ButtonImpl;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

@Name("New Button")
@Description("Create a new custom button with a style, content, link state, enable state and emoji. The emoji MUST be a UNICODE character.")
@Examples("discord command buttons:\n" +
        "\tprefixes: !\n" +
        "\ttrigger:\n" +
        "\t\treply with \"*buttons ...*\" and store it in {_msg}\n" +
        "\t\tset {_row} to new buttons row\n" +
        "\t\tadd new link button with url \"http://disky.itsthesky.info/\" with style danger with content \"DiSky Website\" with emoji \"\uD83D\uDD17\" to row {_row}\n" +
        "\t\tadd new button with url \"custom-id\" with style danger with content \"Dangerous!\" with emoji \"\uD83D\uDD36\" to row {_row}\n" +
        "\t\tadd new button with url \"custom-id2\" with style success with content \"Green :D\" with emoji \"\uD83D\uDCD7\" to row {_row}\n" +
        "\t\tadd new disabled button with url \"custom-id3\" with style secondary with content \"I'm disabled\" with emoji \"\uD83D\uDE2D\" to row {_row}\n" +
        "\t\tadd row {_row} to message {_msg}")
@Since("1.13")
public class ExprNewButton extends SimpleExpression<ButtonBuilder> {
    
    static {
        Skript.registerExpression(ExprNewButton.class, ButtonBuilder.class, ExpressionType.SIMPLE,
                "[a] new [(enabled|disabled)] [link] button [with (id|url)] %string%[,] (with style|styled) %buttonstyle%[,] [with (content|text) %-string%][,] [with [emoji] %-emote%]");
    }

    private Expression<String> exprIdOrURL;
    private Expression<ButtonStyle> exprStyle;
    private Expression<String> exprContent;
    private Expression<Emote> exprEmoji;
    private boolean isEnabled;
    private boolean isLink;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprIdOrURL = (Expression<String>) exprs[0];
        exprStyle = (Expression<ButtonStyle>) exprs[1];
        exprContent = (Expression<String>) exprs[2];
        exprEmoji = (Expression<Emote>) exprs[3];
        isEnabled = !parseResult.expr.contains("new disabled");
        isLink = parseResult.expr.contains("new link") ||
                parseResult.expr.contains("new enabled link") ||
                parseResult.expr.contains("new disabled link");
        return true;
    }

    @Override
    protected ButtonBuilder[] get(Event e) {
        String idOrURL = exprIdOrURL.getSingle(e);
        ButtonStyle style = exprStyle.getSingle(e);

        String content = exprContent == null ? null : exprContent.getSingle(e);
        Emote emoji = exprEmoji == null ? null : exprEmoji.getSingle(e);

        if (style == null || idOrURL == null) return new ButtonBuilder[0];
        if (emoji == null && content == null) return new ButtonBuilder[0];

        if (isLink)
            style = ButtonStyle.LINK;

        Button button = (content == null ? Button.of(style, idOrURL, "") : Button.of(style, idOrURL, content));

        if (emoji != null && content != null)
            button = Button.of(style, idOrURL, content).withEmoji(emoji.isEmote() ? Emoji.fromEmote(emoji.getEmote()) : Emoji.fromUnicode(emoji.getName()));

        if (!isEnabled)
            button = button.asDisabled();

        return new ButtonBuilder[] {ButtonBuilder.fromButton(button)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends ButtonBuilder> getReturnType() {
        return ButtonBuilder.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "new button with id or url " + exprIdOrURL.toString(e, debug);
    }
}