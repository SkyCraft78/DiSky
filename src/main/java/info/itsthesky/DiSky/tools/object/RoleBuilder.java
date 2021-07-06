package info.itsthesky.disky.tools.object;

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
    private int position;
    private List<Permission> allow = new ArrayList<>();
    private List<Permission> deny = new ArrayList<>();

    public RoleBuilder() {
        this.name = "default name";
        this.color = new Color(Role.DEFAULT_COLOR_RAW);
        this.mentionable = false;
        this.separate = false;
        this.position = 0;
    }

    public RoleBuilder(Role role) {
        this.name = role.getName();
        this.color = role.getColor();
        this.separate = role.isHoisted();
        this.mentionable = role.isMentionable();
    }

    public RoleAction create(Guild guild) {
        return guild
                .createRole()
                .setHoisted(this.separate)
                .setColor(this.color)
                .setName(this.name)
                .setMentionable(this.mentionable)
                .setPermissions(this.allow);
    }

    public String getName() {
        return name;
    }
    public Color getColor() {
        return color;
    }
    public int getPosition() {
        return position;
    }
    public boolean isSeparate() {
        return separate;
    }
    public boolean isMentionable() {
        return mentionable;
    }

    public void setPosition(int position) {
        this.position = position;
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
