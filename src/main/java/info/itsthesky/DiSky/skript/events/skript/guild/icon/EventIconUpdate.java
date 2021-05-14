package info.itsthesky.disky.skript.events.skript.guild.icon;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateIconEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Name("Guild Icon Update")
@Description("Run when someone change the icon of the specific guild. See old icon url and new icon url.")
@Examples("on guild icon update:")
@Since("1.9")
public class EventIconUpdate extends Event {

    static {
        Skript.registerEvent("Invite Create", SimpleEvent.class, EventIconUpdate.class, "[discord] guild icon (change|update)")
            .description("Run when someone change the icon of the specific guild. See old icon url and new icon url.")
            .examples("on guild icon update:")
            .since("1.9");

        EventValues.registerEventValue(EventIconUpdate.class, JDA.class, new Getter<JDA, EventIconUpdate>() {
            @Nullable
            @Override
            public JDA get(final @NotNull EventIconUpdate event) {
                return event.getEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EventIconUpdate.class, Guild.class, new Getter<Guild, EventIconUpdate>() {
            @Nullable
            @Override
            public Guild get(final @NotNull EventIconUpdate event) {
                return event.getEvent().getGuild();
            }
        }, 0);

    }

    private static final HandlerList HANDLERS = new HandlerList();

    private final GuildUpdateIconEvent e;

    public EventIconUpdate(
            final GuildUpdateIconEvent e
            ) {
        super(Utils.areEventAsync());
        this.e = e;
        ExprNewIconURL.newIconURL = e.getNewIconUrl();
        ExprOldIconURL.oldIconURL = e.getOldIconUrl();
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public GuildUpdateIconEvent getEvent() {
        return e;
    }
}