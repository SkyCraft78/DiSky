package info.itsthesky.disky.skript.events.message;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.MessageEvent;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public class ReactionAdd extends DiSkyEvent<GuildMessageReactionAddEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", ReactionAdd.class, EvtReactionAdd.class,
                "reaction add[ed]")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");

       EventValues.registerEventValue(EvtReactionAdd.class, User.class, new Getter<User, EvtReactionAdd>() {
            @Override
            public User get(EvtReactionAdd event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

       EventValues.registerEventValue(EvtReactionAdd.class, Member.class, new Getter<Member, EvtReactionAdd>() {
            @Override
            public Member get(EvtReactionAdd event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

       EventValues.registerEventValue(EvtReactionAdd.class, User.class, new Getter<User, EvtReactionAdd>() {
            @Override
            public User get(EvtReactionAdd event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

       EventValues.registerEventValue(EvtReactionAdd.class, Member.class, new Getter<Member, EvtReactionAdd>() {
            @Override
            public Member get(EvtReactionAdd event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

       EventValues.registerEventValue(EvtReactionAdd.class, info.itsthesky.disky.tools.object.Emote.class, new Getter<info.itsthesky.disky.tools.object.Emote, EvtReactionAdd>() {
            @Override
            public info.itsthesky.disky.tools.object.Emote get(EvtReactionAdd event) {
                return info.itsthesky.disky.tools.object.Emote.fromReaction(event.getJDAEvent().getReactionEmote());
            }
        }, 0);

       EventValues.registerEventValue(EvtReactionAdd.class, TextChannel.class, new Getter<TextChannel, EvtReactionAdd>() {
            @Override
            public TextChannel get(EvtReactionAdd event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

       EventValues.registerEventValue(EvtReactionAdd.class, Guild.class, new Getter<Guild, EvtReactionAdd>() {
            @Override
            public Guild get(EvtReactionAdd event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtReactionAdd.class, JDA.class, new Getter<JDA, EvtReactionAdd>() {
            @Override
            public JDA get(EvtReactionAdd event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

        EventValues.registerEventValue(EvtReactionAdd.class, UpdatingMessage.class, new Getter<UpdatingMessage, EvtReactionAdd>() {
            @Override
            public UpdatingMessage get(EvtReactionAdd event) {
                return UpdatingMessage.from(event.getJDAEvent().getMessageId());
            }
        }, 0);

    }

    public static class EvtReactionAdd extends SimpleDiSkyEvent<GuildMessageReactionAddEvent> implements MessageEvent {
        public EvtReactionAdd(ReactionAdd event) { }

        @Override
        public MessageChannel getMessageChannel() {
            return getJDAEvent().getChannel();
        }
    }

}