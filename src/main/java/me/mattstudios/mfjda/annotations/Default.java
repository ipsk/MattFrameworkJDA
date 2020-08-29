package me.mattstudios.mfjda.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines if the method annotated is the default method for the {@link me.mattstudios.mfjda.base.CommandBase command}.
 *
 * e.g. the method annotated with this annotation should be the method for handling !command
 *
 * @author Mateus Moreira
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Default {}
