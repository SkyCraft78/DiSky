package info.itsthesky.disky.skript.events;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdatePositionEvent;

public class CategoryPosition extends DiSkyEvent<CategoryUpdatePositionEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", CategoryPosition.class, EvtCategoryPosition.class,
                "category position (change|update)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");


       EventValues.registerEventValue(EvtCategoryPosition.class, Category.class, new Getter<Category, EvtCategoryPosition>() {
            @Override
            public Category get(EvtCategoryPosition event) {
                return event.getJDAEvent().getEntity();
            }
        }, 0);

       EventValues.registerEventValue(EvtCategoryPosition.class, Guild.class, new Getter<Guild, EvtCategoryPosition>() {
            @Override
            public Guild get(EvtCategoryPosition event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtCategoryPosition.class, Category.class, new Getter<Category, EvtCategoryPosition>() {
            @Override
            public Category get(EvtCategoryPosition event) {
                return event.getJDAEvent().getCategory();
            }
        }, 0);

       EventValues.registerEventValue(EvtCategoryPosition.class, JDA.class, new Getter<JDA, EvtCategoryPosition>() {
            @Override
            public JDA get(EvtCategoryPosition event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtCategoryPosition extends SimpleDiSkyEvent<CategoryUpdatePositionEvent> {
        public EvtCategoryPosition(CategoryPosition event) { }
    }

}