package me.mattstudios.mfjda.base;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class CommandData {

    private final @NotNull CommandBase commandBase;
    private final @Nullable Method method;

    private boolean isDefault = false;
    private boolean hasOptional = false;
    private boolean shouldDelete = false;

    private @Nullable String requirement;
    private int lowerLimit = -1;
    private int upperLimit = -1;

    // The list with the other parameters.
    private final @NotNull List<Class<?>> params = new ArrayList<>();

    public CommandData(final @NotNull CommandBase commandBase, final @Nullable Method method) {
        this.commandBase = commandBase;
        this.method = method;
    }

    public @NotNull CommandBase getCommandBase() {
        return commandBase;
    }

    public @Nullable Method getMethod() {
        return method;
    }

    public @NotNull List<Class<?>> getParams() {
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

    public @Nullable String getRequirement() {
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
