package info.itsthesky.disky.tools;

import ch.njol.skript.config.Node;
import ch.njol.skript.log.SkriptLogger;

/**
 * @author Sky
 */
public class NodeInformation {

    private final Node node;
    private final int line;
    private final String fileName;
    private final String lineContent;

    public NodeInformation() {
        this.node = SkriptLogger.getNode();
        this.line = this.node.getLine();
        this.fileName = this.node.getConfig().getFileName();
        this.lineContent = this.node.getKey();
    }

    public String getDebugLabel() {
        return "(" + fileName + ", line " + line + ": " + lineContent + ")";
    }

    @Override
    public String toString() {
        return getDebugLabel();
    }

    public Node getNode() {
        return node;
    }

    public int getLine() {
        return line;
    }

    public String getFileName() {
        return fileName;
    }

    public String getLineContent() {
        return lineContent;
    }
}
