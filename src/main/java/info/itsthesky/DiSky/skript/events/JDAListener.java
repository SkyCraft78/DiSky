package info.itsthesky.DiSky.skript.events;

import info.itsthesky.DiSky.DiSky;
import info.itsthesky.DiSky.skript.events.skript.audio.EventVoiceJoin;
import info.itsthesky.DiSky.skript.events.skript.audio.EventVoiceLeave;
import info.itsthesky.DiSky.skript.events.skript.audio.EventVoiceMove;
import info.itsthesky.DiSky.skript.events.skript.guild.EventGuildBan;
import info.itsthesky.DiSky.skript.events.skript.guild.EventGuildUnban;
import info.itsthesky.DiSky.skript.events.skript.members.EventMemberBoost;
import info.itsthesky.DiSky.skript.events.skript.members.EventMemberLeave;
import info.itsthesky.DiSky.skript.events.skript.messages.EventMessageDelete;
import info.itsthesky.DiSky.skript.events.skript.messages.EventMessageReceive;
import info.itsthesky.DiSky.skript.events.skript.messages.EventPrivateMessage;
import info.itsthesky.DiSky.skript.events.skript.nickname.EventNickChange;
import info.itsthesky.DiSky.skript.events.skript.reaction.EventReactionAdd;
import info.itsthesky.DiSky.skript.events.skript.reaction.EventReactionRemove;
import info.itsthesky.DiSky.skript.events.skript.role.EventRoleAdd;
import info.itsthesky.DiSky.skript.events.skript.role.EventRoleCreate;
import info.itsthesky.DiSky.skript.events.skript.role.EventRoleDelete;
import info.itsthesky.DiSky.skript.events.skript.role.EventRoleRemove;
import info.itsthesky.DiSky.skript.events.skript.slashcommand.EventSlashCommand;
import info.itsthesky.DiSky.tools.Utils;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class JDAListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        List<Event> event = new ArrayList<>();

        if (e.isFromGuild()) {
            /* Message receive */
            event.add(new EventMessageReceive(e));
        } else {
            if (e.getAuthor().isBot()) return;
            event.add(new EventPrivateMessage(e));
        }
        event.forEach((event1) -> {
            Utils.sync(() -> DiSky.getInstance().getServer().getPluginManager().callEvent(event1));
        });
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent e) {
        Utils.sync(() -> DiSky.getInstance().getServer().getPluginManager().callEvent(new EventSlashCommand(e)));
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent e) {
        Event event = new EventMemberLeave(
                e.getMember(),
                e.getGuild()
        );
        Utils.sync(() -> DiSky.getInstance().getServer().getPluginManager().callEvent(event));
    }

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent e) {
        Utils.sync(() -> DiSky.getInstance().getServer().getPluginManager().callEvent(new EventReactionAdd(e)));
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent e) {
        Utils.sync(() -> DiSky.getInstance().getServer().getPluginManager().callEvent(new EventVoiceMove(e)));
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent e) {
        Utils.sync(() -> DiSky.getInstance().getServer().getPluginManager().callEvent(new EventVoiceLeave(e)));
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent e) {
        Utils.sync(() -> DiSky.getInstance().getServer().getPluginManager().callEvent(new EventVoiceJoin(e)));
    }

    @Override
    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent e) {
        Utils.sync(() -> DiSky.getInstance().getServer().getPluginManager().callEvent(new EventReactionRemove(e)));
    }
    @Override
    public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent e) {
        Utils.sync(() -> DiSky.getInstance().getServer().getPluginManager().callEvent(new EventNickChange(e)));
    }
    @Override
    public void onGuildMessageDelete(GuildMessageDeleteEvent e) {
        Utils.sync(() -> DiSky.getInstance().getServer().getPluginManager().callEvent(new EventMessageDelete(e)));
    }

    @Override
    public void onRoleCreate(RoleCreateEvent e) {
        Utils.sync(() -> DiSky.getInstance().getServer().getPluginManager().callEvent(new EventRoleCreate(e)));
    }

    @Override
    public void onRoleDelete(RoleDeleteEvent e) {
        Utils.sync(() -> DiSky.getInstance().getServer().getPluginManager().callEvent(new EventRoleDelete(e)));
    }

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent e) {
        Utils.sync(() -> DiSky.getInstance().getServer().getPluginManager().callEvent(new EventRoleAdd(e)));
    }

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent e) {
        Utils.sync(() -> DiSky.getInstance().getServer().getPluginManager().callEvent(new EventRoleRemove(e)));
    }


    @Override
    public void onGuildMemberUpdateBoostTime(GuildMemberUpdateBoostTimeEvent e) {
        Utils.sync(() -> DiSky.getInstance().getServer().getPluginManager().callEvent(new EventMemberBoost(e)));
    }

    @Override
    public void onGuildBan(GuildBanEvent e) {
        Utils.sync(() -> DiSky.getInstance().getServer().getPluginManager().callEvent(new EventGuildBan(e)));
    }

    @Override
    public void onGuildUnban(GuildUnbanEvent e) {
        Utils.sync(() -> DiSky.getInstance().getServer().getPluginManager().callEvent(new EventGuildUnban(e)));
    }

}
