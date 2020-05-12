package me.mattstudios.mfjda.base;

import net.dv8tion.jda.api.JDA;

public abstract class CommandBase {

    private JDA jda;

    void setJda(final JDA jda) {
        this.jda = jda;
    }

    public JDA getJda() {
        return jda;
    }

}
