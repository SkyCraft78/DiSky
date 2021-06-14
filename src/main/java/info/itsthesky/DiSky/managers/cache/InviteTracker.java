package info.itsthesky.disky.managers.cache;

import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.skript.events.skript.bot.EventBotJoin;
import info.itsthesky.disky.skript.events.skript.members.EventMemberJoin;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
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
    public void onReady(final ReadyEvent event) {
        event.getJDA().getGuilds().forEach(this::attemptInviteCaching);
    }

    @Override
    public void onGuildJoin(final GuildJoinEvent event)
    {
        Utils.sync(() -> DiSky.getInstance().getServer().getPluginManager().callEvent(new EventBotJoin(event)));
        final Guild guild = event.getGuild();
        attemptInviteCaching(guild);
    }

    @Override
    public void onGuildLeave(final GuildLeaveEvent event)
    {
        final long guildId = event.getGuild().getIdLong();
        inviteCache.entrySet().removeIf(entry -> entry.getValue().getGuildId() == guildId);
    }

    private void attemptInviteCaching(final Guild guild) {
        Utils.async(() -> {
            DiSky.getInstance().getLogger().info("Started invite cache for guild " + guild.getName() + "...");
            long start = System.currentTimeMillis();
            try {
                guild.retrieveInvites().queue(is -> {
                    for (Invite invite : is) inviteCache.put(invite.getCode(), new CachedInvite(invite));
                });
            } catch (InsufficientPermissionException e) {
                DiSky.getInstance().getLogger().severe("DiSky can't catch invite for the event-invite in member join for guild '"+guild.getName()+"', need permission: " + e.getPermission().getName());
            }
            DiSky.getInstance().getLogger().info("Invite cache for guild " + guild.getName() + " finished! Took " + (start - System.currentTimeMillis()) + "ms!");
        });
    }

}