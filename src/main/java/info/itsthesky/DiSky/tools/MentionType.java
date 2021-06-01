package info.itsthesky.disky.tools;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * @author JDA
 */
public enum MentionType {
        USER("<@!?(\\d+)>", "users"),
        ROLE("<@&(\\d+)>", "roles"),
        CHANNEL("<#(\\d+)>", null),
        EMOTE("<a?:([a-zA-Z0-9_]+):([0-9]+)>", null),
        HERE("@here", "everyone"),
        EVERYONE("@everyone", "everyone");

        private final Pattern pattern;
        private final String parseKey;

        MentionType(String regex, String parseKey)
        {
            this.pattern = Pattern.compile(regex);
            this.parseKey = parseKey;
        }

        @NotNull
        public Pattern getPattern()
        {
            return pattern;
        }

        @Nullable
        public String getParseKey()
        {
            return parseKey;
        }
    }