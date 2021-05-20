package info.itsthesky.disky.skript.commands;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.log.ParseLogHandler;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.skript.util.Timespan;
import ch.njol.skript.util.Utils;
import info.itsthesky.disky.managers.BotManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CommandObject {

    private final String name;
    private final List<String> aliases;
    private final List<String> roles;
    private final List<String> perms;
    private final List<ChannelType> executableIn;
    private final List<Expression<String>> prefixes;
    private final String description;
    private final String usage;
    private final String category;
    private final String pattern;
    private final String permMessage;
    private final Timespan cooldownGuild;
    private final String cooldownMessage;
    private final List<String> bots;

    private Trigger trigger;

    private List<Argument<?>> arguments;

    public CommandObject(File script, String name, String pattern, List<Argument<?>> arguments, List<Expression<String>> prefixes,
                         List<String> aliases, String description, String usage, List<String> roles,
                         List<ChannelType> executableIn, List<String> bots, List<TriggerItem> items,
                         List<String> perms, String permMessage, String category,
                         Timespan cooldownGuild, String cooldownMessage) {
        this.name = name;
        if (aliases != null) {
            aliases.removeIf(alias -> alias.equalsIgnoreCase(name));
        }
        this.cooldownGuild = cooldownGuild;
        this.cooldownMessage = cooldownMessage;
        this.aliases = aliases;
        this.roles = roles;
        this.executableIn = executableIn;
        this.description = Utils.replaceEnglishChatStyles(description);
        this.usage = Utils.replaceEnglishChatStyles(usage);
        this.pattern = pattern;
        this.prefixes = prefixes;
        this.bots = bots;
        this.perms = perms;
        this.arguments = arguments;
        this.permMessage = permMessage;
        this.category = category;

        trigger = new Trigger(script, "discord command " + name, new SimpleEvent(), items);

    }

    public boolean execute(CommandEvent event) {
        ParseLogHandler log = SkriptLogger.startParseLogHandler();

        try {

            boolean ok = CommandFactory.getInstance().parseArguments(event.getArguments(), this, event);
            if (!ok) {
                return false;
            }
            if (!this.getExecutableIn().contains(event.getMessageChannel().getType())) {
                return false;
            }
            if (this.getRoles() != null && event.getMember() != null) {
                if (event.getMember().getRoles().stream().noneMatch(r -> this.getRoles().contains(r.getName()))) {
                    return false;
                }
            }
            try {
                if (bots != null && !bots.contains(BotManager.getNameByJDA(event.getBot()))) {
                    return false;
                }
            } catch (NullPointerException ignored) {}

            List<Permission> permissions = info.itsthesky.disky.tools.Utils.convertPerms(perms.toArray(new String[0]));
            if (event.getGuild() != null) {
                if (!event.getMember().hasPermission(permissions)) {
                    if (permMessage != null) {
                        event.getMessageChannel().sendMessage(permMessage).queue();
                    }
                    return false;
                }
            }

            // again, bukkit apis are mostly sync so run it on the main thread
            info.itsthesky.disky.tools.Utils.sync(() -> trigger.execute(event));

        } finally {
            log.stop();
        }

        return true;
    }

    public List<Argument<?>> getArguments() {
        return arguments;
    }

    public String getName() {
        return name;
    }

    public String getPattern() {
        return pattern;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public List<Expression<String>> getPrefixes() {
        return prefixes;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getPermMessage() {
        return permMessage;
    }

    public String getUsage() {
        return usage;
    }

    public List<String> getUsableAliases() {
        List<String> usableAliases = new ArrayList<>();
        usableAliases.add(getName());
        if (getAliases() != null) {
            usableAliases.addAll(getAliases());
        }
        return usableAliases;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public List<ChannelType> getExecutableIn() {
        return executableIn;
    }

    public List<String> getRoles() {
        return roles;
    }
    public List<String> getPerms() {
        return perms;
    }

    public File getScript() {
        return trigger.getScript();
    }

}