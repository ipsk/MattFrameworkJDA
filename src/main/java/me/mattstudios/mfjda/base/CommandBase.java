package me.mattstudios.mfjda.base;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;

/**
 * The base class for all commands in the framework
 * All commands extend this class
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

    public Message getMessage() {
        return message;
    }

    public JDA getJda() {
        return jda;
    }

}
