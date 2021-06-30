package info.itsthesky.disky.tools;

import ch.njol.skript.classes.Comparator;
import net.dv8tion.jda.api.entities.ISnowflake;

public class DiSkyComparator<T extends ISnowflake> implements Comparator<T, T> {

    @Override
    public Relation compare(T first, T second) {
        if (first.getId().equalsIgnoreCase(second.getId()))
            return Relation.EQUAL;
        return Relation.NOT_EQUAL;
    }

    @Override
    public boolean supportsOrdering() {
        return false;
    }
}
