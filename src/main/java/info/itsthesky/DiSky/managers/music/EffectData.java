package info.itsthesky.DiSky.managers.music;

import net.dv8tion.jda.api.entities.Guild;

public class EffectData {

    private Guild guild;
    private Long guildID;
    private double speed;
    private double depth;
    private double pitch;
    private double frequency;

    public EffectData(Guild guild) {
        this.guildID = guild.getIdLong();
        this.depth = 0.1;
        this.speed = 1.0;
        this.pitch = 1.0;
        this.frequency = 0.001;
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }
    public Long getGuildID() {
        return guildID;
    }
    public void setGuildID(Long guildID) {
        this.guildID = guildID;
    }

    public double getSpeed() {
        return speed;
    }
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    public void addSpeed(double speed) {
        this.speed = this.speed + speed;
    }
    public void removeSpeed(double speed) {
        this.speed = this.speed - speed;
    }

    public double getPitch() {
        return pitch;
    }
    public void setPitch(double pitch) {
        this.pitch = pitch;
    }
    public void addPitch(double pitch) {
        this.pitch = this.pitch + pitch;
    }
    public void removePitch(double pitch) {
        this.pitch = this.pitch - pitch;
    }

    public double getDepth() {
        return depth;
    }
    public void addDepth(double depth) {
        this.depth = this.depth + depth;
    }
    public void removeDepth(double depth) {
        this.depth = this.depth - depth;
    }
    public void setDepth(double depth) {
        this.depth = depth;
    }

    public double getFrequency() {
        return frequency;
    }
    public void addFrequency(double frequency) {
        this.frequency = this.frequency + frequency;
    }
    public void removeFrequency(double frequency) {
        this.frequency = this.frequency - frequency;
    }
    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }
}
