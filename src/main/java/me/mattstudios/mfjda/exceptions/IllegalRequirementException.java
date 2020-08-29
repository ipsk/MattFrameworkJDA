package me.mattstudios.mfjda.exceptions;

import java.lang.reflect.Method;

public final class IllegalRequirementException extends RuntimeException {

    public IllegalRequirementException(final Method method, final Class<?> caller, final String message) {
        super("Method " + method.getName() + " in class " + caller.getSimpleName() + message);
    }
}
