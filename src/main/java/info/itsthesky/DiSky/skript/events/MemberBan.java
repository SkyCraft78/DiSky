package info.itsthesky.disky.skript.events;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;

public class MemberBan extends DiSkyEvent<GuildBanEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", MemberBan.class, EvtMemberBan.class,
                "")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtMemberBan.class, User.class, new Getter<User, EvtMemberBan>() {
            @Override
            public User get(EvtMemberBan event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

       EventValues.registerEventValue(EvtMemberBan.class, Guild.class, new Getter<Guild, EvtMemberBan>() {
            @Override
            public Guild get(EvtMemberBan event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtMemberBan.class, JDA.class, new Getter<JDA, EvtMemberBan>() {
            @Override
            public JDA get(EvtMemberBan event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtMemberBan extends SimpleDiSkyEvent<GuildBanEvent> {
        public EvtMemberBan(MemberBan event) { }
    }

}