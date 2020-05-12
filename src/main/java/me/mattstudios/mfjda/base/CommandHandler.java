package me.mattstudios.mfjda.base;

import me.mattstudios.mfjda.annotations.SubCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CommandHandler extends ListenerAdapter {

    private final JDA jda;
    private final String commandName;
    private final Map<String, CommandData> subCommands = new HashMap<>();
    private final List<String> prefixes = new ArrayList<>();

    public CommandHandler(final JDA jda, final CommandBase command, final String commandName, final List<String> prefixes) {
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

            // Checks if the method is public and if the class has command in it
            if (!Modifier.isPublic(method.getModifiers())) continue;
            if (!method.isAnnotationPresent(SubCommand.class)) continue;

            for (final String subCommand : method.getAnnotation(SubCommand.class).value()) {
                final CommandData commandData = new CommandData(command, method);

                subCommands.put(subCommand, commandData);
            }
        }

    }

    /**
     * Listens for the command
     */
    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        final Message message = event.getMessage();
        final List<String> arguments = Arrays.asList(message.getContentRaw().split(" "));

        if (!message.getContentRaw().startsWith(commandName)) return;

        String commandName = arguments[0].substring(1);

        final CommandData commandData = subCommands.get(commandName);

        if (commandData == null) return;

        try {
            commandData.getMethod().invoke(commandData.getCommandBase());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private boolean startsWithPrefix(final String message) {

    }

}
