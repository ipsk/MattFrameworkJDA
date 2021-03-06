package me.mattstudios.mfjda.base;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class CommandData {

    private final CommandBase commandBase;
    private final Method method;

    private boolean isDefault = false;
    private boolean hasOptional = false;
    private boolean shouldDelete = false;

    private String requirement;
    private int lowerLimit = -1;
    private int upperLimit = -1;

    // The list with the other parameters.
    private final List<Class<?>> params = new ArrayList<>();

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

    public List<Class<?>> getParams() {
        return params;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(final boolean aDefault) {
        isDefault = aDefault;
    }

    public void addParameter(final Class<?> parameter) {
        params.add(parameter);
    }

    public boolean hasOptional() {
        return hasOptional;
    }

    public void setOptional(final boolean hasOptional) {
        this.hasOptional = hasOptional;
    }

    public boolean shouldDelete() {
        return shouldDelete;
    }

    public void setShouldDelete(final boolean shouldDelete) {
        this.shouldDelete = shouldDelete;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(final String requirement) {
        this.requirement = requirement;
    }

    public int getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(final int lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public int getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(final int upperLimit) {
        this.upperLimit = upperLimit;
    }
}
