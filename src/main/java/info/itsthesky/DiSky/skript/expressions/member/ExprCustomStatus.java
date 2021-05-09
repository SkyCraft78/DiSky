package info.itsthesky.disky.skript.expressions.member;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Custom Status")
@Description("Get a member custom status.")
@Examples("if custom status of event-member contain \"https://\" # For anti-URL")
@Since("1.6")
public class ExprCustomStatus extends SimplePropertyExpression<Member, String> {

    static {
        register(ExprCustomStatus.class, String.class,
                "[discord] custom status",
                "member"
        );
    }

    @Nullable
    @Override
    public String convert(Member entity) {
        if (entity instanceof Member) {
            return ((Member) entity).getActivities().stream().filter(t -> {
                return t.getType().equals(Activity.ActivityType.CUSTOM_STATUS);
            }).findFirst().get().getName();
        }
        return null;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    protected String getPropertyName() {
        return "online status";
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