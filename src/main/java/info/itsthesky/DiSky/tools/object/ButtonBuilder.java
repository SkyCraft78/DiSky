package info.itsthesky.disky.tools.object;

import info.itsthesky.disky.DiSky;
import net.dv8tion.jda.api.interactions.button.Button;
import net.dv8tion.jda.api.interactions.button.ButtonStyle;

import javax.annotation.Nullable;

public class ButtonBuilder {

    private String content;
    private ButtonStyle color;
    private Emote emote;
    private boolean isLink;
    private boolean isDisabled;
    private String idOrURl;

    public ButtonBuilder(String idOrURl, @Nullable Boolean isLink) {
        this.idOrURl = idOrURl;
        this.content = null;
        this.color = ButtonStyle.PRIMARY;
        this.isDisabled = false;
        this.isLink = isLink != null && isLink;
        this.emote = null;
    }

    private ButtonBuilder(
            String content,
            ButtonStyle color,
            Emote emote,
            boolean isLink,
            boolean isDisabled,
            String idOrURl
    ) {
        this.content = content;
        this.color = color;
        this.emote = emote;
        this.isLink = isLink;
        this.isDisabled = isDisabled;
        this.idOrURl = idOrURl;
    }

    public static ButtonBuilder fromButton(Button original) {
        return new ButtonBuilder(
                original.getLabel(),
                original.getStyle(),
                (original.getEmoji() != null ? new Emote(original.getEmoji()) : null),
                original.getStyle().equals(ButtonStyle.LINK),
                original.isDisabled(),
                (original.getStyle().equals(ButtonStyle.LINK) ? original.getUrl() : original.getId())
        );
    }

    public Button build() {
        if (isEmpty()) {
            DiSky.getInstance().getLogger().severe("[DiSky] You're trying to build and empty (without content / emote) message button!");
            return null;
        }

        Button button;
        String c = content;
        ButtonStyle style = isLink ? ButtonStyle.LINK : color;

        button = emote == null ? Button.of(style, idOrURl, c) : Button.of(style, idOrURl, c).withEmoji(emote.getEmoji());
        return isDisabled ? button.asDisabled() : button.asEnabled();
    }

    @Override
    public String toString() {
        return "ButtonBuilder{" +
                "content='" + content + '\'' +
                ", color=" + color +
                ", emote=" + emote +
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

    public String getId() {
        return idOrURl;
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

    public Emote getEmote() {
        return emote;
    }

    public void setEmote(Emote emote) {
        this.emote = emote;
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
