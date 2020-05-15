package me.mattstudios.mfjda.base;

import me.mattstudios.mfjda.annotations.Command;
import me.mattstudios.mfjda.annotations.Prefix;
import me.mattstudios.mfjda.exceptions.MfException;
import net.dv8tion.jda.api.JDA;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class CommandManager {

    private final ParameterHandler parameterHandler = new ParameterHandler();
    private final MessageHandler messageHandler = new MessageHandler();
    private final JDA jda;
    private final Map<String, CommandHandler> commands = new HashMap<>();

    public CommandManager(final JDA jda) {
        this.jda = jda;
    }

    public void register(final CommandBase command) {
        // Injects JDA into the command class
        command.setJda(jda);

        final Class<?> commandClass = command.getClass();

        // Checks for the Prefix annotation
        if (!commandClass.isAnnotationPresent(Prefix.class)) {
            throw new MfException("Class " + commandClass.getName() + " needs to be annotated with @Prefix!");
        }

        // Checks for the Prefix annotation
        if (!commandClass.isAnnotationPresent(Command.class)) {
            throw new MfException("Class " + commandClass.getName() + " needs to be annotated with @Command!");
        }

        final String[] prefixes = commandClass.getAnnotation(Prefix.class).value();
        final String[] commands = commandClass.getAnnotation(Command.class).value();

        // Adds a new command for each prefix added
        for (final String commandName : commands) {
            this.commands.put(commandName, new CommandHandler(parameterHandler, messageHandler, jda, command, commandName, Arrays.asList(prefixes)));
        }

    }

}
