package info.itsthesky.disky.skript.expressions.member;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Presence of member / bot")
@Description("Get member discord presence, and set it for bot.")
@Examples("set presence of bot \"name\" to listening \"Hello world!\"")
@Since("1.12")
public class ExprMemberPresence extends SimplePropertyExpression<Object, Activity> {

    static {
        register(ExprMemberPresence.class, Activity.class,
                "[discord] presence",
                "member/bot"
        );
    }

    @Nullable
    @Override
    public Activity convert(Object entity) {
        if (entity instanceof Member) {
            Member member = (Member) entity;
            return member.getActivities().get(0);
        } else if (entity instanceof JDA) {
            return ((JDA) entity).getPresence().getActivity();
        }
        return null;
    }

    @Override
    public Class<? extends Activity> getReturnType() {
        return Activity.class;
    }

    @Override
    protected String getPropertyName() {
        return "discord presence";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Activity.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (delta == null || delta.length == 0 || delta[0] == null) return;
        Activity activity = (Activity) delta[0];
        if (mode == Changer.ChangeMode.SET) {
            for (Object entity : getExpr().getArray(e)) {
                if (!(entity instanceof JDA)) return;
                JDA bot = (JDA) entity;
                bot.getPresence().setActivity(activity);
            }
        }
    }
}