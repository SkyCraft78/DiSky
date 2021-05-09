package info.itsthesky.disky.tools.object;

import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public enum Badge {
    STAFF("", User.UserFlag.STAFF),
    PARTNER("https://i.discord.fr/5nB.png", User.UserFlag.PARTNER),
    HYPESQUAD("https://i.discord.fr/CS2.png", User.UserFlag.HYPESQUAD),
    BUG_HUNTER_LEVEL_1("https://i.discord.fr/Ye7.png", User.UserFlag.BUG_HUNTER_LEVEL_1),
    HYPESQUAD_BRAVERY("https://i.discord.fr/N5s.png", User.UserFlag.HYPESQUAD_BRAVERY),
    HYPESQUAD_BRILLIANCE("https://i.discord.fr/bVC.png", User.UserFlag.HYPESQUAD_BRILLIANCE),
    HYPESQUAD_BALANCE("https://i.discord.fr/ig0.png", User.UserFlag.HYPESQUAD_BALANCE),
    EARLY_SUPPORTER("https://i.discord.fr/6Fe.png", User.UserFlag.EARLY_SUPPORTER),
    TEAM_USER("https://i.discord.fr/BbZ.png", User.UserFlag.TEAM_USER),
    SYSTEM("", User.UserFlag.SYSTEM),
    BUG_HUNTER_LEVEL_2("https://i.discord.fr/2F7.png", User.UserFlag.BUG_HUNTER_LEVEL_2),
    VERIFIED_BOT("", User.UserFlag.VERIFIED_BOT),
    VERIFIED_DEVELOPER("https://i.discord.fr/Vov.png", User.UserFlag.VERIFIED_DEVELOPER);

    private final String imageLink;
    private final User.UserFlag original;
    Badge(final String imageLink, final User.UserFlag original) {
        this.imageLink = imageLink;
        this.original = original;
    }

    public String getImageLink() {
        return imageLink;
    }

    public User.UserFlag getOriginal() {
        return original;
    }

    @Nullable
    public static Badge getFromOriginal(@NotNull final User.UserFlag original) {
        for (Badge badge : values()) if (badge.getOriginal() == original) return badge;
        return null;
    }
}
