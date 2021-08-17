package info.itsthesky.disky.tools;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.Variable;
import ch.njol.skript.lang.VariableString;
import ch.njol.skript.util.Date;
import ch.njol.skript.util.Timespan;
import ch.njol.skript.variables.Variables;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.DiSky;
import info.itsthesky.disky.managers.BotManager;
import info.itsthesky.disky.tools.emojis.EmojiManager;
import info.itsthesky.disky.tools.emojis.EmojiParser;
import info.itsthesky.disky.tools.object.ButtonBuilder;
import info.itsthesky.disky.tools.object.ButtonRow;
import info.itsthesky.disky.tools.object.Emote;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;
import net.dv8tion.jda.api.utils.data.DataArray;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.*;
import java.util.function.Consumer;

public class Utils extends ListenerAdapter {

    private static final String defaultString = "";
    private static final Number defaultNumber = 0;
    private static final Boolean defaultBoolean = false;
    private static final Object defaultObject = "";
    public static final boolean INFO_CACHE = getOrSetDefault("config.yml", "InfoCache", true);

    public static Kleenean getHasDelayBefore() {
        return DiSky.getSkriptAdapter().getHasDelayBefore();
    }

    public static <T> T verifyVar(@NotNull Event e, @Nullable Expression<T> expression) {
        return verifyVar(e, expression, null);
    }

    public static <T> T verifyVar(@NotNull Event e, @Nullable Expression<T> expression, T defaultValue) {
        return expression == null ? defaultValue : (expression.getSingle(e) == null ? defaultValue : expression.getSingle(e));
    }

    public static @Nullable Variable<?> parseVar(Expression<?> expression, boolean shouldBeList) {
        if (expression instanceof Variable<?>) {
            if (shouldBeList && !((Variable<?>) expression).isList()) {
                Skript.error("The specified variable must be a list!");
                return null;
            }
            return (Variable<?>) expression;
        }
        Skript.error("You must specific a valid variable, but got " + expression.toString());
        return null;
    }

    public static @Nullable Variable<?> parseVar(Expression<?> expression) {
        return parseVar(expression, false);
    }

    public static ReplyAction parseComponents(ReplyAction action, Object... components) {
        return action.addActionRows(parseRows(components).toArray(new ActionRow[0]));
    }

    /**
     * @author Sky
     */
    @SuppressWarnings("unchecked")
    public static @Nullable Object pasteVarMaps(Object first, Object second) {
        try {
            Field firstHashMapField = ReflectionUtils.getField(first.getClass(), "hashMap");
            Field firstTreeMapField = ReflectionUtils.getField(first.getClass(), "treeMap");

            Field secondHashMapField = ReflectionUtils.getField(second.getClass(), "hashMap");
            Field secondTreeMapField = ReflectionUtils.getField(second.getClass(), "treeMap");

            HashMap<String, Object> firstHashMap = ReflectionUtils.getFieldValue(firstHashMapField, first);
            HashMap<String, Object> firstTreeMap = ReflectionUtils.getFieldValue(firstTreeMapField, first);

            ((HashMap<String, Object>) secondTreeMapField.get(null)).putAll(firstTreeMap);
            ((HashMap<String, Object>) secondHashMapField.get(null)).putAll(firstHashMap);

            return second;

        } catch (Exception ex) {
            return null;
        }
    }

    public static MessageAction parseComponents(MessageAction action, Object... components) {
        return action.setActionRows(parseRows(components).toArray(new ActionRow[0]));
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] safeArray(T... original) {
        List<T> safes = new ArrayList<>();
        for (T o : original) {
            if (o instanceof Object[]) {
                Collections.addAll(safes, (T[]) o);
            } else {
                safes.add(o);
            }
        }
        return (T[]) safes.toArray(new Object[0]);
    }

