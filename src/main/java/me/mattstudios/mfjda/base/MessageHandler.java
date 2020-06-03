package me.mattstudios.mfjda.base;

import me.mattstudios.mfjda.base.components.MessageResolver;
import me.mattstudios.mfjda.exceptions.MfException;
import net.dv8tion.jda.api.entities.Message;

import java.util.HashMap;
import java.util.Map;

public final class MessageHandler {

    // The map with the messages to send.
    private final Map<String, MessageResolver> messages = new HashMap<>();

    // Registers all the default messages.
    MessageHandler() {
        register("cmd.no.permission", message -> message.getChannel().sendMessage("You don't have permission to run this command!").queue());
        register("cmd.no.exists", message -> message.getChannel().sendMessage("That command doesn't exist!").queue());
        register("cmd.wrong.usage", message -> message.getChannel().sendMessage("Wrong usage for command!").queue());
    }

    /**
     * Method to register new messages and overwrite the existing ones.
     *
     * @param messageId       The message ID to be set.
     * @param messageResolver The message resolver function.
     */
    public void register(final String messageId, final MessageResolver messageResolver) {
        messages.put(messageId, messageResolver);
    }

    /**
     * Checks if the message has ID
     * @param messageId the ID to check
     * @return True or False
     */
    boolean hasId(String messageId) {
        return messages.get(messageId) != null;
    }

    /**
     * Sends the registered message to the channel.
     *
     * @param messageId The message ID.
     * @param message   The message that was sent to get data to send the message to.
     */
    void sendMessage(final String messageId, final Message message) {
        final MessageResolver messageResolver = messages.get(messageId);
        if (messageResolver == null) throw new MfException("The message ID " + messageId + " does not exist!");
        messageResolver.resolve(message);
    }

}
