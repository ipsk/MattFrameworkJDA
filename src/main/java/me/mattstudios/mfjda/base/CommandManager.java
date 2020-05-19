package me.mattstudios.mfjda.base;

import me.mattstudios.mfjda.annotations.Command;
import me.mattstudios.mfjda.annotations.Prefix;
import me.mattstudios.mfjda.base.components.MessageResolver;
import me.mattstudios.mfjda.base.components.ParameterResolver;
import me.mattstudios.mfjda.base.components.RequirementResolver;
import me.mattstudios.mfjda.exceptions.MfException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public final class CommandManager extends ListenerAdapter {

    private final JDA jda;

    private final ParameterHandler parameterHandler = new ParameterHandler();
    private final RequirementHandler requirementHandler = new RequirementHandler();
    private final MessageHandler messageHandler = new MessageHandler();

    private final Set<String> prefixes = new HashSet<>();
    private final Map<String, CommandHandler> commands = new HashMap<>();

    public CommandManager(final JDA jda) {
        this.jda = jda;

        jda.addEventListener(this);
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
            final CommandHandler commandHandler = this.commands.get(commandName);
            if (commandHandler != null) {
                commandHandler.registerSubCommands(command);
                continue;
            }

            this.prefixes.addAll(Arrays.asList(prefixes));
            this.commands.put(commandName, new CommandHandler(parameterHandler, messageHandler, requirementHandler, command, Arrays.asList(prefixes)));
        }

    }

    /**
     * Registers the new requirement
     *
     * @param id                  The requirement ID
     * @param requirementResolver The requirement resolver
     */
    public void registerRequirement(final String id, final RequirementResolver requirementResolver) {
        requirementHandler.register(id, requirementResolver);
    }

    /**
     * Registers the new parameter
     *
     * @param clss              The parameter class
     * @param parameterResolver The requirement resolver
     */
    public void registerParameter(final Class<?> clss, final ParameterResolver parameterResolver) {
        parameterHandler.register(clss, parameterResolver);
    }

    /**
     * Registers the new message
     *
     * @param id              The message ID
     * @param messageResolver The requirement resolver
     */
    public void registerMessage(final String id, final MessageResolver messageResolver) {
        messageHandler.register(id, messageResolver);
    }

    @Override
    public void onGuildMessageReceived(final @NotNull GuildMessageReceivedEvent event) {
        final Message message = event.getMessage();
        final List<String> arguments = Arrays.asList(message.getContentRaw().split(" "));

        // Checks if the message starts with the prefixes
        if (arguments.isEmpty()) return;

        // Gets the prefix being used and checks if it's command or not
        final String prefix = getPrefix(arguments.get(0));
        if (prefix == null) return;

        // Checks if the command exists
        final String commandName = arguments.get(0).replace(prefix, "");
        final CommandHandler commandHandler = commands.get(commandName);
        if (commandHandler == null) {
            message.getChannel().sendMessage("COMMAND DOESN'T EXIST").queue();
            return;
        }

        // Checks if the command given uses this prefix
        if (!commandHandler.isPrefix(prefix)) {
            message.getChannel().sendMessage("COMMAND DOESN'T EXIST").queue();
            return;
        }

        commandHandler.executeCommand(message, arguments);
    }

    /**
     * Checks if the message starts with the given prefix
     *
     * @param command The message to check
     * @return Whether or not the message starts with the prefix from the list
     */
    private String getPrefix(final String command) {
        for (String prefix : prefixes) {
            final Pattern pattern = Pattern.compile("^" + Pattern.quote(prefix) + "[\\w]");
            final Matcher matcher = pattern.matcher(command);

            if (matcher.find()) {
                return prefix;
            }
        }

        return null;
    }
}
