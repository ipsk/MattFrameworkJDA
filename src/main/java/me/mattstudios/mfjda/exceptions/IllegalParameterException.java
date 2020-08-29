package me.mattstudios.mfjda.exceptions;

import java.lang.reflect.Method;

public final class IllegalParameterException extends RuntimeException {

    public IllegalParameterException(final Method method, final Class<?> caller, final String message) {
        super("Method " + method.getName() + " in class " + caller.getSimpleName() + message);
    }
}
