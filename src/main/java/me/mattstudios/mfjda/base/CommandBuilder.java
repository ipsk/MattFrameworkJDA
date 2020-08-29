package me.mattstudios.mfjda.base;

import me.mattstudios.mfjda.base.components.CommandExecutor;
import org.jetbrains.annotations.NotNull;

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
    public @NotNull CommandBuilder setPrefix(final @NotNull List<String> prefixes) {
        command.addPrefixes(prefixes);
        return this;
    }

    /**
     * Sets the command prefix
     *
     * @param prefix The prefix
     * @return Builder
     */
    @SuppressWarnings("unused") // Framework method, used by the user of this framework, not us
    public @NotNull CommandBuilder setPrefix(final @NotNull String prefix) {
        return setPrefix(Collections.singletonList(prefix));
    }

    /**
     * Sets the command names
     *
     * @param commands The list of commands
     * @return Builder
     */
    public @NotNull CommandBuilder setCommand(final @NotNull List<String> commands) {
        command.addCommands(commands);
        return this;
    }

    /**
     * Sets the command name
     *
     * @param command The command
     * @return Builder
     */
    public @NotNull CommandBuilder setCommand(final @NotNull String command) {
        return setCommand(Collections.singletonList(command));
    }

    /**
     * Sets the command executor which will handle the command later
     *
     * @param commandExecutor The executor
     * @return Builder
     */
    public @NotNull CommandBuilder setExecutor(final @NotNull CommandExecutor commandExecutor) {
        command.setCommandExecutor(commandExecutor);
        return this;
    }

    public @NotNull CommandBuilder setArgumentsLimit(final int lower, final int upper) {
        command.setLimit(lower, upper);
        return this;
    }

    public @NotNull CommandBuilder setArgumentsLimit(final int limit) {
        return setArgumentsLimit(limit, limit);
    }

    public @NotNull CommandBuilder setRequirement(final @NotNull String requirement) {
        command.setRequirement(requirement);
        return this;
    }

    public @NotNull CommandBuilder autoDelete() {
        command.setAutoDelete(true);
        return this;
    }

    public @NotNull CommandBuilder setSubCommand(final @NotNull List<String> subCommands) {
        command.addSubCommands(subCommands);
        return this;
    }

    public @NotNull CommandBuilder setSubCommand(final @NotNull String subCommand) {
        return setSubCommand(Collections.singletonList(subCommand));
    }

    /**
     * Builds the command so it can be registered
     *
     * @return A command base command
     */
    @SuppressWarnings("unused") // Framework method, used by the user of this framework, not us
    public @NotNull CommandBase build() {
        return command;
    }
}
