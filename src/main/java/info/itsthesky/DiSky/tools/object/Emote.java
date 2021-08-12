package info.itsthesky.disky.tools.object;

import info.itsthesky.disky.tools.emojis.EmojiParser;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Emote implements IMentionable {
    private final String name;
    private net.dv8tion.jda.api.entities.Emote emote;
    private final boolean isEmote;
    private final String mention;
    private Emoji emoji;

    public Emote(String name, String mention) {
        this.name = name.replaceAll(":", "");
        this.mention = mention;
        this.isEmote = false;
    }

    public static Emote fromReaction(MessageReaction.ReactionEmote emote) {
        if (emote.isEmote()) {
            return new Emote(emote.getEmote());
        } else {
            return new Emote(EmojiParser.parseToAliases(emote.getEmoji()), emote.getEmoji());
        }
    }

    public Emote(Emoji emoji) {
        this.emoji = emoji;
        this.mention = emoji.getName();
        this.name = emoji.getId();
        this.isEmote = false;
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
        return isEmote ? emote.getName() : name;
    }

    public JDA getJDA() {
        return isEmote ? emote.getJDA() : null;
    }

    @NotNull
    @Override
    public String getId() {
        return getID();
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

    public String getID() {
        return isEmote ? emote.getId() : name;
    }

    @Override
    public @NotNull String getAsMention() {
        return isEmote ? emote.getAsMention() : mention;
    }

    @Override
    public long getIdLong() {
        return Long.parseLong(getID());
    }
}