package info.itsthesky.DiSky.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Variable;
import ch.njol.util.Kleenean;
import info.itsthesky.DiSky.tools.DiSkyErrorHandler;
import info.itsthesky.DiSky.tools.Utils;
import info.itsthesky.DiSky.tools.object.messages.Channel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Clone Discord Entity")
@Description("Clone an existing discord entity (channel, category or role) in another guild.")
@Examples("discord command clone <textchannel>:\n" +
        "\tprefixes: !\n" +
        "\tpermissions: administrator\n" +
        "\tpermission message: :x: You don't have enough permission!\n" +
        "\ttrigger:\n" +
        "\t\tclone arg-1 and store it in {_c}\n" +
        "\t\treply with \"Channel cloned: %mention tag of {_c}%\"")
@Since("1.5.3")
public class EffCloneEntity extends Effect {

    static {
        Skript.registerEffect(EffCloneEntity.class,
                "["+ Utils.getPrefixName() +"] clone [discord] [entity] %role/textchannel/channel/category% [in [the] [guild] %-guild%] [and store (it|the entity) in %-object%]");
    }

    private Expression<Object> exprEntity;
    private Expression<Guild> exprGuild;
    private Expression<Object> exprVar;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        exprEntity = (Expression<Object>) exprs[0];
        if (exprs.length == 1) exprGuild = (Expression<Guild>) exprs[1];
        if (exprs.length == 2) exprVar = (Expression<Object>) exprs[2];
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            Object entity = exprEntity.getSingle(e);
            if (entity == null) return;
            boolean guildSpecified = exprGuild != null;
            Guild guild = exprGuild == null ? null : exprGuild.getSingle(e);
            Object newEntity = null;

            if (entity instanceof Role) {
                Role role = (Role) entity;
                if (guildSpecified) {
                    newEntity = role.createCopy(guild).complete();
                } else {
                    newEntity = role.createCopy().complete();
                }
            } else if (entity instanceof Category) {
                Category category = (Category) entity;
                if (guildSpecified) {
                    newEntity = category.createCopy(guild).complete();
                } else {
                    newEntity = category.createCopy().complete();
                }
            } else if (entity instanceof Channel || entity instanceof TextChannel) {
                TextChannel channel = Utils.checkChannel(entity);
                if (channel == null) return;
                if (guildSpecified) {
                    newEntity = channel.createCopy(guild).complete();
                } else {
                    newEntity = channel.createCopy().complete();
                }
            }

            if (exprVar == null) return;
            if (!exprVar.getClass().getName().equalsIgnoreCase("ch.njol.skript.lang.Variable")) return;
            Variable var = (Variable) exprVar;
            Utils.setSkriptVariable(var, newEntity, e);
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "clone discord entity " + exprEntity.toString(e, debug);
    }

}
