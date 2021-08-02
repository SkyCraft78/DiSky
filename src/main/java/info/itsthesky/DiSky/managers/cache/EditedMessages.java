package info.itsthesky.disky.managers.cache;

import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.skript.events.message.MessageEdit;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.exceptions.MissingAccessException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Made by ItsTheSky the 25/03/2021 <br>
 * Allow the JDA to cache message with simple wrapper.
 */
public class EditedMessages extends ListenerAdapter {

    private static final HashMap<String, CachedMessage> previousMessages = new HashMap<>();
    private static final HashMap<String, CachedMessage> newMessages = new HashMap<>();

    public static void editMessage(Message message) {
        previousMessages.put(message.getId(), new CachedMessage(message));
    }

    public static CachedMessage retrieveNewMessage(String id) {
        AtomicReference<CachedMessage> finalMessage = new AtomicReference<>();
        newMessages.forEach((msgID, cm) -> {
            if (cm.getMessageID().equalsIgnoreCase(id)) {
                finalMessage.set(cm);
            }
        });
        if (finalMessage.get() == null) {
            DiSky.getInstance().getLogger().warning("DiSky cannot retrieve the cached message '"+id+"', since it was not cached!");
        }
        return finalMessage.get();
    }

    public static CachedMessage retrieveOldMessage(String id) {
        AtomicReference<CachedMessage> finalMessage = new AtomicReference<>();
        previousMessages.forEach((msgID, cm) -> {
            if (cm.getMessageID().equalsIgnoreCase(id)) {
                finalMessage.set(cm);
            }
        });
        if (finalMessage.get() == null) {
            DiSky.getInstance().getLogger().warning("DiSky cannot retrieve the cached message '"+id+"', since it was not cached!");
        }
        return finalMessage.get();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        previousMessages.put(e.getMessageId(), new CachedMessage(e.getMessage()));
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        if (e.getUser().isBot() || e.getUser().getId().equalsIgnoreCase(e.getJDA().getSelfUser().getId())) {
            for (TextChannel channel : e.getGuild().getTextChannels()) {
                for (Message message : channel.getHistory().getRetrievedHistory()) {
                    previousMessages.put(message.getId(), new CachedMessage(message));
                }
            }
        }
    }

    @Override
    public void onReady(ReadyEvent e) {
        for (Guild guild : e.getJDA().getGuilds()) {
            Utils.async(() -> {
                if (Utils.INFO_CACHE) DiSky.getInstance().getLogger().info("Starting cache of \"message edit\" event for guild " + guild.getName() + "...");
                long start = System.currentTimeMillis();
                for (TextChannel channel : guild.getTextChannels()) {
                    try {
                        channel.getIterableHistory().queue(history -> {
                            if (history == null) return;
                            for (Message message : history) {
                                previousMessages.put(message.getId(), new CachedMessage(message));
                            }
                        });
                    } catch (MissingAccessException ex) {
                        if (Utils.INFO_CACHE) DiSky.getInstance().getLogger().warning("DiSky cannot cache messages for "message edit" event since the bot doesn't have the " + ex.getPermission().getName() + " permission!");
                    }
                }
                if (Utils.INFO_CACHE) DiSky.getInstance().getLogger().info("Finished caching messages for \"message edit\" event for guild " + guild.getName() + "! Took " + (start - System.currentTimeMillis()) + "ms!");
            });
        }
    }

    @Override
    public void onGuildMessageUpdate(GuildMessageUpdateEvent e) {
        if (!previousMessages.containsKey(e.getMessageId())) {
            previousMessages.put(e.getMessageId(), new CachedMessage(e.getMessage()));
        }
        newMessages.put(e.getMessageId(), new CachedMessage(e.getMessage()));
        MessageEdit.oldContent = retrieveOldMessage(e.getMessageId()).getContent();
        MessageEdit.newContent = retrieveNewMessage(e.getMessageId()).getContent();
    }

}
