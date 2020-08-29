package me.mattstudios.mfjda.exceptions;

public class IllegalCommandException extends RuntimeException {

    public IllegalCommandException(final Class<?> commandClass, final String message) {
        super("Class " + commandClass.getSimpleName() + message);
    }
}
