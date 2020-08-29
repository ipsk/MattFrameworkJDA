package me.mattstudios.mfjda.base.components;

import org.jetbrains.annotations.Nullable;

public final class TypeResult {

    private final @Nullable Object resolvedValue;
    private final @Nullable String argumentName;

    /**
     * Main constructor
     *
     * @param resolvedValue The resolved value
     * @param argumentName  The argument
     */
    public TypeResult(final @Nullable Object resolvedValue, final @Nullable Object argumentName) {
        this.resolvedValue = resolvedValue;
        this.argumentName = String.valueOf(argumentName);
    }

    /**
     * Secondary constructor with just the argument name
     *
     * @param argumentName The argument
     */
    public TypeResult(final @Nullable Object argumentName) {
        this(null, argumentName);
    }

    /**
     * Gets the resolved value
     *
     * @return The resolved value
     */
    public @Nullable Object getResolvedValue() {
        return resolvedValue;
    }

    /**
     * Gets the argument name
     *
     * @return The argument name
     */
    public @Nullable String getArgumentName() {
        return argumentName;
    }
}
