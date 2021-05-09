package info.itsthesky.disky.managers.cache;

import info.itsthesky.disky.DiSky;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.MissingAccessException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by ItsTheSky the 20/03/2021 <br>
 * Allow the JDA to cache message with simple wrapper.
 */
public class Messages extends ListenerAdapter {

    private static final List<CachedMessage> cachedMessages = new ArrayList<>();

    public static void cacheMessage(Message message) {
        cachedMessages.add(new CachedMessage(message));
    }
    public static CachedMessage retrieveMessage(String id) {
        AtomicReference<CachedMessage> finalMessage = new AtomicReference<>();
        cachedMessages.forEach((cm) -> {
            if (cm.getMessageID().equalsIgnoreCase(id)) {
                finalMessage.set(cm);
            }
        });
        if (finalMessage.get() == null) {
            DiSky.getInstance().getLogger().warning("DiSky can't retrieve the cached message '"+id+"', since it was not cached!");
        }
        return finalMessage.get();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        cachedMessages.add(new CachedMessage(e.getMessage()));
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        if (e.getUser().isBot() || e.getUser().getId().equalsIgnoreCase(e.getJDA().getSelfUser().getId())) {
            for (TextChannel channel : e.getGuild().getTextChannels()) {
                for (Message message : channel.getHistory().getRetrievedHistory()) {
                    cachedMessages.add(new CachedMessage(message));
                }
            }
        }
    }

    @Override
    public void onReady(ReadyEvent e) {
        for (Guild guild : e.getJDA().getGuilds()) {
            for (TextChannel channel : guild.getTextChannels()) {
                List<Message> history = null;
                try {
                     history = channel.getHistory().retrievePast(100).complete();
                } catch (MissingAccessException ex) {
                    DiSky.getInstance().getLogger().warning("DiSky cannot cache message for the message delete event since the bot doesn't have the " + ex.getPermission().getName() + " permission!");
                }
                if (history == null) return;
                for (Message message : history) {
                    cachedMessages.add(new CachedMessage(message));
                }
            }
        }
    }

}
