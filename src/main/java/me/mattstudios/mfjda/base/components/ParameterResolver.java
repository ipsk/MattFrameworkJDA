package me.mattstudios.mfjda.base.components;

@FunctionalInterface
public interface ParameterResolver {

    /**
     * Resolves the type of class and returns the function registered.
     *
     * @param argument The object to be tested.
     * @return The result of the function.
     */
    TypeResult resolve(Object argument);

}
