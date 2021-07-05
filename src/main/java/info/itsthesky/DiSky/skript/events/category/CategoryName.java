package info.itsthesky.disky.skript.events.category;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.LogEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdateNameEvent;

public class CategoryName extends DiSkyEvent<CategoryUpdateNameEvent> {

    static {
        DiSkyEvent.register("Category Name", CategoryName.class, EvtCategoryName.class,
                "category name (change|update)")
                .setName("Category Name");

       EventValues.registerEventValue(EvtCategoryName.class, Category.class, new Getter<Category, EvtCategoryName>() {
            @Override
            public Category get(EvtCategoryName event) {
                return event.getJDAEvent().getEntity();
            }
        }, 0);

       EventValues.registerEventValue(EvtCategoryName.class, Guild.class, new Getter<Guild, EvtCategoryName>() {
            @Override
            public Guild get(EvtCategoryName event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtCategoryName.class, Category.class, new Getter<Category, EvtCategoryName>() {
            @Override
            public Category get(EvtCategoryName event) {
                return event.getJDAEvent().getCategory();
            }
        }, 0);

       EventValues.registerEventValue(EvtCategoryName.class, JDA.class, new Getter<JDA, EvtCategoryName>() {
            @Override
            public JDA get(EvtCategoryName event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtCategoryName extends SimpleDiSkyEvent<CategoryUpdateNameEvent> implements LogEvent {
        public EvtCategoryName(CategoryName event) { }

        @Override
        public User getActionAuthor() {
            return getJDAEvent().getGuild().retrieveAuditLogs().complete().get(0).getUser();
        }
    }

}