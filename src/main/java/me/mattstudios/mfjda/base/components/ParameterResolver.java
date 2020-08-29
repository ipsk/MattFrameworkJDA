package me.mattstudios.mfjda.base.components;

import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ParameterResolver {

    /**
     * Resolves the type of class and returns the function registered.
     *
     * @param argument The object to be tested.
     * @param guild The guild that it's coming from.
     * @return The result of the function.
     */
    @NotNull TypeResult resolve(final @NotNull Object argument, final @NotNull Guild guild);
}
