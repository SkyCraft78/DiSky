package info.itsthesky.DiSky.skript.events.bukkit;

import info.itsthesky.DiSky.DiSky;
import info.itsthesky.DiSky.managers.BotManager;
import info.itsthesky.DiSky.skript.events.skript.*;
import info.itsthesky.DiSky.tools.object.command.DiscordCommand;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.event.Event;

public class JDAListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        /* Message receive */
        Event event = null;
        if (!e.getAuthor().isBot()) {
            event = new EventMessageReceive(
                    e.getTextChannel(),
                    e.getAuthor(),
                    e.getMessage(),
                    e.getGuild()
            );
        } else {
            event = new EventBotMessageReceive(
                    e.getTextChannel(),
                    e.getAuthor(),
                    e.getMessage(),
                    e.getGuild()
            );
        }
        // ***
        /* Command event */
        DiscordCommand discordCommand = new DiscordCommand(e.getMessage().getContentRaw());
        EventCommand command = new EventCommand(
                e.getTextChannel(),
                e.getAuthor(),
                e.getMessage(),
                e.getGuild(),
                discordCommand
        );
        DiSky.getInstance().getServer().getPluginManager().callEvent(event);
        DiSky.getInstance().getServer().getPluginManager().callEvent(command);
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        Event event;
        if (e.getUser().isBot() && e.getUser().getId().equals(BotManager.getFirstBot().getSelfUser().getId())) {
           event = new EventBotJoin(
                    BotManager.getFirstBotName(),
                    e.getGuild()
            );
        } else {
            event = new EventMemberJoin(
                    e.getUser(),
                    e.getGuild()
            );
        }
        DiSky.getInstance().getServer().getPluginManager().callEvent(event);
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent e) {
        Event event = new EventMemberJoin(
                e.getUser(),
                e.getGuild()
        );
        DiSky.getInstance().getServer().getPluginManager().callEvent(event);
    }

}