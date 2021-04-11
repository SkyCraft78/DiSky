package info.itsthesky.DiSky.managers.cache;

import net.dv8tion.jda.api.entities.Invite;

/**
 * Way to cache JDA's entity, bypassing the cache problem.
 * This one is for invite, to get the invite used in a join event.
 * @author ItsThesky
 */
public class CachedInvite {

    private final long guildId;
    private final long userID;
    private final String code;
    private int uses;

    public CachedInvite(final Invite invite) {
        this.guildId = invite.getGuild().getIdLong();
        this.uses = invite.getUses();
        this.userID = invite.getInviter().getIdLong();
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
