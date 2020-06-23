package me.mattstudios.mfjda.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines if the annotated method has any requirements to
 * be allowed to execute the command/sub command
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Requirement {

    /**
     * If the method has any requirements, should be provided
     * as the String value of a {@link Boolean boolean} (e.g. "true")
     *
     * Named "value" and of type String due to the way Java
     * annotations work
     */
    String value();
}
