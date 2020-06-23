package me.mattstudios.mfjda.exceptions;

/**
 * An exception thrown when something goes wrong internally
 * with the framework
 */
public final class MfException extends RuntimeException {

    public MfException(final String message) {
        super(message);
    }
}
