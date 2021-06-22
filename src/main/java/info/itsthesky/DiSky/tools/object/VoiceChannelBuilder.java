package info.itsthesky.disky.tools.object;

import info.itsthesky.disky.DiSky;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class VoiceChannelBuilder {

    private String name;
    private Category parent;
    private Integer userLimit;
    private Integer bitrate;

    public VoiceChannelBuilder() {
        this.name = "Default Name";
    }

    public VoiceChannel build(Guild guild) {
        if (this.bitrate != null && bitrate < 8000 || bitrate != null && bitrate > guild.getMaxBitrate()) {
            DiSky.getInstance().getLogger().severe("You're trying to set a voice channel bitrate to " + bitrate +". However, this value can't be smaller than 8000 or greater than " + guild.getMaxBitrate() + " !");
            this.bitrate = null;
        }
        return guild
                .createVoiceChannel(this.name)
                .setParent(this.parent == null ? null : this.parent)
                .setUserlimit(this.userLimit == null ? null : this.userLimit)
                .setBitrate(this.bitrate == null ? null : this.bitrate)
                .complete();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Integer getUserLimit() {
        return userLimit;
    }

    public void setUserLimit(Integer userLimit) {
        this.userLimit = userLimit;
    }

    public Integer getBitrate() {
        return bitrate;
    }

    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }
}
