package info.itsthesky.disky.tools.events;

public interface DiSkyGetter<F, T> {
    F get(T arg);
}