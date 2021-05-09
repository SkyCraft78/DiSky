package info.itsthesky.disky.tools.object;

import info.itsthesky.disky.DiSky;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class TextChannelBuilder {

    private String name;
    private String topic;
    private boolean nsfw;
    private boolean news;
    private int slowmode;
    private Category category;


    public TextChannelBuilder() {
        this.name = "default-name";
        this.nsfw = false;
        this.slowmode = 0;
        this.topic = "";
        this.news = false;
        this.category = null;
    }

    public TextChannel createChannel(Guild guild) {
        return guild
                .createTextChannel(this.name)
                .setSlowmode(this.slowmode)
                .setTopic(this.topic)
                .setNews(this.news)
                .setParent(this.category)
                .setNSFW(this.nsfw)
                .complete();
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public boolean isNsfw() {
        return nsfw;
    }

    public void setNsfw(boolean nsfw) {
        this.nsfw = nsfw;
    }

    public boolean isNews() {
        return news;
    }

    public void setNews(boolean news) {
        this.news = news;
    }

    public int getSlowmode() {
        return slowmode;
    }

    public void setSlowmode(int slowmode) {
        if ((slowmode < 0) || (slowmode > TextChannel.MAX_SLOWMODE)) {
            DiSky.getInstance().getLogger()
                    .severe("Cannot set the slow mode amount of a textchannel if the value is not between 0 and " + TextChannel.MAX_SLOWMODE + " seconds! (Yours is "+slowmode+")");
            return;
        }
        this.slowmode = slowmode;
    }
}
