package info.itsthesky.DiSky.skript.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.util.SkriptColor;
import ch.njol.util.coll.CollectionUtils;
import info.itsthesky.DiSky.DiSky;
import info.itsthesky.DiSky.tools.Utils;
import info.itsthesky.DiSky.tools.object.RoleBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.awt.*;

@Name("Color of member, embed, role or builder")
@Description("Get or set the color of a role, embed, builder...\nCan only GET the color of a MEMBER!\nYou can use any other color expression (from other addon too) as soon as the object's type return is java.awt.Color")
@Examples("set color of embed to red")
@Since("1.4")
public class ExprColorOf extends SimplePropertyExpression<Object, Object> {

    static {
        register(ExprColorOf.class, Object.class,
                "[discord] [(role|embed|builder|member)] colo[u]r",
                "role/rolebuilder/embed/member"
        );
    }

    @Nullable
    @Override
    public Color convert(Object entity) {
        Color finalColor = null;
        if (entity instanceof EmbedBuilder) finalColor = ((EmbedBuilder) entity).build().getColor();
        if (entity instanceof Role) finalColor = ((Role) entity).getColor();
        if (entity instanceof RoleBuilder) finalColor = ((RoleBuilder) entity).getColor();
        if (entity instanceof Member) finalColor = ((Member) entity).getColor();
        return finalColor;
    }

    @Override
    public Class<? extends Color> getReturnType() {
        return Color.class;
    }

    @Override
    protected String getPropertyName() {
        return "discord color";
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Object.class);
        }
        return CollectionUtils.array();
    }

    @Override
    public void change(Event e, @Nullable Object[] delta, Changer.ChangeMode mode) {
        if (delta == null || delta.length == 0) return;

        Color finalColor = null;

        /*
         else if (delta[0] instanceof ch.njol.skript.util.Color) {

                ch.njol.skript.util.Color tempSkript = (ch.njol.skript.util.Color) delta[0];
                finalColor = new Color(tempSkript.asBukkitColor().getRed(), tempSkript.asBukkitColor().getGreen(), tempSkript.asBukkitColor().getBlue());

            }
         */

        try {
            if (delta[0] instanceof org.bukkit.Color) {

                finalColor = Utils.toJavaColor((org.bukkit.Color) delta[0]);

            } else if (delta[0] instanceof Color) {

                finalColor = (Color) delta[0];

            } else {

                Object c = (DiSky.getSkriptAdapter().colorFromName(delta[0].toString()));
                if (c instanceof SkriptColor) finalColor = Utils.toJavaColor(((SkriptColor) c).asBukkitColor());
                if (c instanceof ch.njol.skript.util.Color) finalColor = Utils.toJavaColor(((ch.njol.skript.util.Color) delta[0]).asBukkitColor());

            }
        } catch (IncompatibleClassChangeError ex) {
            DiSky.getInstance().getLogger().severe("DiSky tried to convert a color from Skript, but it seems you're using an old version of it. Please update to 2.2+!");
            finalColor = Color.RED;
        }

        if (finalColor == null) {
            DiSky.getInstance().getLogger().severe("Cannot parse the color '"+delta[0]+"', which is not from Skript, Bukkit or Java object! (From class: " + delta[0].getClass().getName());
            return;
        }

        if (mode == Changer.ChangeMode.SET) {
            for (Object entity : getExpr().getArray(e)) {
                if (entity instanceof Role) {
                    Role role = (Role) entity;
                    role.getManager().setColor(finalColor).queue();
                    return;
                } else if (entity instanceof EmbedBuilder) {
                    EmbedBuilder embedBuilder = (EmbedBuilder) entity;
                    embedBuilder.setColor(finalColor);
                    return;
                } else if (entity instanceof RoleBuilder) {
                    RoleBuilder roleBuilder = (RoleBuilder) entity;
                    roleBuilder.setColor(finalColor);
                    return;
                }
                DiSky.getInstance().getLogger().severe("Cannot change the discord color of entity '"+entity.getClass().getName()+"', since that's not a Discord entity!");
            }
        }
    }
}