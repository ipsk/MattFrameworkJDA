package me.mattstudios.mfjda.base.components;

import net.dv8tion.jda.api.entities.Message;

import java.util.List;

@FunctionalInterface
public interface CommandExecutor {

    void execute(final List<String> args, final Message message);

}
