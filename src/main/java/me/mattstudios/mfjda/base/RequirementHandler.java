package me.mattstudios.mfjda.base;

import me.mattstudios.mfjda.base.components.RequirementResolver;
import net.dv8tion.jda.api.entities.Member;

import java.util.HashMap;
import java.util.Map;

public final class RequirementHandler {

    // The map of registered requirements.
    private final Map<String, RequirementResolver> registeredRequirements = new HashMap<>();

    // Registers the new requirements
    public void register(final String id, final RequirementResolver requirementResolver) {
        registeredRequirements.put(id, requirementResolver);
    }

    // Resolves the registered requirements
    boolean getResolvedResult(final String id, final Member member) {
        final RequirementResolver requirementResolver = registeredRequirements.get(id);
        return requirementResolver != null && requirementResolver.resolve(member);
    }

    // Checks if the id is registered
    boolean isRegistered(final String id) {
        return registeredRequirements.get(id) != null;
    }

}
