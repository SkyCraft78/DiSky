package info.itsthesky.disky.skript.commands;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.Utils;
import ch.njol.util.Kleenean;
import ch.njol.util.StringUtils;
import info.itsthesky.disky.skript.events.skript.EventButtonClick;
import info.itsthesky.disky.skript.events.skript.EventButtonsSection;
import info.itsthesky.disky.skript.events.skript.EventReactSection;
import info.itsthesky.disky.tools.StaticData;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Peter Güttinger
 * edited minorly for Vixio by Blitz, and then edited by ItstheSky for DiSky implementation
 */
@Name("Last Argument")
@Description("Get an argument in a discord command builder.")

public class ExprArgument extends SimpleExpression<Object> {
    static {
        Skript.registerExpression(ExprArgument.class, Object.class, ExpressionType.SIMPLE,
                "[][the] last arg[ument][s]",
                "[][the] arg[ument][s](-| )<(\\d+)>", "[][the] <(\\d*1)st|(\\d*2)nd|(\\d*3)rd|(\\d*[4-90])th> arg[ument][s]",
                "[][the] arg[ument][s]",
                "[][the] %*classinfo%( |-)arg[ument][( |-)<\\d+>]", "[][the] arg[ument]( |-)%*classinfo%[( |-)<\\d+>]");
    }

    @SuppressWarnings("null")
    private Argument<?> arg;

    @Override
    public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parser) {
        if (!ScriptLoader.isCurrentEvent(CommandEvent.class) &&
                !ScriptLoader.isCurrentEvent(EventReactSection.class) &&
                        !ScriptLoader.isCurrentEvent(EventButtonsSection.class)
        )
            return false;

        List<Argument<?>> currentArguments = CommandFactory.getInstance().currentArguments;
        final List<Argument<?>> lastArguments = StaticData.lastArguments;
        if (currentArguments == null) {
            if (lastArguments.isEmpty()) {
                Skript.error("The expression 'argument' can only be used within a command", ErrorQuality.SEMANTIC_ERROR);
                return false;
            } else {
                currentArguments = lastArguments;
                StaticData.lastArguments = new ArrayList<>();
            }
        }
        if (currentArguments.size() == 0) {
            Skript.error("This command doesn't have any arguments", ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        Argument<?> arg = null;
        switch (matchedPattern) {
            case 0:
                arg = currentArguments.get(currentArguments.size() - 1);
                break;
            case 1:
            case 2:
                @SuppressWarnings("null") final int i = Utils.parseInt(parser.regexes.get(0).group(1));
                if (i > currentArguments.size()) {
                    Skript.error("The command doesn't have a " + StringUtils.fancyOrderNumber(i) + " argument", ErrorQuality.SEMANTIC_ERROR);
                    return false;
                }
                arg = currentArguments.get(i - 1);
                break;
            case 3:
                if (currentArguments.size() == 1) {
                    arg = currentArguments.get(0);
                } else {
                    Skript.error("'argument(s)' cannot be used if the command has multiple arguments. Use 'argument 1', 'argument 2', etc. instead", ErrorQuality.SEMANTIC_ERROR);
                    return false;
                }
                break;
            case 4:
            case 5:
                @SuppressWarnings("unchecked") final ClassInfo<?> c = ((Literal<ClassInfo<?>>) exprs[0]).getSingle();
                @SuppressWarnings("null") final int num = parser.regexes.size() > 0 ? Utils.parseInt(parser.regexes.get(0).group()) : -1;
                int j = 1;
                for (final Argument<?> a : currentArguments) {
                    if (!c.getC().isAssignableFrom(a.getType()))
                        continue;
                    if (arg != null) {
                        Skript.error("There are multiple " + c + " arguments in this command", ErrorQuality.SEMANTIC_ERROR);
                        return false;
                    }
                    if (j < num) {
                        j++;
                        continue;
                    }
                    arg = a;
                    if (j == num)
                        break;
                }
                if (arg == null) {
                    j--;
                    if (num == -1 || j == 0)
                        Skript.error("There is no " + c + " argument in this command", ErrorQuality.SEMANTIC_ERROR);
                    else if (j == 1)
                        Skript.error("There is only one " + c + " argument in this command", ErrorQuality.SEMANTIC_ERROR);
                    else
                        Skript.error("There are only " + j + " " + c + " arguments in this command", ErrorQuality.SEMANTIC_ERROR);
                    return false;
                }
                break;
            default:
                assert false : matchedPattern;
                return false;
        }
        assert arg != null;
        this.arg = arg;
        return true;
    }

    @Override
    @Nullable
    protected Object[] get(final Event e) {
        return arg.getCurrent(e);
    }

    @Override
    public Class<? extends Object> getReturnType() {
        return arg.getType();
    }

    @Override
    public String toString(final @Nullable Event e, final boolean debug) {
        if (e == null)
            return "the " + StringUtils.fancyOrderNumber(arg.getIndex() + 1) + " argument";
        return Classes.getDebugMessage(getArray(e));
    }

    @Override
    public boolean isSingle() {
        return arg.isSingle();
    }

    @Override
    public boolean isLoopOf(final String s) {
        return s.equalsIgnoreCase("argument");
    }

}