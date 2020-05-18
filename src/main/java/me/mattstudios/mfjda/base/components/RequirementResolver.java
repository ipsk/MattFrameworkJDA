package me.mattstudios.mfjda.base.components;

import net.dv8tion.jda.api.entities.Member;

@FunctionalInterface
public interface RequirementResolver {

    boolean resolve(final Member member);

}
