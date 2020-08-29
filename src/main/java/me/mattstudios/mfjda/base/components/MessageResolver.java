package me.mattstudios.mfjda.base.components;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface MessageResolver {

    /**
     * Resolves messages and executes the code registered in it.
     *
     * @param message The message that was typed
     */
    void resolve(final @NotNull Message message);
}
