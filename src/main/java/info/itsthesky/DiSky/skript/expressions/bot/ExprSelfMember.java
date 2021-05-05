package info.itsthesky.DiSky.skript.expressions.bot;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.DiSky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Bot Self Member")
@Description("Get the self member (member type) of a bot (JDA type)")
@Since("1.11")
public class ExprSelfMember extends SimplePropertyExpression<JDA, Member> {

    static {
        register(ExprSelfMember.class, Member.class,
                "[bot] self member",
                "string/bot"
        );
    }

    @Nullable
    @Override
    public Member convert(JDA entity) {
        return Utils.searchMember(entity, entity.getSelfUser().getId());
    }

    @Override
    public Class<? extends Member> getReturnType() {
        return Member.class;
    }

    @Override
    protected String getPropertyName() {
        return "self member";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {

    }
}