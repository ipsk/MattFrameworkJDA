package me.mattstudios.mfjda.base;

import me.mattstudios.mfjda.base.components.CommandExecutor;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.List;

final class BuildCommand extends CommandBase {

    private final List<String> prefixes = new ArrayList<>();
    private final List<String> commands = new ArrayList<>();
    private final List<String> subCommand = new ArrayList<>();

    private CommandExecutor commandExecutor;
    private boolean autoDelete = false;
    private int lowerLimit = -1;
    private int upperLimit = -1;
    private String requirement;

    void addPrefixes(final List<String> prefixes) {
        this.prefixes.addAll(prefixes);
    }

    void addCommands(final List<String> commands) {
        this.commands.addAll(commands);
    }

    void addSubCommands(final List<String> subCommand) {
        this.subCommand.addAll(subCommand);
    }

    List<String> getPrefixes() {
        return prefixes;
    }

    List<String> getCommands() {
        return commands;
    }

    List<String> getSubCommands() {
        return subCommand;
    }

    boolean shouldAutoDelete() {
        return autoDelete;
    }

    void setAutoDelete(final boolean autoDelete) {
        this.autoDelete = autoDelete;
    }

    public int getLowerLimit() {
        return lowerLimit;
    }

    public int getUpperLimit() {
        return upperLimit;
    }

    void setLimit(final int lowerLimit, final int upperLimit) {
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
    }

    String getRequirement() {
        return requirement;
    }

    void setRequirement(final String requirement) {
        this.requirement = requirement;
    }

    void setCommandExecutor(final CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    void execute(final List<String> args, final Message message) {
        if (commandExecutor == null) return;
        commandExecutor.execute(args, message);
    }
}
