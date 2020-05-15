package me.mattstudios.mfjda.base;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;

public abstract class CommandBase {

    private JDA jda;
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
