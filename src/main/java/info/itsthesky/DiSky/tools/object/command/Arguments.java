package info.itsthesky.disky.tools.object.command;

import java.util.List;

public class Arguments {

    private final List<String> args;

    public Arguments(List<String> args) {
        this.args = args;
    }

    public List<String> getArgs() {
        return args;
    }

}
