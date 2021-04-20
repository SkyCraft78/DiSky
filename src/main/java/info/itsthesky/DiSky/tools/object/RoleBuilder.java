package info.itsthesky.DiSky.tools.object;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.requests.restaction.RoleAction;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoleBuilder {

    private String name;
    private Color color;
    private boolean mentionable;
    private boolean separate;
    private List<Permission> allow = new ArrayList<>();
    private List<Permission> deny = new ArrayList<>();

    public RoleBuilder() {
        this.name = "default name";
        this.color = new Color(Role.DEFAULT_COLOR_RAW);
        this.mentionable = false;
        this.separate = false;
    }

    public RoleBuilder(Role role) {
        this.name = role.getName();
        this.color = role.getColor();
        this.separate = role.isHoisted();
        this.mentionable = role.isMentionable();
    }

    public Role create(Guild guild) {
        RoleAction manager = guild
                .createRole()
                .setHoisted(this.separate)
                .setColor(this.color)
                .setName(this.name)
                .setMentionable(this.mentionable);
        Role finalRole = manager.complete();

        for (Permission perm : this.allow) {
            finalRole
                    .getManager()
                    .givePermissions(perm)
                    .queue();
        }
        for (Permission perm : this.deny) {
            finalRole
                    .getManager()
                    .revokePermissions(perm)
                    .queue();
        }

        return finalRole;
    }

    public String getName() {
        return name;
    }
    public Color getColor() {
        return color;
    }
    public boolean isSeparate() {
        return separate;
    }
    public boolean isMentionable() {
        return mentionable;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    public void setMentionable(boolean mentionable) {
        this.mentionable = mentionable;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSeparate(boolean separate) {
        this.separate = separate;
    }

    public void addDeny(Permission... permission) {
        deny.addAll(Arrays.asList(permission));
    }
    public void addAllow(Permission... permission) {
        allow.addAll(Arrays.asList(permission));
    }
}
