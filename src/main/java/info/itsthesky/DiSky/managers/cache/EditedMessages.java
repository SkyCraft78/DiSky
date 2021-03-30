package info.itsthesky.DiSky.managers.cache;

import info.itsthesky.DiSky.DiSky;
import info.itsthesky.DiSky.skript.events.skript.messages.MessageEdit.EventMessageEdit;
import info.itsthesky.DiSky.skript.events.skript.messages.MessageEdit.ExprNewContent;
import info.itsthesky.DiSky.skript.events.skript.messages.MessageEdit.ExprOldContent;
import info.itsthesky.DiSky.tools.Utils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
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
            DiSky.getInstance().getLogger().warning("DiSky can't retrieve the cached message '"+id+"', since it was not cached!");
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
            DiSky.getInstance().getLogger().warning("DiSky can't retrieve the cached message '"+id+"', since it was not cached!");
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
            for (TextChannel channel : guild.getTextChannels()) {
                for (Message message : channel.getHistory().getRetrievedHistory()) {
                    previousMessages.put(message.getId(), new CachedMessage(message));
                }
            }
        }
    }

    @Override
    public void onGuildMessageUpdate(GuildMessageUpdateEvent e) {
        if (!previousMessages.containsKey(e.getMessageId())) {
            previousMessages.put(e.getMessageId(), new CachedMessage(e.getMessage()));
        }
        newMessages.put(e.getMessageId(), new CachedMessage(e.getMessage()));
        ExprNewContent.newContent = retrieveNewMessage(e.getMessageId()).getContent();
        ExprOldContent.oldContent = retrieveOldMessage(e.getMessageId()).getContent();
        Utils.sync(() -> DiSky.getInstance().getServer().getPluginManager().callEvent(new EventMessageEdit(e)));
    }

}
