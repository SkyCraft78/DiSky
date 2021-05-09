package info.itsthesky.disky.skript.expressions.attachments;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import info.itsthesky.disky.tools.DiSkyErrorHandler;
import info.itsthesky.disky.tools.Utils;
import net.dv8tion.jda.api.entities.Message;
import org.bukkit.event.Event;

import java.io.File;

@Name("Download Attachment")
@Description("Download the specific attachment to a file path.")
@Examples("download {_attachment} in folder \"plugins/data/attachments/\"")
@Since("1.7")
public class EffAttDownload extends Effect {

    static {
        Skript.registerEffect(EffAttDownload.class,
                "["+ Utils.getPrefixName() +"] (download|dl) [the] [attachment] %attachment% (in|to) [the] [(folder|path)] %string%");
    }

    private Expression<Message.Attachment> exprAtt;
    private Expression<String> exprPath;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.exprAtt = (Expression<Message.Attachment>) exprs[0];
        this.exprPath = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        DiSkyErrorHandler.executeHandleCode(e, Event -> {
            Message.Attachment attachment = exprAtt.getSingle(e);
            String path = exprPath.getSingle(e);
            if (attachment == null || path == null) return;
            File file = new File(path);
            if (!file.isDirectory()) return;
            file.mkdirs();
            attachment.downloadToFile(file);
        });
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "download attachment " + exprAtt.toString(e, debug) + " to folder " + exprPath.toString(e, debug);
    }

}
