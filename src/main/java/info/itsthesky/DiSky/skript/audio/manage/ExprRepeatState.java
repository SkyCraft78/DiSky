package info.itsthesky.disky.skript.audio.manage;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky.managers.music.AudioUtils;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Repeating State of Guild")
@Description("Get ot set the repeating state of a guild. If it's true, the track will be repeating every time.")
@Examples("discord command repeat [<boolean>]:\n" +
        "\tprefixes: *\n" +
        "\ttrigger:\n" +
        "\t\tif arg-1 is not set:\n" +
        "\t\t\treply with \":x: **You __must__ set the repeating state to `true` or `false`!**\"\n" +
        "\t\t\tstop\n" +
        "\t\tset repeating state of event-guild to arg-1\n" +
        "\t\tif bot is repeating in event-guild:\n" +
        "\t\t\tset {_name} to \"enable\"\n" +
        "\t\telse:\n" +
        "\t\t\tset {_name} to \"disable\"\n" +
        "\t\treply with \":v: **You have `%{_name}%` the repeating state of this guild!**\"")
@Since("1.11")
public class ExprRepeatState extends SimplePropertyExpression<Guild, Boolean> {

    static {
        register(ExprRepeatState.class, Boolean.class,
                "[discord] repeating [state]",
                "guild"
        );
    }

    @Nullable
    @Override
    public Boolean convert(Guild guild) {
        return AudioUtils.getGuildAudioPlayer(guild).getScheduler().isRepeated();
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    protected String getPropertyName() {
        return "repeating state";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Boolean.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (delta == null || delta.length == 0) return;
        Boolean state = Boolean.parseBoolean(delta[0].toString());
        if (mode == Changer.ChangeMode.SET) {
            for (Guild guild : getExpr().getArray(e)) {
                 AudioUtils.getGuildAudioPlayer(guild).getScheduler().setRepeated(state);
            }
        }
    }
}