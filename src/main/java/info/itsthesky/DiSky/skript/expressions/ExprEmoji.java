package info.itsthesky.disky.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.Emote;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

@Name("Reaction / Emoji")
@Description("Get an emoji object using string. Could be an emoji ID (custom), unicode (default) or name (custom and default).")
@Since("1.8")
public class ExprEmoji extends SimpleExpression<Emote> {
    static {
        Skript.registerExpression(ExprEmoji.class, Emote.class, ExpressionType.SIMPLE,
                "(emoji|emote|reaction)[s] %strings% [(from|in) %-guild%]");
    }

    private Expression<String> name;
    private Expression<Guild> guild;

    @Override
    protected Emote[] get(Event e) {
        String[] emote = name.getAll(e);
        Guild guild = this.guild == null ? null : this.guild.getSingle(e);
        if (emote.length == 0) return new Emote[0];
        List<Emote> emojis = new ArrayList<>();
        for (String input : emote) {
            emojis.add(Utils.unicodeFrom(input, guild));
        }
        return emojis.toArray(new Emote[0]);
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Emote> getReturnType() {
        return Emote.class;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "emoji named " + name.toString(e, debug) + (guild == null ? "" : " from " + guild.toString(e, debug));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        name = (Expression<String>) exprs[0];
        guild = (Expression<Guild>) exprs[1];
        return true;
    }
}