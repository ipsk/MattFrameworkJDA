package me.mattstudios.mfjda.base;

import me.mattstudios.mfjda.annotations.Default;
import me.mattstudios.mfjda.annotations.Delete;
import me.mattstudios.mfjda.annotations.Optional;
import me.mattstudios.mfjda.annotations.Requirement;
import me.mattstudios.mfjda.annotations.SubCommand;
import me.mattstudios.mfjda.exceptions.IllegalParameterException;
import me.mattstudios.mfjda.exceptions.IllegalRequirementException;
import me.mattstudios.mfjda.exceptions.RequirementNotFoundException;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class CommandHandler {

    private final Map<String, CommandData> subCommands = new HashMap<>();
    private final List<String> prefixes = new ArrayList<>();
    private final ParameterHandler parameterHandler;
    private final MessageHandler messageHandler;
    private final RequirementHandler requirementHandler;

    public CommandHandler(final ParameterHandler parameterHandler, final MessageHandler messageHandler, final RequirementHandler requirementHandler, final CommandBase command, final List<String> prefixes) {
        this.parameterHandler = parameterHandler;
        this.messageHandler = messageHandler;
        this.requirementHandler = requirementHandler;

        this.prefixes.addAll(prefixes);
        registerSubCommands(command);
    }

    /**
     * Checks whether or not the prefix is for this command
     *
     * @param prefix The prefix to check
     * @return true if the prefix is for the command that this handler handles (in the list),
     *         false if it isn't (not in the list)
     */
    boolean isPrefix(final String prefix) {
        return prefixes.parallelStream().anyMatch(commandPrefix -> commandPrefix.equals(prefix));
    }

    /**
     * Registers the sub commands in the class
     *
     * @param command The command base given
     */
    void registerSubCommands(final CommandBase command) {

        // Command made from builder
        if (command instanceof BuildCommand) {
            registerSubCommands((BuildCommand) command);
            return;
        }

        // Iterates through all the methods in the class
        for (final Method method : command.getClass().getDeclaredMethods()) {

            // Ensures that the method is public and is a method annotated with either @Default or @SubCommand, meaning
            // we want to use it as a command method
            if ((!method.isAnnotationPresent(Default.class) && !method.isAnnotationPresent(SubCommand.class)) || !Modifier.isPublic(method.getModifiers())) {
                continue;
            }

            final CommandData commandData = new CommandData(command, method);

            for (int i = 0; i < method.getParameterTypes().length; i++) {
                final Class<?> parameter = method.getParameterTypes()[i];

                if (parameter.equals(String[].class) && i != method.getParameterTypes().length - 1) {
//                    throw new MfException("Method " + method.getName() + " in class " + command.getClass().getName() + " 'String[] args' have to be the last parameter if wants to be used!");
                    throw new IllegalParameterException(method, command.getClass(), ": 'String[] args' must be the last parameter to be used!");
                }

                if (!this.parameterHandler.isRegisteredType(parameter)) {
//                    throw new MfException("Method " + method.getName() + " in class " + command.getClass().getName() + " contains unregistered parameter types!");
                    throw new IllegalParameterException(method, command.getClass(), " contains unregistered parameter types!");
                }

                commandData.addParameter(parameter);
            }

            for (int i = 0; i < method.getParameters().length; i++) {
                final Parameter parameter = method.getParameters()[i];

                if (i != method.getParameters().length - 1 && parameter.isAnnotationPresent(Optional.class))
//                    throw new MfException("Method " + method.getName() + " in class " + command.getClass().getName() + " - Optional parameters can only be used as the last parameter of a method!");
                    throw new IllegalParameterException(method, command.getClass(), ": Optional parameters can only be used as the final parameter of a method!");


                if (parameter.isAnnotationPresent(Optional.class)) commandData.setOptional(true);
            }

            checkDefault(method, commandData);

            if (method.isAnnotationPresent(Requirement.class)) {
                final String requirementId = method.getAnnotation(Requirement.class).value();

                if (!requirementId.startsWith("#")) {
//                    throw new MfException("Method " + method.getName() + " in class " + command.getClass().getName() + " attempted to register an invalid requirement. All requirement IDs must start with #!");
                    throw new IllegalRequirementException(method, command.getClass(), " attempted to register an illegal requirement. All requirement IDs must start with '#'!");
                }

                if (!requirementHandler.isRegistered(requirementId)) {
//                    throw new MfException("Method " + method.getName() + " in class " + command.getClass().getName() + " attempted to register a requirement that does not exist!");
                    throw new RequirementNotFoundException(method, command.getClass(), " attempted to register a requirement that does not exist!");
                }

                commandData.setRequirement(requirementId);
            }

            // Checks if annotated with should delete
            checkShouldDelete(method, commandData);

            // Checks for sub commands if the current method is not a default one
            if (!commandData.isDefault() && method.isAnnotationPresent(SubCommand.class)) {
                for (final String subCommand : method.getAnnotation(SubCommand.class).value()) {
                    subCommands.put(subCommand.toLowerCase(), commandData);
                }

                continue;
            }

            // If the command is default add default command
            if (commandData.isDefault()) {
                subCommands.put("jda-default", commandData);
            }
        }

    }

    void registerSubCommands(final BuildCommand command) {
        prefixes.addAll(command.getPrefixes());

        final CommandData data = new CommandData(command, null);

        data.setLowerLimit(command.getLowerLimit());
        data.setUpperLimit(command.getUpperLimit());
        data.setRequirement(command.getRequirement());
        data.setShouldDelete(command.shouldAutoDelete());

        final List<String> subCommands = command.getSubCommands();
        if (subCommands.isEmpty()) {
            data.setDefault(true);
            this.subCommands.put("jda-default", data);
            return;
        }

        for (final String subCommand : subCommands) {
            this.subCommands.put(subCommand, data);
        }
    }

    /**
     * Executes this handler's command and it's sub commands
     *
     * @param message   The message being parsed
     * @param arguments The arguments from the message
     */
    public void executeCommand(final @NotNull Message message, final @NotNull List<String> arguments) {
        CommandData subCommand = getDefaultSubCommand();

        // Checks if it should be a default command or a sub command
        String commandArg = "";
        if (arguments.size() > 1) commandArg = arguments.get(1).toLowerCase();
        if (subCommand == null || subCommands.containsKey(commandArg)) subCommand = subCommands.get(commandArg);

        // Ensures that the command is a valid command that exists, or sends the usage message if it isn't
        if (subCommand == null) {
            sendWrongUsage(message);
            return;
        }

        // Ensures that the sub command is a valid sub command, or sends the usage message if it isn't
        if (subCommand.isDefault() && subCommand.getParams().isEmpty() && arguments.size() > 1) {
            sendWrongUsage(message);
            return;
        }

        final String requirementId = subCommand.getRequirement();
        if (requirementId != null && !requirementHandler.getResolvedResult(requirementId, message.getMember())) {
            messageHandler.sendMessage("cmd.no.permission", message);
            return;
        }

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

            if (subCommand.getCommandBase() instanceof BuildCommand) {
                executeBuildCommand(subCommand, argumentsList, message);
                return;
            }

            // Checks if the method only has a sender as the parameter.
            if (subCommand.getParams().size() == 0 && argumentsList.size() == 0) {
                if (subCommand.shouldDelete()) message.delete().queue();
                method.invoke(subCommand.getCommandBase());
                return;
            }

            // Checks if it is a default type command with just sender and args.
            if (subCommand.getParams().size() == 1
                && String[].class.isAssignableFrom(subCommand.getParams().get(0))) {
                if (subCommand.shouldDelete()) message.delete().queue();
                method.invoke(subCommand.getCommandBase(), (Object) argumentsList.toArray(new String[0]));
                return;
            }

            // Checks for correct command usage.
            if (subCommand.getParams().size() != argumentsList.size() && !subCommand.hasOptional()) {
                if (!subCommand.isDefault() && subCommand.getParams().size() == 0) {
                    sendWrongUsage(message);
                    return;
                }

                if (!String[].class.isAssignableFrom(subCommand.getParams().get(subCommand.getParams().size() - 1))) {
                    sendWrongUsage(message);
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

                    if (argumentsList.size() > subCommand.getParams().size()) {
                        sendWrongUsage(message);
                        return;
                    }

                    if (argumentsList.size() < subCommand.getParams().size() - 1) {
                        sendWrongUsage(message);
                        return;
                    }

                    if (argumentsList.size() < subCommand.getParams().size()) argumentsList.add(null);

                }

                // checks if the parameters and arguments are valid
                if (subCommand.getParams().size() > argumentsList.size()) {
                    sendWrongUsage(message);
                    return;
                }


                Object argument = argumentsList.get(i);

                // Checks for String[] args.
                if (parameter.equals(String[].class)) {
                    String[] args = new String[argumentsList.size() - i];

                    for (int j = 0; j < args.length; j++) {
                        args[j] = argumentsList.get(i + j);
                    }

                    argument = args;
                }

                final Object result = parameterHandler.getTypeResult(parameter, argument, message.getGuild(), subCommand, subCommand.getParams().get(i).getName());
                invokeParams.add(result);
            }

            if (subCommand.shouldDelete()) message.delete().queue();

            // Calls the command method method.
            method.invoke(subCommand.getCommandBase(), invokeParams.toArray());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void executeBuildCommand(final CommandData subCommand, final List<String> arguments, final Message message) {
        final BuildCommand buildCommand = (BuildCommand) subCommand.getCommandBase();

        final int upperLimit = subCommand.getUpperLimit();
        final int lowerLimit = subCommand.getLowerLimit();
        final int argumentSize = arguments.size();

        if ((upperLimit != -1 && argumentSize > upperLimit) || (lowerLimit != -1 && argumentSize < lowerLimit)) {
            sendWrongUsage(message);
            return;
        }

        if (subCommand.shouldDelete()) message.delete().queue();
        buildCommand.execute(arguments, message);
    }

    /**
     * Gets the default command
     *
     * @return The default command or null
     */
    private CommandData getDefaultSubCommand() {
        return subCommands.getOrDefault("jda-default", null);
    }

    /**
     * Checks if the method is default or not
     *
     * @param method      The method
     * @param commandData The command data
     */
    private void checkDefault(final Method method, final CommandData commandData) {
        if (method.getAnnotation(Default.class) != null) {
            commandData.setDefault(true);
        }
    }

    /**
     * Checks if the method has the delete annotation
     *
     * @param method      The method
     * @param commandData The command data
     */
    private void checkShouldDelete(final Method method, final CommandData commandData) {
        if (method.isAnnotationPresent(Delete.class)) {
            commandData.setShouldDelete(true);
        }
    }

    /**
     * Sends the wrong message to the sender
     *
     * @param message    The message that is being sent
     */
    private void sendWrongUsage(final Message message) {
        messageHandler.sendMessage("cmd.wrong.usage", message);
    }
}
