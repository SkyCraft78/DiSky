package info.itsthesky.disky.skript.events.message;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.MessageEvent;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;

public class MessageEdit extends DiSkyEvent<GuildMessageUpdateEvent> {

    public static String oldContent = "";
    public static String newContent = "";

    static {
        DiSkyEvent.register("Inner Event Name", MessageEdit.class, EvtMessageEdit.class,
                "message edit[ed]")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");

        EventValues.registerEventValue(EvtMessageEdit.class, UpdatingMessage.class, new Getter<UpdatingMessage, EvtMessageEdit>() {
            @Override
            public UpdatingMessage get(EvtMessageEdit event) {
                return UpdatingMessage.from(event.getJDAEvent().getMessage());            }
        }, 0);

        EventValues.registerEventValue(EvtMessageEdit.class, User.class, new Getter<User, EvtMessageEdit>() {
            @Override
            public User get(EvtMessageEdit event) {
                return event.getJDAEvent().getAuthor();
            }
        }, 0);

        EventValues.registerEventValue(EvtMessageEdit.class, String.class, new Getter<String, EvtMessageEdit>() {
            @Override
            public String get(EvtMessageEdit event) {
                return newContent;
            }
        }, 1);

        EventValues.registerEventValue(EvtMessageEdit.class, String.class, new Getter<String, EvtMessageEdit>() {
            @Override
            public String get(EvtMessageEdit event) {
                return newContent;
            }
        }, 0);

        EventValues.registerEventValue(EvtMessageEdit.class, String.class, new Getter<String, EvtMessageEdit>() {
            @Override
            public String get(EvtMessageEdit event) {
                return oldContent;
            }
        }, -1);

        EventValues.registerEventValue(EvtMessageEdit.class, Member.class, new Getter<Member, EvtMessageEdit>() {
            @Override
            public Member get(EvtMessageEdit event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(EvtMessageEdit.class, TextChannel.class, new Getter<TextChannel, EvtMessageEdit>() {
            @Override
            public TextChannel get(EvtMessageEdit event) {
                return event.getJDAEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EvtMessageEdit.class, Guild.class, new Getter<Guild, EvtMessageEdit>() {
            @Override
            public Guild get(EvtMessageEdit event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtMessageEdit.class, JDA.class, new Getter<JDA, EvtMessageEdit>() {
            @Override
            public JDA get(EvtMessageEdit event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtMessageEdit extends SimpleDiSkyEvent<GuildMessageUpdateEvent> implements MessageEvent {
        public EvtMessageEdit(MessageEdit event) { }

        @Override
        public MessageChannel getMessageChannel() {
            return getJDAEvent().getChannel();
        }
    }

}