    /**
     * Handler of the result and the exception of a JDA's rest action.
     * @param restAction   The rest action to hande
     * @param consumer     The consumer to execute when the rest action succeed or not.
     * @param defaultValue The default value to use if an exception occur. Can be null.
     * @author Sky
     */
    public static <T> void handleRestAction(RestAction<T> restAction, Consumer<T> consumer, @Nullable T defaultValue) {
        restAction.queue(
                consumer,
                ex -> {
                    DiSkyErrorHandler.logException(ex);
                    consumer.accept(defaultValue);
                }
        );
    }

    public static void addEmoteToMessage(Emote emote, Message message) {
        if (emote.isAnimated()) {
            message.addReaction(emote.getEmote()).queue(null, DiSkyErrorHandler::logException);
        } else {
            if (emote.isEmote()) {
                message.addReaction(emote.getEmote()).queue(null, DiSkyErrorHandler::logException);
            } else {
                message.addReaction(emote.getAsMention()).queue(null, DiSkyErrorHandler::logException);
            }
        }
    }

    public static List<ActionRow> parseRows(Object... components) {
        components = safeArray(components);
        // We assume it's a "SET" action
        List<ActionRow> rows = new ArrayList<>();
        try {
            for (Object component : components) {
                if (component == null) continue;
                if (component instanceof ButtonRow) {
                    List<Button> buttons = new ArrayList<>();
                    for (ButtonBuilder buttonBuilder : ((ButtonRow) component).getButtons()) {
                        if (buttonBuilder.build() != null)
                            buttons.add(buttonBuilder.build());
                    }
                    if (buttons.size() > 0) rows.add(ActionRow.of(buttons.toArray(new Component[0])));
                } else {
                    SelectionMenu selects = ((SelectionMenu.Builder) component).build();
                    rows.add(ActionRow.of(selects));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rows;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] verifyVars(@NotNull Event e, @Nullable Expression<T> expression) {
        return expression == null ? (T[]) new Object[0] : (expression.getArray(e) == null ? (T[]) new Object[0] : expression.getArray(e));
    }

    public static Emoji convert(Emote emote) {
        return emote.isEmote() ? Emoji.fromEmote(emote.getEmote()) : Emoji.fromUnicode(emote.getAsMention());
    }

    public static void setHasDelayBefore(Kleenean hasDelayBefore) {
        DiSky.getSkriptAdapter().setHasDelayBefore(hasDelayBefore);
    }

    public static Color toJavaColor(org.bukkit.Color bukkitColor) {
        return new Color(bukkitColor.getRed(), bukkitColor.getGreen(), bukkitColor.getBlue());
    }

    public static @Nullable String getLatestVersion() {
        try {
            DataArray json = DataArray.fromJson(getTextFromURL("https://api.github.com/repos/SkyCraft78/DiSky/tags"));
            return json.getObject(1).getString("name");
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getTextFromURL(String url) {
        String output = "null";
        try {
            output = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O Error: " + e.getMessage());
        }
        return output;
    }

    @Nullable
    public static Guild getGuildFromEvent(Event event) {
        Guild guild = null;
        try {
            guild = (Guild) event.getClass().getDeclaredMethod("getEvent()").getClass().getDeclaredMethod("getGuild()").invoke(event);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) { }
        return guild;
    }
    
    public static List<ButtonBuilder> convertButtons(List<Button> originals) {
        final List<ButtonBuilder> buttonBuilders = new ArrayList<>();
        for (Button button : originals) {
            buttonBuilders.add(ButtonBuilder.fromButton(button));
        }
        return buttonBuilders;
    }

    private static  <T> Object getDefaultValue(T object) {
        if (object instanceof String) {
            return defaultString;
        } else if (object instanceof Number) {
            return defaultNumber;
        } else if (object instanceof Boolean) {
            return defaultBoolean;
        } else {
            return defaultObject;
        }
    }

    public static GuildChannel getChannel(TextChannel channel, Guild guild) {
        return guild.getGuildChannelById(channel.getId());
    }

    public static String generateCode(final int size) {
        String cars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i <= size; i++) {
            int r = (int) (Math.random() * (-size)) + size;
            builder.append(cars.split("")[r]);
        }
        return builder.toString();
    }

    public static VariableString getVariableName(Variable<?> var) {
        return DiSky.getSkriptAdapter().getVariableName(var);
    }

    public static boolean containURL(String input) {
        return input.contains("http://") ||
                input.contains("https://") ||
                input.contains("www") ||
                input.contains(".com") ||
                input.contains(".org") ||
                input.contains(".html") ||
                input.contains(".php");
    }

    public static boolean areJDASimilar(JDA jda, Object bot) {
        if (bot == null) return true;
        if (jda == null) return false;
        JDA botJDA = bot instanceof JDA ? (JDA) bot : BotManager.getBot(bot.toString());
        if (botJDA == null) return false;
        return jda == botJDA;
    }

    public static boolean areEmojiSimilar(MessageReaction.ReactionEmote first, Emote second) {
        if (first.isEmote()) {
            Emote f = new Emote(first.getEmote());
            return f.getName().equalsIgnoreCase(second.getName());
        } else {
            return EmojiParser.parseToUnicode(first.getAsReactionCode()).equals(second.getAsMention());
        }
    }

    @SafeVarargs
    public static <T> Consumer<T> combine(Consumer<T>... consumers) {
        return Arrays.stream(consumers).reduce(s -> {}, Consumer::andThen);
    }

    public static LinkedHashMap<String, Integer> sortHashMapByValues(
            HashMap<String, Integer> passedMap) {
        List<String> mapKeys = new ArrayList<>(passedMap.keySet());
        List<Integer> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<String, Integer> sortedMap =
                new LinkedHashMap<>();

        for (Integer val : mapValues) {
            Iterator<String> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                String key = keyIt.next();
                Integer comp1 = passedMap.get(key);

                if (comp1.equals(val)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }

    public static <T, Q> LinkedHashMap<T, Q> reverseMap(LinkedHashMap<T, Q> toReverse) {
        LinkedHashMap<T, Q> reversedMap = new LinkedHashMap<>();
        List<T> reverseOrderedKeys = new ArrayList<>(toReverse.keySet());
        Collections.reverse(reverseOrderedKeys);
        reverseOrderedKeys.forEach((key)->reversedMap.put(key,toReverse.get(key)));
        return reversedMap;
    }

    public static List<Permission> convertPerms(String... perms) {
        List<Permission> permissions = new ArrayList<>();
        for (String s : perms) {
            try {
                permissions.add(Permission.valueOf(s.replace(" ", "_").toUpperCase()));
            } catch (IllegalArgumentException ignored) {}
        }
        return permissions;
    }

    public static boolean isClassExist(String className) {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    public static Emote unicodeFrom(String input, Guild guild) {
        String id = input.replaceAll("[^0-9]", "");
        if (id.isEmpty()) {
            try {
                if (guild == null) {
                    Set<JDA> jdaInstances = BotManager.getBotsJDA();
                    for (JDA jda : jdaInstances) {
                        Collection<net.dv8tion.jda.api.entities.Emote> emoteCollection = jda.getEmotesByName(input, false);
                        if (!emoteCollection.isEmpty()) {
                            return new Emote(emoteCollection.iterator().next());
                        }
                    }
                    return unicodeFrom(input);
                }
                Collection<net.dv8tion.jda.api.entities.Emote> emotes = guild.getEmotesByName(input, false);
                if (emotes.isEmpty()) {
                    return unicodeFrom(input);
                }

                return new Emote(emotes.iterator().next());
            } catch (UnsupportedOperationException | NoSuchElementException x) {
                return null;
            }
        } else {
            if (guild == null) {
                Set<JDA> jdaInstances = BotManager.getBotsJDA();
                for (JDA jda : jdaInstances) {
                    net.dv8tion.jda.api.entities.Emote emote = jda.getEmoteById(id);
                    if (emote != null) {
                        return new Emote(emote);
                    }
                }
                return unicodeFrom(input);
            }
            try {
                net.dv8tion.jda.api.entities.Emote emote = guild.getEmoteById(id);
                if (emote == null) {
                    net.dv8tion.jda.api.entities.Emote emote1 = guild.getJDA().getEmoteById(id);
                    if (!(emote1 == null)) {
                        return new Emote(emote1);
                    }
                    return null;
                }

                return new Emote(emote);
            } catch (UnsupportedOperationException | NoSuchElementException x) {
                return null;
            }
        }
    }

    public static Emote unicodeFrom(String input) {
        if (EmojiManager.isEmoji(input)) {
            return new Emote(input, EmojiParser.parseToUnicode(input));
        } else {
            String emote = input.contains(":") ? input : ":" + input + ":";
            return new Emote(input.replaceAll(":", ""), EmojiParser.parseToUnicode(emote));
        }
    }

    public static SelectionMenu.Builder convert(SelectionMenu original) {
        return SelectionMenu.fromData(original.toData());
    }

    @Nullable
    public static Long parseLong(@NotNull String s, boolean manageDiscordValue) {
        return parseLong(s, false, manageDiscordValue);
    }

    @Nullable
    public static Long parseLong(@NotNull String s, boolean shouldPrintError, boolean manageDiscordValue) {
        Long id = null;
        if (manageDiscordValue) {
            s = s
                    .replaceAll("&", "")
                    .replaceAll("<", "")
                    .replaceAll(">", "")
                    .replaceAll("#", "")
                    .replaceAll("!", "")
                    .replaceAll("@", "");
        }
        try {
            id = Long.parseLong(s);
        } catch (NumberFormatException e) {
            //if (shouldPrintError) e.printStackTrace();
            return null;
        }
        return id;
    }

    public static boolean equalsAnyIgnoreCase(String toMatch, String... potentialMatches) {
        return Arrays.asList(potentialMatches).contains(toMatch);
    }

    public static boolean areEventAsync() {
        return false;
    }

    public static String getPrefixName() {
        return "disky";
    }

    public static TextChannel checkChannel(Object original) {
        if (original instanceof TextChannel) {
            return (TextChannel) original;
        } else {
            return null;
        }
    }

    public static String replaceFirst(String s, String pattern, String replacement) {
        int idx = s.indexOf(pattern);
        return s.substring(0, idx) + replacement + s.substring(idx + pattern.length());
    }

    public static int hexToInt(String hex) {
        hex = hex.replace("#", "");
        return Integer.parseInt(hex, 16);
    }

    public static Integer round(Double number) {
        String t = number.toString().split("\\.")[0];
        return Integer.valueOf(t);
    }

    public static boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static <T> void setSkriptVariable(Variable<T> variable, Object value, Event event) {
        variable.change(event, new Object[] {value}, Changer.ChangeMode.SET);
    }

    public static <T> void setSkriptList(Variable<T> variable, Event event, List<?> values) {
        List<Object> list = Collections.singletonList(values);
        String name = variable.getName().getDefaultVariableName();

        int separatorLength = Variable.SEPARATOR.length() + 1;
        name = name.substring(0, (name.length() - separatorLength));
        name = name.toLowerCase() + Variable.SEPARATOR;
        for (int i = 1; i < list.size()+1; i++){
            Variables.setVariable(name + i, list.get(i-1), event, variable.isLocal());
        }
    }

    @Nullable
    public static Member searchMember(JDA bot, String id) {
        for (Guild guild : bot.getGuilds()) {
            for (Member member : guild.getMembers()) {
                if (member.getId().equalsIgnoreCase(id)) return member;
            }
        }
        return null;
    }

    @Nullable
    public static Message searchMessage(JDA bot, String id) {
        for (Guild guild : bot.getGuilds()) {
            for (TextChannel channel : guild.getTextChannels()) {
                return channel.retrieveMessageById(id).complete();
            }
        }
        return null;
    }

    public static String colored(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static User castMember(Member entity) {
        return entity.getUser();
    }

    public static Object getFieldValue(String path) throws Exception {
        int lastDot = path.lastIndexOf(".");
        String className = path.substring(0, lastDot);
        String fieldName = path.substring(lastDot + 1);
        Class myClass = Class.forName(className);
        Field myField = myClass.getDeclaredField(fieldName);
        return myField.get(null);
    }

    @Override
    public void onReady(ReadyEvent e) {
        timeHashMap.remove(e.getJDA());
        try {
            timeHashMap.put(e.getJDA(), Date.now());
        } catch (NoSuchMethodError ex) {
            DiSky.getInstance().getLogger().severe("DiSky has bad support for 2.2 and older version of Skript. Some features, such as uptime and color conversion won't work!");
        }
    }
    public static HashMap<JDA, Date> timeHashMap = new HashMap<>();
    public static Timespan getUpTime(JDA jda) {
        return timeHashMap.get(jda).difference(Date.now());
    }

    public static void sync(Runnable runnable) {
        Bukkit.getScheduler().runTask(DiSky.getInstance(), runnable);
    }

    public static void async(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(DiSky.getInstance(), runnable);
    }

    public static boolean areSlashCommandsEnabled() {
        File file = new File(DiSky.getInstance().getDataFolder(), "config.yml");
        if (!file.exists()) return false;
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (config.contains("SlashCommand.Enabled")) return false;
        if (!config.getBoolean("SlashCommand.Enabled")) {
            DiSky.getInstance().getLogger().warning("Slash Commands are disabled by default, but you're trying to use them somewhere.");
            DiSky.getInstance().getLogger().warning("See the wiki (https://github.com/SkyCraft78/DiSky/wiki/Slash-Commands) to know how enable and use them!");
        }
        return config.getBoolean("SlashCommand.Enabled");
    }

    @SuppressWarnings("unchecked")
    public static <T> T getOrSetDefault(String file, String path, T defaultValue) {
        File f = new File(DiSky.getInstance().getDataFolder(), file);
        if (!f.exists()) return (T) getDefaultValue(defaultValue);
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(f);
        if (!configuration.contains(path)) {
            configuration.set(path, defaultValue);
            try {
                configuration.save(f);
            } catch (IOException e) {
                DiSky.getInstance().getLogger().warning("Unable to save the default DiSky configuration file :c Error: " + e.getMessage());
            }
            return defaultValue;
        }
        return (T) configuration.get(path, defaultValue);
    }

    public static String[] valuesToString(Object... values) {
        List<String> s = new ArrayList<>();
        for (Object o : values) {
            s.add(o.toString());
        }
        return s.toArray(new String[0]);
    }

    public static void saveResourceAs(String inPath) {
        if (inPath == null || inPath.isEmpty()) {
            throw new IllegalArgumentException("The input path cannot be null or empty");
        }
        InputStream in = DiSky.getInstance().getResource(inPath);
        if (in == null) {
            throw new IllegalArgumentException("The file "+inPath+" cannot be found in plugin's resources");
        }
        if (!DiSky.getInstance().getDataFolder().exists() && !DiSky.getInstance().getDataFolder().mkdir()) {
            DiSky.getInstance().getLogger().severe("Failed to make the main directory !");
        }
        File inFile = new File(DiSky.getInstance().getDataFolder(), inPath);
        if (!inFile.exists()) {
            Bukkit.getConsoleSender().sendMessage("§cThe file "+inFile.getName()+" cannot be found, creating one for you ...");

            DiSky.getInstance().saveResource(inPath, false);

            if (!inFile.exists()) {
                DiSky.getInstance().getLogger().severe("Unable to copy file!");
            } else {
                Bukkit.getConsoleSender().sendMessage("§aThe file "+inFile.getName()+" has been created!");
            }
        }
    }
}
