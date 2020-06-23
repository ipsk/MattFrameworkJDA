package me.mattstudios.mfjda.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines if the class annotated is a {@link me.mattstudios.mfjda.base.CommandBase command}
 *
 * Annotate the command that extends {@link me.mattstudios.mfjda.base.CommandBase CommandBase}
 * with this
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {

    /**
     * The name of the command
     *
     * Named "value" due to the way Java annotations work
     */
    String[] value();
}
