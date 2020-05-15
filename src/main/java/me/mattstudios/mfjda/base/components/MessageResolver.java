package me.mattstudios.mfjda.base.components;

import net.dv8tion.jda.api.entities.MessageChannel;

@FunctionalInterface
public interface MessageResolver {

    /**
     * Resolves messages and executes the code registered in it.
     *
     * @param channel The command sender to send the message to.
     */
    void resolve(MessageChannel channel);

}
