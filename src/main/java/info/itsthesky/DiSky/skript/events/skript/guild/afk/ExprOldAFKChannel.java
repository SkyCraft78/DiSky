package info.itsthesky.disky.skript.events.skript.guild.afk;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Old AFK Channel")
@Description("Get the old afk channel in a afk channel update event")
@Since("1.9")
public class ExprOldAFKChannel extends SimpleExpression<VoiceChannel> {

    public static VoiceChannel oldAFKChannel;

    static {
        Skript.registerExpression(ExprOldAFKChannel.class, VoiceChannel.class, ExpressionType.SIMPLE,
                "[the] new afk [voice] channel"
        );
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Nullable
    @Override
    protected VoiceChannel[] get(Event e) { return new VoiceChannel[]{oldAFKChannel}; }
    @Override
    public Class<? extends VoiceChannel> getReturnType() { return VoiceChannel.class; }
    @Override
    public boolean isSingle() { return true; }
    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "the old afk voice channel";
    }
}
