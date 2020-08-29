package me.mattstudios.mfjda.base;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import me.mattstudios.mfjda.base.components.ParameterResolver;
import me.mattstudios.mfjda.base.components.TypeResult;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public final class ParameterHandler {

    // The map of registered parameters.
    private final Map<Class<?>, ParameterResolver> registeredTypes = new HashMap<>();

    // Registers all the parameters;
    ParameterHandler(final @NotNull JDA jda) {
        register(Short.class, (arg, guild) -> {
            final Integer integer = Ints.tryParse(String.valueOf(arg));
            return integer == null ? new TypeResult(arg) : new TypeResult(integer.shortValue(), arg);
        });
        register(Integer.class, (arg, guild) -> new TypeResult(Ints.tryParse(String.valueOf(arg)), arg));
        register(Long.class, (arg, guild) -> new TypeResult(Longs.tryParse(String.valueOf(arg)), arg));
        register(Float.class, (arg, guild) -> new TypeResult(Floats.tryParse(String.valueOf(arg)), arg));
        register(Double.class, (arg, guild) -> new TypeResult(Doubles.tryParse(String.valueOf(arg)), arg));

        register(String.class, (arg, guild) -> arg instanceof String ? new TypeResult(arg, arg) : new TypeResult(arg));

        register(String[].class, (arg, guild) -> {
            if (arg instanceof String[]) return new TypeResult(arg, arg);
            // Will most likely never happen.
            return new TypeResult(arg);
        });

        register(Boolean.class, (arg, guild) -> new TypeResult(Boolean.valueOf(String.valueOf(arg)), arg));
        register(boolean.class, (arg, guild) -> new TypeResult(Boolean.valueOf(String.valueOf(arg)), arg));

        register(User.class, (arg, guild) -> {
            if (arg == null) return new TypeResult(null);
            final String numericArg = arg.toString().replaceAll(("[^\\d]"), "");
            return new TypeResult(jda.getUserById(numericArg), arg);
        });

        register(Member.class, (arg, guild) -> {
            if (arg == null) return new TypeResult(null);
            final String numericArg = arg.toString().replaceAll(("[^\\d]"), "");
            return new TypeResult(guild.getMemberById(numericArg), arg);
        });

    }

    /**
     * Registers the new class type of parameters and their results.
     *
     * @param clss              The class type to be added.
     * @param parameterResolver The built in method that returns the value wanted.
     */
    public void register(final @NotNull Class<?> clss, final @NotNull ParameterResolver parameterResolver) {
        registeredTypes.put(clss, parameterResolver);
    }

    /**
     * Gets a specific type result based on a class type.
     *
     * @param clss       The class to check.
     * @param object     The input object of the functional interface.
     * @param subCommand The command base class.
     * @param paramName  The parameter name from the command method.
     * @return The output object of the functional interface.
     */
    Object getTypeResult(final Class<?> clss, final Object object, final Guild guild, final CommandData subCommand, final String paramName) {
        final TypeResult result = registeredTypes.get(clss).resolve(object, guild);
        //subCommand.getCommandBase().addArgument(paramName, result.getArgumentName());

        return result.getResolvedValue();
    }

    /**
     * Checks if the class has already been registered or not.
     *
     * @param clss The class type to check.
     * @return Returns true if it contains.
     */
    boolean isRegisteredType(final Class<?> clss) {
        return registeredTypes.get(clss) != null;
    }
}
