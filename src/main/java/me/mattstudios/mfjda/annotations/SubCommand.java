package me.mattstudios.mfjda.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines if the method is a sub command of this {@link Command command}
 *
 * e.g. !command subcommand
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubCommand {

    /**
     * The name of the {@link SubCommand sub command}
     *
     * Named "value" due to the way Java annotations work
     */
    String[] value();
}
