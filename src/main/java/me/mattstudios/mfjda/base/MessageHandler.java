package me.mattstudios.mfjda.base;

import me.mattstudios.mfjda.base.components.MessageResolver;
import me.mattstudios.mfjda.exceptions.MessageNotFoundException;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

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
     * Registers a new message with the given messageId, and uses the given messageResolver
     * to resolve it when sendMessage is executed.
     *
     * @param messageId       The message ID to be set.
     * @param messageResolver The message resolver function.
     */
    public void register(final @NotNull String messageId, final @NotNull MessageResolver messageResolver) {
        messages.put(messageId, messageResolver);
    }

    /**
     * Checks if this message handler contains the message ID specified
     *
     * @param messageId the ID to check
     * @return true if the ID was not null (in the list), false if it was null
     *         (not in the list)
     */
    boolean hasId(final String messageId) {
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
//        if (messageResolver == null) throw new MfException("The message ID " + messageId + " does not exist!");
        if (messageResolver == null) throw new MessageNotFoundException(messageId);
        messageResolver.resolve(message);
    }

}
