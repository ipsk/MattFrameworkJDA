package me.mattstudios.mfjda.base.components;

import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@FunctionalInterface
public interface CommandExecutor {

    /**
     * Executor for the command builder
     *
     * @param args    the arguments from the command
     * @param message the message that is always passed
     */
    void execute(final @NotNull List<String> args, final @NotNull Message message);
}
