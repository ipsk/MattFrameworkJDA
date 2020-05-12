package me.mattstudios.mfjda.base;

import java.lang.reflect.Method;

public final class CommandData {

    private final CommandBase commandBase;
    private final Method method;

    public CommandData(final CommandBase commandBase, final Method method) {
        this.commandBase = commandBase;
        this.method = method;
    }

    public CommandBase getCommandBase() {
        return commandBase;
    }

    public Method getMethod() {
        return method;
    }

}
