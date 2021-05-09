package info.itsthesky.disky.managers.music;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

public class AudioData {

    private final JDA jda;
    private final Guild guild;

    public AudioData(Guild guild, JDA jda) {
        this.guild = guild;
        this.jda = jda;
    }

    public JDA getJda() {
        return jda;
    }

    public Guild getGuild() {
        return guild;
    }
}
