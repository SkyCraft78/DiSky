package info.itsthesky.disky.skript.events;

import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import info.itsthesky.disky.tools.events.DiSkyEvent;
import info.itsthesky.disky.tools.events.SimpleDiSkyEvent;
import net.dv8tion.jda.api.JDA;
import info.itsthesky.disky.tools.object.UpdatingMessage;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdatePermissionsEvent;

import java.util.List;

public class CategoryPermission extends DiSkyEvent<CategoryUpdatePermissionsEvent> {

    static {
        DiSkyEvent.register("Inner Event Name", CategoryPermission.class, EvtCategoryPermission.class,
                "category permission (change|update)")
                .setName("Docs Event Name")
                .setDesc("Event description")
                .setExample("Event Example");

       EventValues.registerEventValue(EvtCategoryPermission.class, Guild.class, new Getter<Guild, EvtCategoryPermission>() {
            @Override
            public Guild get(EvtCategoryPermission event) {
                return event.getJDAEvent().getGuild();
            }
        }, 0);

       EventValues.registerEventValue(EvtCategoryPermission.class, Category.class, new Getter<Category, EvtCategoryPermission>() {
            @Override
            public Category get(EvtCategoryPermission event) {
                return event.getJDAEvent().getCategory();
            }
        }, 0);

       EventValues.registerEventValue(EvtCategoryPermission.class, JDA.class, new Getter<JDA, EvtCategoryPermission>() {
            @Override
            public JDA get(EvtCategoryPermission event) {
                return event.getJDAEvent().getJDA();
            }
        }, 0);

    }

    public static class EvtCategoryPermission extends SimpleDiSkyEvent<CategoryUpdatePermissionsEvent> {
        public EvtCategoryPermission(CategoryPermission event) { }
    }

}