package me.mattstudios.mfjda.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the prefix for the {@link Command command}. This annotation must be
 * present along side {@link Command command} if used, and can only be omitted
 * if you explicitly specify a global prefix to use in your {@link me.mattstudios.mfjda.base.CommandManager command manager}
 *
 * @author Mateus Moreira
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Prefix {

    /**
     * The prefix, or prefixes, for the {@link Command command} (name + aliases).
     * Must be present along side a {@link Command command} annotation.
     *
     * Note: there is no primary prefix for the command, all of them have the same
     * heirachy and will be treated equally
     *
     * Named "value" because in Java annotations, "value" is the only parameter that
     * does not need to be explicitly named
     */
    String[] value();
}
