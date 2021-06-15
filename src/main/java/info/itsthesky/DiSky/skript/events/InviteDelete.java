package info.itsthesky.disky.skript.events;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;

public class InviteDelete extends DiSkyEvent<GuildInviteDeleteEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", InviteDelete.class, EvtInviteDelete.class,
                "invite delete")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtInviteDelete.class, GuildChannel.class, new Getter<GuildChannel, EvtInviteDelete>() {
            @Override
            public GuildChannel get(EvtInviteDelete event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtInviteDelete.class, TextChannel.class, new Getter<TextChannel, EvtInviteDelete>() {
            @Override
            public TextChannel get(EvtInviteDelete event) {
                return event.getJDAEvent().getTextChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtInviteDelete.class, Category.class, new Getter<Category, EvtInviteDelete>() {
            @Override
            public Category get(EvtInviteDelete event) {
                return event.getJDAEvent().getCategory();
            }
        }, 0);

       EventValues.registerEventValue(EvtInviteDelete.class, VoiceChannel.class, new Getter<VoiceChannel, EvtInviteDelete>() {
            @Override
            public VoiceChannel get(EvtInviteDelete event) {
                return event.getJDAEvent().getVoiceChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtInviteDelete.class, GuildChannel.class, new Getter<GuildChannel, EvtInviteDelete>() {
            @Override
            public GuildChannel get(EvtInviteDelete event) {
                return (GuildChannel) event.getJDAEvent().getStoreChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtInviteDelete.class, Guild.class, new Getter<Guild, EvtInviteDelete>() {
            @Override
            public Guild get(EvtInviteDelete event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtInviteDelete.class, JDA.class, new Getter<JDA, EvtInviteDelete>() {
            @Override
            public JDA get(EvtInviteDelete event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtInviteDelete extends SimpleDiSkyEvent<GuildInviteDeleteEvent> {
        public EvtInviteDelete(InviteDelete event) { }
    }

}