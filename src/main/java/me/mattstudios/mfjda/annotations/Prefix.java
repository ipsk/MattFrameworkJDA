package me.mattstudios.mfjda.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the prefix for the {@link Command command}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Prefix {

    /**
     * The value for the {@link Command command} prefix
     *
     * Named "value" due to the way Java annotations work
     */
    String[] value();
}
