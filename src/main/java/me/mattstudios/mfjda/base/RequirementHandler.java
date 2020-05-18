package me.mattstudios.mfjda.base;

import me.mattstudios.mfjda.base.components.RequirementResolver;
import net.dv8tion.jda.api.entities.Member;

import java.util.HashMap;
import java.util.Map;

public final class RequirementHandler {

    // The map of registered requirements.
    private final Map<String, RequirementResolver> registeredRequirements = new HashMap<>();

    /**
     * Registers the new requirement
     *
     * @param id                  The requirement ID
     * @param requirementResolver The resolver
     */
    public void register(final String id, final RequirementResolver requirementResolver) {
        registeredRequirements.put(id, requirementResolver);
    }

    /**
     * Resolves the registered requirements
     *
     * @param id     The requirement ID
     * @param member The member using the command
     * @return True if the command can be executed and false if not
     */
    boolean getResolvedResult(final String id, final Member member) {
        final RequirementResolver requirementResolver = registeredRequirements.get(id);
        return requirementResolver != null && requirementResolver.resolve(member);
    }

    /**
     * Checks whether or not the ID is registered
     *
     * @param id The requirement ID
     * @return True if registered
     */
    boolean isRegistered(final String id) {
        return registeredRequirements.get(id) != null;
    }

}
