package info.itsthesky.disky.skript.events;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateOwnerEvent;

public class GuildOwner extends DiSkyEvent<GuildUpdateOwnerEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", GuildOwner.class, EvtGuildOwner.class,
                "[guild] owner (change|update)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtGuildOwner.class, Member.class, new Getter<Member, EvtGuildOwner>() {
            @Override
            public Member get(EvtGuildOwner event) {
                return event.getJDAEvent().getOldOwner();
            }
        }, -1);

       EventValues.registerEventValue(EvtGuildOwner.class, Member.class, new Getter<Member, EvtGuildOwner>() {
            @Override
            public Member get(EvtGuildOwner event) {
                return event.getJDAEvent().getNewOwner();
            }
        }, 1);

       EventValues.registerEventValue(EvtGuildOwner.class, Guild.class, new Getter<Guild, EvtGuildOwner>() {
            @Override
            public Guild get(EvtGuildOwner event) {
                return event.getJDAEvent().getEntity();
            }
        }, 0);

       EventValues.registerEventValue(EvtGuildOwner.class, JDA.class, new Getter<JDA, EvtGuildOwner>() {
            @Override
            public JDA get(EvtGuildOwner event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtGuildOwner extends SimpleDiSkyEvent<GuildUpdateOwnerEvent> {
        public EvtGuildOwner(GuildOwner event) { }
    }

}