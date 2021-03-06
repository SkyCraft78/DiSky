package info.itsthesky.disky.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import info.itsthesky.disky.tools.async.AsyncEffect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.Utils;
import info.itsthesky.disky.tools.object.RoleBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;

@Name("Allow / Deny permission")
@Description("Allow or deny a discord permission to a role or a user in a channel.")
@Examples("discord command setupmute:\n" +
        "\tpermissions: administrator\n" +
        "\tpermission message: **:x: You don't have the permission!**\n" +
        "\tprefixes: !\n" +
        "\ttrigger:\n" +
        "\t\tmake new role:\n" +
        "\t\t\tset discord name of role builder to \"Muted\"\n" +
        "\t\t\tset color of role builder to black\n" +
        "\t\t\tloop text channels of event-guild:\n" +
        "\t\t\t\tdeny permission (message write) to role builer in loop-channel\n" +
        "\t\tcreate last role builder in event-guild and store it in {_role}\n" +
        "\t\treply with \"**Mute role created: %mention tag of {_role}%!**\"")
@Since("1.4")
public class EffManagePermission extends AsyncEffect {

    static {
        Skript.registerEffect(EffManagePermission.class,
                "["+ Utils.getPrefixName() +"] allow [discord] [perm[ission]] %permissions% to %member/role/rolebuilder% in [the] [channel] %category/channel/textchannel/voicechannel%",
                "["+ Utils.getPrefixName() +"] deny [discord] [perm[ission]] %permissions% to %member/role/rolebuilder% in [the] [channel] %category/channel/textchannel/voicechannel%"
        );
    }

    private Expression<Permission> exprPerm;
    private Expression<Object> exprTarget;
    private Expression<Object> exprEntity;
    private boolean isAllow;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprPerm = (Expression<Permission>) exprs[0];
        exprTarget = (Expression<Object>) exprs[1];
        exprEntity = (Expression<Object>) exprs[2];
        isAllow = matchedPattern == 0;
        return true;
    }

    @Override
    protected void execute(Event e) {
        Permission[] perm = exprPerm.getArray(e);
        Object target = exprTarget.getSingle(e);
        Object entity = exprEntity.getSingle(e);
        if (perm.length == 0 || target == null || entity == null) return;

        GuildChannel channel = (GuildChannel) entity;

        if (target instanceof Role) {
            Role role = (Role) target;
            if (isAllow) {
                channel.upsertPermissionOverride(role).grant(perm).queue(null, DiSkyErrorHandler::logException);
            } else {
                channel.upsertPermissionOverride(role).deny(perm).queue(null, DiSkyErrorHandler::logException);
            }
        } if (target instanceof RoleBuilder) {
            RoleBuilder role = (RoleBuilder) target;
            if (isAllow) {
                role.addAllow(perm);
            } else {
                role.addDeny(perm);
            }
        } else if (target instanceof Member) {
            Member member = (Member) target;
            if (isAllow) {
                channel.upsertPermissionOverride(member).grant(perm).queue(null, DiSkyErrorHandler::logException);
            } else {
                channel.upsertPermissionOverride(member).deny(perm).queue(null, DiSkyErrorHandler::logException);
            }
        }

    }

    @Override
    public String toString(Event e, boolean debug) {
        if (isAllow) {
            return "allow permission " + exprPerm.toString(e, debug) + " to role or user " + exprTarget.toString(e,debug);
        } else {
            return "deny permission " + exprPerm.toString(e, debug) + " to role or user " + exprTarget.toString(e,debug);
        }
    }

}
