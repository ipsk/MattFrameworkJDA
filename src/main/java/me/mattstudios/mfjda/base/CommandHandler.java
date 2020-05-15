package me.mattstudios.mfjda.base;

import me.mattstudios.mfjda.annotations.Default;
import me.mattstudios.mfjda.annotations.Optional;
import me.mattstudios.mfjda.annotations.SubCommand;
import me.mattstudios.mfjda.exceptions.MfException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class CommandHandler extends ListenerAdapter {

    private final JDA jda;
    private final String commandName;
    private final Map<String, CommandData> subCommands = new HashMap<>();
    private final List<String> prefixes = new ArrayList<>();
    private final ParameterHandler parameterHandler;
    private final MessageHandler messageHandler;

    public CommandHandler(final ParameterHandler parameterHandler, final MessageHandler messageHandler, final JDA jda, final CommandBase command, final String commandName, final List<String> prefixes) {
        this.parameterHandler = parameterHandler;
        this.messageHandler = messageHandler;
        this.jda = jda;
        this.commandName = commandName;
        this.prefixes.addAll(prefixes);

        jda.addEventListener(this);

        registerSubCommands(command);
    }

    /**
     * Registers the sub commands in the class
     *
     * @param command The command base given
     */
    private void registerSubCommands(final CommandBase command) {

        // Iterates through all the methods in the class
        for (final Method method : command.getClass().getDeclaredMethods()) {

            // Checks if the method is public
            if ((!method.isAnnotationPresent(Default.class) && !method.isAnnotationPresent(SubCommand.class)) || !Modifier.isPublic(method.getModifiers())) {
                continue;
            }

            final CommandData commandData = new CommandData(command, method);

            for (int i = 0; i < method.getParameterTypes().length; i++) {
                final Class<?> parameter = method.getParameterTypes()[i];

                if (parameter.equals(String[].class) && i != method.getParameterTypes().length - 1) {
                    throw new MfException("Method " + method.getName() + " in class " + command.getClass().getName() + " 'String[] args' have to be the last parameter if wants to be used!");
                }

                if (!this.parameterHandler.isRegisteredType(parameter)) {
                    throw new MfException("Method " + method.getName() + " in class " + command.getClass().getName() + " contains unregistered parameter types!");
                }

                commandData.addParameter(parameter);
            }

            for (int i = 0; i < method.getParameters().length; i++) {
                final Parameter parameter = method.getParameters()[i];

                if (i != method.getParameters().length - 1 && parameter.isAnnotationPresent(Optional.class))
                    throw new MfException("Method " + method.getName() + " in class " + command.getClass().getName() + " - Optional parameters can only be used as the last parameter of a method!");


                if (parameter.isAnnotationPresent(Optional.class)) commandData.setOptional(true);
            }

            // Checks for the default command
            if (method.isAnnotationPresent(Default.class)) {
                commandData.setDefault(true);
            }

            // Checks for sub commands if the current method is not a default one
            if (!commandData.isDefault() && method.isAnnotationPresent(SubCommand.class)) {
                for (final String subCommand : method.getAnnotation(SubCommand.class).value()) {
                    subCommands.put(subCommand.toLowerCase(), commandData);
                }

                return;
            }

            // If the command is default add default command
            if (commandData.isDefault()) {
                subCommands.put("jda-default", commandData);
            }
        }

    }

    /**
     * Listens for the command
     */
    @Override
    public void onGuildMessageReceived(final GuildMessageReceivedEvent event) {
        final Message message = event.getMessage();

        final List<String> arguments = Arrays.asList(message.getContentRaw().split(" "));

        // Checks if the message starts with the prefixes
        if (arguments.isEmpty()) return;

        // Checks if the command entered is the correct one
        if (!isCommand(arguments.get(0))) return;

        CommandData subCommand = getDefaultSubCommand();

        // Checks if it should be a default command or a sub command
        String commandArg = "";
        if (arguments.size() > 1) commandArg = arguments.get(1).toLowerCase();
        if (subCommand == null || subCommands.containsKey(commandArg)) subCommand = subCommands.get(commandArg);

        // TODO wrong usage
        if (subCommand == null) return;

        subCommand.getCommandBase().setMessage(message);
        execute(subCommand, arguments, message);
    }

    private void execute(final CommandData subCommand, final List<String> arguments, final Message message) {
        try {

            final Method method = subCommand.getMethod();
            // Checks if it the command is default and remove the sub command argument one if it is not.
            final List<String> argumentsList = new LinkedList<>(arguments);

            if (argumentsList.size() > 0) argumentsList.remove(0);
            if (!subCommand.isDefault()) argumentsList.remove(0);

            // Check if the method only has a sender as parameter.
            if (subCommand.getParams().size() == 0 && argumentsList.size() == 0) {
                method.invoke(subCommand.getCommandBase());
                return;
            }

            // Checks if it is a default type command with just sender and args.
            if (subCommand.getParams().size() == 1
                && String[].class.isAssignableFrom(subCommand.getParams().get(0))) {
                method.invoke(subCommand.getCommandBase(), arguments);
                return;
            }

            // Checks for correct command usage.
            if (subCommand.getParams().size() != argumentsList.size() && !subCommand.hasOptional()) {

                if (!subCommand.isDefault() && subCommand.getParams().size() == 0) {
                    messageHandler.sendMessage("cmd.wrong.usage", message.getChannel());
                    return;
                }

                if (!String[].class.isAssignableFrom(subCommand.getParams().get(subCommand.getParams().size() - 1))) {
                    messageHandler.sendMessage("cmd.wrong.usage", message.getChannel());
                    return;
                }

            }

            // Creates a list of the params to send.
            final List<Object> invokeParams = new ArrayList<>();

            // Iterates through all the parameters to check them.
            for (int i = 0; i < subCommand.getParams().size(); i++) {
                final Class<?> parameter = subCommand.getParams().get(i);

                // Checks for optional parameter.
                if (subCommand.hasOptional()) {

                    if (argumentsList.size() > subCommand.getParams().size()) return;

                    if (argumentsList.size() < subCommand.getParams().size() - 1) return;

                    if (argumentsList.size() < subCommand.getParams().size()) argumentsList.add(null);

                }

                // checks if the parameters and arguments are valid
                if (subCommand.getParams().size() > argumentsList.size())
                    // TODO WRONG USAGE
                    return;


                Object argument = argumentsList.get(i);

                // Checks for String[] args.
                if (parameter.equals(String[].class)) {
                    String[] args = new String[argumentsList.size() - i];

                    for (int j = 0; j < args.length; j++) {
                        args[j] = argumentsList.get(i + j);
                    }

                    argument = args;
                }

                final Object result = parameterHandler.getTypeResult(parameter, argument, subCommand, subCommand.getParams().get(i).getName());
                invokeParams.add(result);
            }

            // Calls the command method method.
            method.invoke(subCommand.getCommandBase(), invokeParams.toArray());

        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the message starts with the given prefix
     *
     * @param command The message to check
     * @return Whether or not the message starts with the prefix from the list
     */
    private boolean isCommand(final String command) {
        for (String prefix : prefixes) {
            if (command.equals(prefix + commandName)) return true;
        }

        return false;
    }

    /**
     * Gets the default command
     *
     * @return The default command or null
     */
    private CommandData getDefaultSubCommand() {
        return subCommands.getOrDefault("jda-default", null);
    }

}
