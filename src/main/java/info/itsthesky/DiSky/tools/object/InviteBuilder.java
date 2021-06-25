package info.itsthesky.disky.tools.object;

import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.TextChannel;

public class InviteBuilder {

    private Number maxAge;
    private int maxUse;
    private boolean temp;

    public InviteBuilder() {
        temp = true;
    }

    public Invite build(TextChannel channel) {
        return channel
                .createInvite()
                .setMaxAge(maxAge == null ? null : maxAge.intValue())
                .setMaxUses(maxUse)
                .setTemporary(temp)
                .complete();
    }

    public Number getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Number maxAge) {
        this.maxAge = maxAge;
    }

    public int getMaxUse() {
        return maxUse;
    }

    public void setMaxUse(int maxUse) {
        this.maxUse = maxUse;
    }

    public boolean isTemp() {
        return temp;
    }

    public void setTemp(boolean temp) {
        this.temp = temp;
    }

    @Override
    public String toString() {
        return "InviteBuilder{" +
                "maxAge=" + maxAge +
                ", maxUse=" + maxUse +
                ", temp=" + temp +
                '}';
    }
}
