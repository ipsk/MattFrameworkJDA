package me.mattstudios.mfjda.base;

import me.mattstudios.mfjda.base.components.CommandExecutor;

import java.util.Collections;
import java.util.List;

public final class CommandBuilder {

    final BuildCommand command = new BuildCommand();

    /**
     * Sets the command prefixes
     *
     * @param prefixes The list of prefixes
     * @return Builder
     */
    public CommandBuilder setPrefix(final List<String> prefixes) {
        command.addPrefixes(prefixes);
        return this;
    }

    /**
     * Sets the command prefix
     *
     * @param prefix The prefix
     * @return Builder
     */
    public CommandBuilder setPrefix(final String prefix) {
        return setPrefix(Collections.singletonList(prefix));
    }

    /**
     * Sets the command names
     *
     * @param commands The list of commands
     * @return Builder
     */
    public CommandBuilder setCommand(final List<String> commands) {
        command.addCommands(commands);
        return this;
    }

    /**
     * Sets the command name
     *
     * @param command The command
     * @return Builder
     */
    public CommandBuilder setCommand(final String command) {
        return setCommand(Collections.singletonList(command));
    }

    /**
     * Sets the command executor which will handle the command later
     *
     * @param commandExecutor The executor
     * @return Builder
     */
    public CommandBuilder setExecutor(final CommandExecutor commandExecutor) {
        command.setCommandExecutor(commandExecutor);
        return this;
    }

    public CommandBuilder setArgumentsLimit(final int lower, final int upper) {
        command.setLimit(lower, upper);
        return this;
    }

    public CommandBuilder setArgumentsLimit(final int limit) {
        return setArgumentsLimit(limit, limit);
    }

    public CommandBuilder setRequirement(final String requirement) {
        command.setRequirement(requirement);
        return this;
    }

    public CommandBuilder autoDelete() {
        command.setAutoDelete(true);
        return this;
    }

    public CommandBuilder setSubCommand(final List<String> subCommands) {
        command.addSubCommands(subCommands);
        return this;
    }

    public CommandBuilder setSubCommand(final String subCommand) {
        return setSubCommand(Collections.singletonList(subCommand));
    }

    /**
     * Builds the command so it can be registered
     *
     * @return A command base command
     */
    public CommandBase build() {
        return command;
    }

}
