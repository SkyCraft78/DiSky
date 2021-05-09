package info.itsthesky.disky.tools.object;

import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;

public class CategoryBuilder {

    private String name;
    private int position;

    public CategoryBuilder() {
        this.name = "default name";
        this.position = 1;
    }

    public Category build(Guild guild) {
        return guild
                .createCategory(this.name)
                .setPosition(this.position)
                .complete();
    }

    public void setPosition(int position) {
        this.position = position;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return this.position;
    }
    public String getName() {
        return this.name;
    }

}
