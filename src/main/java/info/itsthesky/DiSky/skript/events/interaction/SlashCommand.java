package info.itsthesky.disky.skript.events.interaction;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.InteractionEvent;
import info.itsthesky.disky.tools.StaticData;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class SlashCommand extends DiSkyEvent<SlashCommandEvent> {

    static {
        DiSkyEvent.register("Slash Command", SlashCommand.class, EvtSlashCommand.class,
                "slash command")
                .setName("Slash Command");

        EventValues.registerEventValue(EvtSlashCommand.class, String.class, new Getter<String, EvtSlashCommand>() {
            @Override
            public String get(EvtSlashCommand event) {
                return event.getJDAEvent().getName();
            }
        }, 0);

        EventValues.registerEventValue(EvtSlashCommand.class, GuildChannel.class, new Getter<GuildChannel, EvtSlashCommand>() {
            @Override
            public GuildChannel get(EvtSlashCommand event) {
                return (GuildChannel) event.getJDAEvent().getChannel();
            }
        }, 0);

        EventValues.registerEventValue(EvtSlashCommand.class, Guild.class, new Getter<Guild, EvtSlashCommand>() {
            @Override
            public Guild get(EvtSlashCommand event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

        EventValues.registerEventValue(EvtSlashCommand.class, Member.class, new Getter<Member, EvtSlashCommand>() {
            @Override
            public Member get(EvtSlashCommand event) {
                return event.getJDAEvent().getMember();
            }
        }, 0);

        EventValues.registerEventValue(EvtSlashCommand.class, User.class, new Getter<User, EvtSlashCommand>() {
            @Override
            public User get(EvtSlashCommand event) {
                return event.getJDAEvent().getUser();
            }
        }, 0);

        EventValues.registerEventValue(EvtSlashCommand.class, JDA.class, new Getter<JDA, EvtSlashCommand>() {
            @Override
            public JDA get(EvtSlashCommand event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtSlashCommand extends SimpleDiSkyEvent<SlashCommandEvent> implements InteractionEvent {
        private final SlashCommandEvent event;
        public EvtSlashCommand(SlashCommand event) {
            this.event = getJDAEvent();
            StaticData.lastSlashCommandEvent = this.event;
        }

        @Override
        public GenericInteractionCreateEvent getInteractionEvent() {
            return getJDAEvent();
        }
    }


}