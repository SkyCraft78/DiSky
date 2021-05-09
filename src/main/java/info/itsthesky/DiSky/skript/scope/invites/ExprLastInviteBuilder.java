package info.itsthesky.disky.skript.scope.invites;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.object.InviteBuilder;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Last Invite Builder")
@Description("Return the last used invite builder.")
@Since("1.7")
public class ExprLastInviteBuilder extends SimpleExpression<InviteBuilder> {

    static {
        Skript.registerExpression(ExprLastInviteBuilder.class, InviteBuilder.class, ExpressionType.SIMPLE,
                "[the] [last] [(generated|created)] invite [builder]"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Nullable
    @Override
    protected InviteBuilder[] get(Event e) {
        return new InviteBuilder[]{ScopeInvite.lastBuilder};
    }

    @Override
    public Class<? extends InviteBuilder> getReturnType() {
        return InviteBuilder.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the last invite builder";
    }
}
