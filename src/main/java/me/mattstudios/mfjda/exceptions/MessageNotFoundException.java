package me.mattstudios.mfjda.exceptions;

public final class MessageNotFoundException extends RuntimeException {

    public MessageNotFoundException(final String messageID) {
        super("The message ID '" + messageID + " does not exist");
    }
}
