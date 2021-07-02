package info.itsthesky.disky.skript.component;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.Emote;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenuInteraction;
import org.bukkit.event.Event;

@Name("New Selection Menu Choice")
@Description("Create a new selection menu choice, with an ID, a name, an optional description and an optional emoji.")
@Since("2.0")
public class ExprNewChoice extends SimpleExpression<SelectOption> {
    
    static {
        Skript.registerExpression(ExprNewChoice.class, SelectOption.class, ExpressionType.SIMPLE,
                "[a] new [default] choice with value %string% (with name|named) %string%[,] [with (desc|description) %-string%][,] [with [emoji] %-emote%]");
    }

    private Expression<String> exprValue, exprName, exprDesc;
    private Expression<Emote> exprEmote;
    private boolean isDefault;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprValue = (Expression<String>) exprs[0];
        exprName = (Expression<String>) exprs[1];
        exprDesc = (Expression<String>) exprs[2];
        exprEmote = (Expression<Emote>) exprs[3];
        isDefault = parseResult.expr.contains("new default");
        return true;
    }

    @Override
    protected SelectOption[] get(Event e) {
        String value = exprValue.getSingle(e);
        String name = exprName.getSingle(e);
        String desc = Utils.verifyVar(e, exprDesc);
        Emote emote = Utils.verifyVar(e, exprEmote);
        if (value == null || name == null) return new SelectOption[0];

        SelectOption option;

        option = emote == null ? SelectOption.of(name, value).withDefault(isDefault) : SelectOption.of(name, value).withDefault(isDefault).withEmoji(Utils.convert(emote));
        if (desc != null)
            option = option.withDescription(desc);

        return new SelectOption[] {option};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends SelectOption> getReturnType() {
        return SelectOption.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "new " + (isDefault ? "default " : "") + "choice with value " + exprValue.toString(e, debug) + " with name " + exprName.toString(e, debug) + (exprEmote == null ? "" : " with emote " + exprEmote.toString(e, debug));
    }
}