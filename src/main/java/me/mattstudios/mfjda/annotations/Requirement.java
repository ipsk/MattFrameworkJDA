package me.mattstudios.mfjda.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines if the annotated method has any requirements to
 * be allowed to execute the command/sub command
 *
 * @author Mateus Moreira
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Requirement {

    /**
     * If the method has any requirements, should be provided as the String value of
     * a {@link Boolean boolean} (e.g. "true")
     *
     * Named "value" because in Java annotations, "value" is the only parameter that
     * does not need to be explicitly named
     */
    String value();
}
