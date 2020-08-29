package me.mattstudios.mfjda.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines if the class annotated is a {@link me.mattstudios.mfjda.base.CommandBase command}
 *
 * Annotate the command that extends {@link me.mattstudios.mfjda.base.CommandBase CommandBase}
 * with this to declare it as a command class.
 *
 * @author Mateus Moreira
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {

    /**
     * The name, or names, of the command (name + aliases).
     *
     * Note: there is no primary name for the command, all of them have the same
     * heirachy and will be treated equally
     *
     * Named "value" because in Java annotations, "value" is the only parameter that
     * does not need to be explicitly named
     */
    String[] value();
}
