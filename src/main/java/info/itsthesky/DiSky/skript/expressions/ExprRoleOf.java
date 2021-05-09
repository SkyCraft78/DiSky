package info.itsthesky.disky.skript.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.disky.tools.MultiplyPropertyExpression;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Role of User")
@Description("Add or remove roles from an user.")
@Examples({"remove role with id \"818182471203684407\" from roles of event-user",
        "add role with id \"818182471203684407\" to roles of event-user"})
@Since("1.0")
public class ExprRoleOf extends MultiplyPropertyExpression<Member, Object> {

    static {
        register(ExprRoleOf.class, Object.class,
                "[discord] [member] role[s]",
                "member"
        );
    }

    @Nullable
    @Override
    public Role[] convert(Member user) {
        return user.getRoles().toArray(new Role[0]);
    }

    @Override
    public Class<? extends Role> getReturnType() {
        return Role.class;
    }

    @Override
    protected String getPropertyName() {
        return "member roles";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.ADD || mode == Changer.ChangeMode.REMOVE) {
            return CollectionUtils.array(Object[].class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (delta == null || delta[0] == null || !(delta[0] instanceof Role)) return;
        switch (mode) {
            case ADD:
                for (Member member : getExpr().getArray(e)) {
                    member.getGuild().addRoleToMember(member, (Role) delta[0]).complete();
                }
                break;
            case REMOVE:
                for (Member member : getExpr().getArray(e)) {
                    member.getGuild().removeRoleFromMember(member, (Role) delta[0]).complete();
                }
                break;
            default:
                return;
        }
    }
}