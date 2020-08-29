package me.mattstudios.mfjda.base;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

/**
 * The base class for all commands in the framework
 * All commands must extend this class to be able to be registered.
 *
 * This class merely serves to provide you with access to the {@link JDA jda}
 * instance you provided from within the command, and access to the original
 * {@link Message message} that was created from the message that JDA received
 * from Discord.
 *
 * @author Mateus Moreira
 * @since 1.0
 */
public abstract class CommandBase {

    private JDA jda;

    /**
     * The message received for the command
     *
     * e.g. the message sent by the person executing
     * the command
     */
    private Message message;

    void setJda(final JDA jda) {
        this.jda = jda;
    }

    void setMessage(final Message message) {
        this.message = message;
    }

    public @NotNull Message getMessage() {
        return message;
    }

    public @NotNull JDA getJda() {
        return jda;
    }

}
