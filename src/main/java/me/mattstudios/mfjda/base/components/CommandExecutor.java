package me.mattstudios.mfjda.base.components;

import net.dv8tion.jda.api.entities.Message;

import java.util.List;

@FunctionalInterface
public interface CommandExecutor {

    /**
     * Executor for the command builder
     *
     * @param args    the arguments from the command
     * @param message the message that is always passed
     */
    void execute(final List<String> args, final Message message);

}
