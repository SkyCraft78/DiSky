package info.itsthesky.disky.tools.object;

import info.itsthesky.disky.DiSky;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

import javax.annotation.Nullable;

public class ButtonBuilder implements ISnowflake {

    private String content;
    private ButtonStyle color;
    private Emoji emoji;
    private boolean isLink;
    private boolean isDisabled;
    private String idOrURl;

    public ButtonBuilder(String idOrURl, @Nullable Boolean isLink) {
        this.idOrURl = idOrURl;
        this.content = "";
        this.color = ButtonStyle.PRIMARY;
        this.isDisabled = false;
        this.isLink = isLink != null && isLink;
        this.emoji = null;
    }

    private ButtonBuilder(
            String content,
            ButtonStyle color,
            Emoji emoji,
            boolean isLink,
            boolean isDisabled,
            String idOrURl
    ) {
        this.content = content;
        this.color = color;
        this.emoji = emoji;
        this.isLink = isLink;
        this.isDisabled = isDisabled;
        this.idOrURl = idOrURl;
    }

    public static ButtonBuilder fromButton(Button original) {
        return new ButtonBuilder(
                original.getLabel(),
                original.getStyle(),
                (original.getEmoji() != null ? original.getEmoji() : null),
                original.getStyle().equals(ButtonStyle.LINK),
                original.isDisabled(),
                (original.getStyle().equals(ButtonStyle.LINK) ? original.getUrl() : original.getId())
        );
    }

    public Button build() {
        if (isEmpty()) {
            DiSky.getInstance().getLogger().severe("[DiSky] You're trying to build an empty (without content / emote) message button!");
            return null;
        }

        Button button;
        String c = content;
        ButtonStyle style = isLink ? ButtonStyle.LINK : color;

        try {
            button = emoji == null ? Button.of(style, idOrURl, c) : Button.of(style, idOrURl, c).withEmoji(emoji);
        } catch (Exception ex) {
            button = Button.of(style, idOrURl, c);
        }
        return isDisabled ? button.asDisabled() : button.asEnabled();
    }

    @Override
    public String toString() {
        return "ButtonBuilder{" +
                "content='" + content + '\'' +
                ", color=" + color +
                ", emote=" + emoji +
                ", isLink=" + isLink +
                ", isDisabled=" + isDisabled +
                ", idOrURl='" + idOrURl + '\'' +
                '}';
    }

    public void setIdOrURl(String idOrURl) {
        this.idOrURl = idOrURl;
    }

    public boolean isEmpty() {
        return content == null;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String getId() {
        return this.idOrURl;
    }

    @Override
    public long getIdLong() {
        try {
            return Long.parseLong(this.idOrURl);
        } catch (Exception ex) {
            return 0;
        }
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ButtonStyle getColor() {
        return color;
    }

    public void setColor(ButtonStyle color) {
        this.color = color;
    }

    public String getIdOrURl() {
        return idOrURl;
    }

    public Emoji getEmoji() {
        return emoji;
    }

    public void setEmoji(Emoji emote) {
        this.emoji = emote;
    }

    public boolean isLink() {
        return isLink;
    }

    public void setLink(boolean link) {
        isLink = link;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }
}
