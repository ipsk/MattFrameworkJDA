package me.mattstudios.mfjda.base.components;

import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

/**
 * A functional interface used for resolving requirements, declared
 * as a functional interface for use in a lambda.
 */
@FunctionalInterface
public interface RequirementResolver {

    /**
     * Resolves the requirement for the member
     *
     * @param member The member that needs to be checked
     * @return Whether or not the member fits the requirement
     */
    boolean resolve(final @NotNull Member member);
}
