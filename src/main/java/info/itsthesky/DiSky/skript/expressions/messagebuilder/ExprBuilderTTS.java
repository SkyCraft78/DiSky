package info.itsthesky.disky.skript.expressions.messagebuilder;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import net.dv8tion.jda.api.MessageBuilder;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Message Builder TTS")
@Description("change the TTS (Text To Speech) state of a message builder")
@Examples("set tts state of {_m} to true")
@Since("1.6")
public class ExprBuilderTTS extends SimplePropertyExpression<MessageBuilder, Boolean> {

    static {
        register(ExprBuilderTTS.class, Boolean.class,
                "[discord] tts [state]",
                "messagebuilder"
        );
    }

    @Nullable
    @Override
    public Boolean convert(MessageBuilder entity) {
        return entity.build().isTTS();
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    protected String getPropertyName() {
        return "tts state";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode.equals(Changer.ChangeMode.SET)) {
            return CollectionUtils.array(Boolean.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (delta == null || delta[0] == null || !(delta[0] instanceof Boolean) || !(mode.equals(Changer.ChangeMode.SET))) return;
        boolean value = (Boolean) delta[0];
        for (MessageBuilder builder : getExpr().getAll(e)) builder.setTTS(value);
    }
}