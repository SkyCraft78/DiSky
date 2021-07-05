package info.itsthesky.disky.skript.events.guild;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBoostTierEvent;

import java.util.Locale;

public class GuildBoostTier extends DiSkyEvent<GuildUpdateBoostTierEvent> {

    static {
        DiSkyEvent.register("Guild Boost Tier Update", GuildBoostTier.class, EvtGuildBoostTier.class,
                "[guild] boost tier (update|change)]")
                .setName("Guild Boost Tier Update");

       EventValues.registerEventValue(EvtGuildBoostTier.class, String.class, new Getter<String, EvtGuildBoostTier>() {
            @Override
            public String get(EvtGuildBoostTier event) {
                return event.getJDAEvent().getNewBoostTier().name().toLowerCase(Locale.ROOT).replace("_", "");
            }
        }, 1);

        EventValues.registerEventValue(EvtGuildBoostTier.class, String.class, new Getter<String, EvtGuildBoostTier>() {
            @Override
            public String get(EvtGuildBoostTier event) {
                return event.getJDAEvent().getOldBoostTier().name().toLowerCase(Locale.ROOT).replace("_", "");
            }
        }, -1);

        EventValues.registerEventValue(EvtGuildBoostTier.class, String.class, new Getter<String, EvtGuildBoostTier>() {
            @Override
            public String get(EvtGuildBoostTier event) {
                return event.getJDAEvent().getOldBoostTier().name().toLowerCase(Locale.ROOT).replace("_", "");
            }
        }, 0);

        EventValues.registerEventValue(EvtGuildBoostTier.class, String.class, new Getter<String, EvtGuildBoostTier>() {
            @Override
            public String get(EvtGuildBoostTier event) {
                return event.getJDAEvent().getOldBoostTier().name().toLowerCase(Locale.ROOT).replace("_", "");
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildBoostTier.class, Guild.class, new Getter<Guild, EvtGuildBoostTier>() {
            @Override
            public Guild get(EvtGuildBoostTier event) {
                return event.getJDAEvent().getEntity();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildBoostTier.class, Guild.class, new Getter<Guild, EvtGuildBoostTier>() {
            @Override
            public Guild get(EvtGuildBoostTier event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildBoostTier.class, JDA.class, new Getter<JDA, EvtGuildBoostTier>() {
            @Override
            public JDA get(EvtGuildBoostTier event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtGuildBoostTier extends SimpleDiSkyEvent<GuildUpdateBoostTierEvent> {
        public EvtGuildBoostTier(GuildBoostTier event) { }
    }

}