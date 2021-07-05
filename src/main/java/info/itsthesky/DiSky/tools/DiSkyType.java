package info.itsthesky.disky.tools;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.function.Function;

/**
 * @author ItsTheSky
 */
public class DiSkyType<T> {

    private final Class<T> clazz;
    private final String codeName;
    private final String user;
    private final Function<T, String> toString;
    private final Function<String, T> parser;
    private final boolean isEnum;
    private final ClassInfo<T> classInfo;

    public DiSkyType(Class<T> clazz, String codeName, String user, Function<T, String> toString, @Nullable Function<String, T> parser, boolean isEnum) {
        this.clazz = clazz;
        this.codeName = codeName;
        this.user = user;
        this.toString = toString;
        this.parser = parser;
        this.isEnum = isEnum;
        this.classInfo = new ClassInfo<>(clazz, codeName)
                .user(user)
                .parser(new Parser<T>() {
                    @Override
                    public @NotNull T parse(final @NotNull String input, final @NotNull ParseContext context) {
                        if (parser == null) return null;
                        if (isEnum) {
                            try {
                                return parser.apply(input);
                            } catch (Exception ignored) {
                                return null;
                            }
                        } else {
                            if (context.equals(ParseContext.COMMAND))
                                return parser.apply(DiSkyType.this.parse(input));
                            return null;
                        }
                    }

                    @Override
                    public boolean canParse(ParseContext context) {
                        if (parser == null) return false;
                        if (isEnum) return true;
                        return context.equals(ParseContext.COMMAND);
                    }

                    @Override
                    public @NotNull String toString(@NotNull T entity, int flags) {
                        try {
                            return toString.apply(entity);
                        } catch (Exception ex) {
                            return entity.toString();
                        }
                    }

                    @Override
                    public @NotNull String toVariableNameString(@NotNull T entity) {
                        try {
                            return toString.apply(entity);
                        } catch (Exception ex) {
                            return entity.toString();
                        }
                    }

                    @Override
                    public @NotNull String getVariableNamePattern() {
                        return ".+";
                    }
                });
    }

    /**
     * By default, assuming this entity have a getName method, and can avoid repeating method via reflection.
     * Plus, parse the ID as an ISnowflake entity.
     * @param clazz The class related to this type
     * @param typeName The single name used in expression
     */
    public DiSkyType(Class<T> clazz, String typeName, String user, Function<String, T> parser) {
        this(
                clazz,
                typeName,
                user,
                entity -> ReflectionUtils.invokeMethod(entity.getClass(), "getName", entity),
                parser,
                false
        );
    }

    public DiSkyType(Class<T> clazz, String typeName, String user) {
        this(
                clazz,
                typeName,
                user,
                entity -> ReflectionUtils.invokeMethod(entity.getClass(), "getName", entity),
                null,
                false
        );
    }

    public static <T extends Enum<?>> DiSkyType<T> fromEnum(Class<T> enumClass, String typeName, String user) {
        return new DiSkyType<>(
                enumClass,
                typeName,
                user,
                entity -> entity.name().toLowerCase(Locale.ROOT).replaceAll("_", " "),
                input -> {
                    try {
                        return ReflectionUtils.invokeMethodEx(enumClass, "valueOf", null, input.toUpperCase(Locale.ROOT).replaceAll(" ", "_"));
                    } catch (Exception ex) {
                        return null;
                    }
                },
                true
                );
    }

    private String parse(String input) {
        return Utils.parseLong(input, false, true).toString();
    }

    // Should never be used outside of the class btw
    public void register(ClassInfo<T> classInfo) {
        Classes.registerClass(classInfo);
    }

    public void register() {
        Classes.registerClass(classInfo);
    }

    public boolean isEnum() {
        return isEnum;
    }

    public ClassInfo<T> getClassInfo() {
        return classInfo;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public String getCodeName() {
        return codeName;
    }

    public String getUser() {
        return user;
    }

    public Function<String, T> getParser() {
        return parser;
    }

    public Function<T, String> getToString() {
        return toString;
    }
}
