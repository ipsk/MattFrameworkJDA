package me.mattstudios.mfjda.base;

import me.mattstudios.mfjda.base.components.RequirementResolver;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class RequirementHandler {

    // The map of registered requirements.
    private final Map<String, RequirementResolver> registeredRequirements = new HashMap<>();

    /**
     * Registers a new requirement with the specified ID
     *
     * Warning: This method WILL overwrite ANY previous requirements with
     * the ID you specify! You have been warned!
     *
     * @param id                  The requirement ID
     * @param requirementResolver The resolver
     */
    public void register(final @NotNull String id, final @NotNull RequirementResolver requirementResolver) {
        registeredRequirements.put(id, requirementResolver);
    }

    /**
     * Resolves the registered requirements
     *
     * @param id     The requirement ID
     * @param member The member using the command
     * @return true if the command can be executed, or false if not
     */
    boolean getResolvedResult(final String id, final Member member) {
        final RequirementResolver requirementResolver = registeredRequirements.get(id);
        return requirementResolver != null && requirementResolver.resolve(member);
    }

    /**
     * Checks whether or not a requirement with the specified ID has been registered
     *
     * @param id The requirement ID to check
     * @return true if the requirement is not null (in the list), or false if it is null
     *         (not in the list)
     */
    boolean isRegistered(final String id) {
        return registeredRequirements.get(id) != null;
    }
}
