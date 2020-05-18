package me.mattstudios.mfjda.base;

import me.mattstudios.mfjda.base.components.MessageResolver;
import me.mattstudios.mfjda.exceptions.MfException;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.HashMap;
import java.util.Map;

public final class MessageHandler {

    // The map with the messages to send.
    private final Map<String, MessageResolver> messages = new HashMap<>();

    // Registers all the default messages.
    MessageHandler() {
        register("cmd.no.permission", channel -> channel.sendMessage(new MessageBuilder("You don't have permission to run this command!").build()).queue());
        register("cmd.no.exists", channel -> channel.sendMessage(new MessageBuilder("That command doesn't exist!").build()).queue());
        register("cmd.wrong.usage", channel -> channel.sendMessage(new MessageBuilder("Wrong usage for command!").build()).queue());
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

    boolean hasId(String messageId) {
        return messages.get(messageId) != null;
    }

    /**
     * Sends the registered message to the command sender.
     *
     * @param messageId The message ID.
     * @param channel   The command sender to send the message to.
     */
    void sendMessage(final String messageId, final MessageChannel channel) {
        final MessageResolver messageResolver = messages.get(messageId);
        if (messageResolver == null) throw new MfException("The message ID \"" + messageId + "\" does not exist!");
        messageResolver.resolve(channel);
    }

}
