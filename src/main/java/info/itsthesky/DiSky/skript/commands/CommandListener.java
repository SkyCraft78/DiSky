package info.itsthesky.disky.skript.commands;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.localization.Language;
import info.itsthesky.disky.managers.BotManager;
import info.itsthesky.disky.oldevents.skript.EventDiSkyCommand;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CommandListener extends ListenerAdapter {
    public static MessageReceivedEvent lastCommandEvent;


    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getAuthor().isBot()) {
            return;
        }

        String content = e.getMessage().getContentRaw();

        for (CommandData storage : CommandFactory.getInstance().getCommands()) {
            CommandObject command = storage.getCommand();
            List<String> globalPrefixes = Arrays.asList(BotManager.prefixes.values().toArray(new String[0]).clone());
            for (Expression<String> prefix : command.getPrefixes()) {
                for (String alias : command.getUsableAliases()) {
                    Message message = e.getMessage();
                    TextChannel textChannel = null;
                    Guild guild = null;
                    if (message.isFromGuild()) {
                        textChannel = e.getTextChannel();
                        guild = e.getGuild();
                    }
                    CommandEvent event = new CommandEvent(e,
                            null, alias, command, null,
                            guild, e.getChannel(), textChannel, message,
                            e.getAuthor(), e.getMember(), e.getJDA());

                    String usedCommand = null;
                    String rawPrefix = prefix.getSingle(event);
                    boolean mentions = false;
                    List<User> mentionedUsers = e.getMessage().getMentionedUsers();
                    if (!mentionedUsers.isEmpty()) {
                        if (rawPrefix.contains(mentionedUsers.get(0).getId())) {
                            rawPrefix = rawPrefix.replaceFirst("!", "");
                            mentions = true;
                        }
                    }

                    if (rawPrefix.endsWith(" ")) {
                        // TODO I'm now questioning the need for the regex replacing, check this out
                        String[] spacedCommand = content.split(" ");
                        String suspectedPrefix = mentions ? spacedCommand[0].replaceFirst("!", "") : spacedCommand[0];
                        if ((suspectedPrefix + " ").equalsIgnoreCase(rawPrefix)) {
                            usedCommand = rawPrefix + (spacedCommand.length == 1 ? "" : spacedCommand[1]);
                        }

                    } else {
                        usedCommand = mentions ? content.split(" ")[0].replaceFirst("!", "") : content.split(" ")[0];
                    }

                    try {
                        String usedPrefixes = usedCommand.split("")[0].toLowerCase(Locale.ROOT);
                    } catch (Exception ignored) {}
                    String rawCommand = "";

                    // TODO: 29/05/2021 Fix the global prefix system. HArd to interfere with original one :'(
                    if (nonNull(usedCommand)) {
                        if ((nonNull(rawPrefix) && usedCommand.equalsIgnoreCase(rawPrefix + alias))) {
                            event.setPrefix(rawPrefix);
                            try {
                                event.setArguments(content.substring((usedCommand).length() + 1));
                            } catch (StringIndexOutOfBoundsException e1) {
                                event.setArguments(null);
                            }
                            // Because most of bukkit's apis are sync only, make sure to run this on bukkit's thread
                            Utils.sync(() -> {

                                lastCommandEvent = e;
                                Bukkit.getPluginManager().callEvent(event);
                                if (!event.isCancelled()) {
                                    command.execute(event);
                                }
                                Bukkit.getPluginManager().callEvent(new EventDiSkyCommand(e, command));

                            });

                            return;

                        }
                    }

                }

            }
        }
    }

    /**
     * Checks if a string is null or the localized string form of null in Skript (usually <none>)
     */
    public boolean nonNull(String s) {
        if (s == null) {
            return false;
        }
        String localized = Language.get("none");
        if (localized == null) {
            // on old skript versions you couldn't change <none> so assume that if result is null
            return !s.equals("<none>");
        }
        return !s.equals(localized);
    }

}