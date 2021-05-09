package info.itsthesky.disky.skript.expressions.embed.scope;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.managers.EmbedManager;
import info.itsthesky.disky.tools.EffectSection;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("Embed Builder")
@Description("This builder allow you to make embed easily. You can specify the template used, you must register this template before use it!")
@Since("1.0")
@Examples("discord command embed:\n" +
        "\tprefixes: !\n" +
        "\ttrigger:\n" +
        "\t\tmake embed:\n" +
        "\t\t\tset title of embed to \"Title\"\n" +
        "\t\t\tset description of embed to \"Description%nl%The title leads to the URL, if given\"\n" +
        "\t\t\tset author of the embed to \"Author name (Can point to URL)\"\n" +
        "\t\t\tset author icon of embed to \"https://cdn.discordapp.com/emojis/825811394963177533.png?v=1\"\n" +
        "\t\t\tset author url of embed to \"https://www.youtube.com/watch?v=i33DB6R8YUY\"\n" +
        "\t\t\tset colour of the embed to orange\n" +
        "\t\t\tadd inline field named \"Field Name\" with value \"Colour sets %nl%< that\" to fields of embed\n" +
        "\t\t\tadd inline field named \"Field Name\" with value \"Color is a java Color%nl%Not a string\" to fields of embed\n" +
        "\t\t\tadd inline field named \"Field Name\" with value \"Field value\" to fields of embed\n" +
        "\t\t\tadd field named \"Non-inline field name\" with value \"The number of fields that can be shown on the same row is limited to 3, but is limited to 2 when an image is included\" to fields of embed\n" +
        "\t\t\tset image of embed to \"https://media.discordapp.net/attachments/237757030708936714/390520880242884608/8xAac.png?width=508&height=522\"\n" +
        "\t\t\tset thumbnail of embed to \"https://cdn.discordapp.com/emojis/825811394963177533.png?v=1\"\n" +
        "\t\t\tset title url of embed to \"https://www.crunchyroll.com/fr/tonikawa-over-the-moon-for-you\"\n" +
        "\t\t\tset footer of embed to \"Footer text\"\n" +
        "\t\t\tset footer icon of embed to \"https://cdn.discordapp.com/emojis/825811394963177533.png?v=1\"\n" +
        "\t\t\tset timestamp of embed to now\n" +
        "\t\treply with last embed")
public class ScopeEmbed extends EffectSection {

    public static EmbedBuilder lastEmbed;
    private Expression<String> exprID;

    static {
        Skript.registerCondition(ScopeEmbed.class, "make [new] [discord] [message] embed [using [the] [template] [(named|with name|with id)] %-string%]");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (exprs.length != 0) exprID = (Expression<String>) exprs[0];
        if (checkIfCondition()) return false;
        if (!hasSection()) return false;
        loadSection(true);
        return true;
    }

    @Override
    protected void execute(Event e) {
        if (exprID != null) {
            String id = exprID.getSingle(e);
            if (id == null) lastEmbed = new EmbedBuilder();
            lastEmbed = EmbedManager.getTemplate(id);
        } else {
            lastEmbed = new EmbedBuilder();
        }
        runSection(e);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        if (exprID != null) {
            return "make new discord embed using template " + exprID.toString(e, debug);
        } else {
            return "make new discord embed";
        }
    }

}
