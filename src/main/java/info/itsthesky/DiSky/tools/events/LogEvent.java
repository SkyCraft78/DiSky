package info.itsthesky.disky.tools.events;

import net.dv8tion.jda.api.entities.User;

import java.lang.reflect.Member;

/**
 * Mean a {@link net.dv8tion.jda.api.entities.User} changed something on the logs
 */
public interface LogEvent {

    User getActionAuthor();

}
