package info.itsthesky.disky.skript.scope.voicechannels;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.object.VoiceChannelBuilder;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Last Voice Channel Builder")
@Description("Return the last used voice channels builder.")
@Since("1.6")
public class ExprLastVoiceBuilder extends SimpleExpression<VoiceChannelBuilder> {

    static {
        Skript.registerExpression(ExprLastVoiceBuilder.class, VoiceChannelBuilder.class, ExpressionType.SIMPLE,
                "[the] [last] [(generated|created)] voice channel [builder]"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Nullable
    @Override
    protected VoiceChannelBuilder[] get(Event e) {
        return new VoiceChannelBuilder[]{ScopeVoiceChannel.lastBuilder};
    }

    @Override
    public Class<? extends VoiceChannelBuilder> getReturnType() {
        return VoiceChannelBuilder.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the last voice channel builder";
    }
}
