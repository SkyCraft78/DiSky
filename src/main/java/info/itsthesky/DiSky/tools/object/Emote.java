package info.itsthesky.disky.tools.object;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Emote implements IMentionable {
    private final String name;
    private net.dv8tion.jda.api.entities.Emote emote;
    private boolean isEmote = false;
    private final String mention;
    private Emoji emoji;

    public Emote(String name) {
        this.name = name;
        this.mention = name;
    }

    public Emote(MessageReaction.ReactionEmote emote) {
        if (emote.isEmote()) {
            this.name = emote.getEmote().getName();
            this.emote = emote.getEmote();
            this.isEmote = true;
            this.mention = emote.getEmote().getAsMention();
        } else {
            this.name = emote.getName();
            this.mention = name;
        }
        if (emote.isEmoji())
            emoji = Emoji.ofUnicode(emote.getAsCodepoints());
    }

    public Emote(Emoji emoji) {
        this.emoji = emoji;
        if (emoji.isCustom()) {
            this.name = emoji.getName();
            this.isEmote = true;
            this.mention = emoji.getAsMention();
        } else {
            this.name = emoji.getName();
            this.mention = name;
        }
    }

    public Emote(net.dv8tion.jda.api.entities.Emote emote) {
        this.name = emote.getName();
        this.emote = emote;
        this.isEmote = true;
        this.mention = emote.getAsMention();
    }

    public Guild getGuild() {
        return isEmote ? emote.getGuild() : null;
    }

    public net.dv8tion.jda.api.entities.Emote getEmote() {
        return isEmote ? emote : null;
    }

    public List<Role> getRoles() {
        return isEmote ? emote.getRoles() : null;
    }

    public String getName() {
        return name;
    }

    public JDA getJDA() {
        return isEmote ? emote.getJDA() : null;
    }

    public boolean isEmote() {
        return isEmote;
    }

    public String getMention() {
        return mention;
    }

    public Emoji getEmoji() {
        return emoji;
    }

    public boolean isAnimated() {
        return isEmote && emote.isAnimated();
    }

    public boolean compare(Emote emote) {
        return emote.getAsMention().equals(getAsMention());
    }

    public String getID() {
        return isEmote ? emote.getId() : null;
    }

    @Override
    public @NotNull String getAsMention() {
        return isEmote ? mention : name;
    }

    @Override
    public long getIdLong() {
        return 0;
    }
}