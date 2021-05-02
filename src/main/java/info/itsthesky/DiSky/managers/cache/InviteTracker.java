package info.itsthesky.DiSky.managers.cache;

import info.itsthesky.DiSky.DiSky;
import info.itsthesky.DiSky.skript.events.skript.EventBotJoin;
import info.itsthesky.DiSky.skript.events.skript.members.EventMemberJoin;
import info.itsthesky.DiSky.tools.Utils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InviteTracker extends ListenerAdapter {

    private final Map<String, CachedInvite> inviteCache = new ConcurrentHashMap<>();


    @Override
    public void onGuildInviteCreate(final GuildInviteCreateEvent event)
    {
        final String code = event.getCode();
        final CachedInvite inviteData = new CachedInvite(event.getInvite());
        inviteCache.put(code, inviteData);

    }

    @Override
    public void onGuildInviteDelete(final GuildInviteDeleteEvent event)
    {
        final String code = event.getCode();
        inviteCache.remove(code);
    }

    @Override
    public void onGuildMemberJoin(final GuildMemberJoinEvent event)
    {
        final Guild guild = event.getGuild();
        final Invite[] inv = new Invite[] {null};

        guild.retrieveInvites().complete().forEach(invite ->
        {
            if (inv[0] == null) return;
            final String code = invite.getCode();
            final CachedInvite cachedInvite = inviteCache.get(code);
            if (cachedInvite == null) return;
            if (invite.getUses() == cachedInvite.getUses()) return;
            cachedInvite.incrementUses();
            inv[0] = invite;
        });
        Utils.sync(() -> DiSky.getInstance().getServer().getPluginManager().callEvent(new EventMemberJoin(event, inv[0])));
    }

    @Override
    public void onGuildReady(final GuildReadyEvent event)
    {
        final Guild guild = event.getGuild();
        attemptInviteCaching(guild);
    }

    @Override
    public void onGuildJoin(final GuildJoinEvent event)
    {
        final Guild guild = event.getGuild();
        attemptInviteCaching(guild);

        Utils.sync(() -> DiSky.getInstance().getServer().getPluginManager().callEvent(new EventBotJoin(event)));
    }

    @Override
    public void onGuildLeave(final GuildLeaveEvent event)
    {
        final long guildId = event.getGuild().getIdLong();
        inviteCache.entrySet().removeIf(entry -> entry.getValue().getGuildId() == guildId);
    }

    private void attemptInviteCaching(final Guild guild)
    {
        final Member selfMember = guild.getSelfMember();

        if (!selfMember.hasPermission(Permission.MANAGE_SERVER))
            return;

        guild.retrieveInvites().queue(retrievedInvites ->
                retrievedInvites.forEach(retrievedInvite ->
                        inviteCache.put(retrievedInvite.getCode(), new CachedInvite(retrievedInvite))));
    }

}