package info.itsthesky.disky.managers.cache;

import net.dv8tion.jda.api.entities.Invite;

/**
 * Way to cache JDA's entity, bypassing the cache problem.
 * This one is for invite, to get the invite used in a join event.
 * @author ItsThesky
 */
public class CachedInvite {

    private long guildId;
    private long userID;
    private String code;
    private int uses;

    public CachedInvite(final Invite invite) {
        if (invite == null) return;
        this.guildId = invite.getGuild() == null ? null : invite.getGuild().getIdLong();
        this.uses = invite.getUses();
        this.userID = invite.getInviter() == null ? null : invite.getInviter().getIdLong();
        this.code = invite.getCode();
    }

    public long getGuildId() {
        return guildId;
    }
    public int getUses() {
        return uses;
    }
    public void incrementUses() {
        this.uses++;
    }
    public long getUserID() {
        return userID;
    }
    public String getCode() {
        return code;
    }
}
