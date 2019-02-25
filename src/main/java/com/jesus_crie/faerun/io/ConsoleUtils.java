package com.jesus_crie.faerun.io;

import com.jesus_crie.faerun.utils.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.function.Function;

public class ConsoleUtils {

    /**
     * Ask for an object, map it from the user input and return it.
     *
     * @param in     - The input where to read from.
     * @param out    - The output where to print to.
     * @param title  - The title printed before the prompt.
     * @param prompt - The prompt, redisplayed each time the input is wrong.
     * @param mapper - Map the string supplied by the user to the target object type.
     * @param <T>    - The type to ask to the user.
     * @return The object mapped from the input.
     */
    @Nonnull
    public static <T> T ask(@Nonnull final Scanner in, @Nonnull final PrintStream out,
                            @Nullable final String title, @Nonnull final String prompt,
                            @Nonnull final Function<String, T> mapper) {

        if (title != null)
            out.println(title);

        T parsed = null;
        do {
            try {
                out.print(prompt);

                parsed = mapper.apply(in.nextLine());
            } catch (Exception e) {
                out.println(); // Jump line to prevent spam
            }
        } while (parsed == null);

        return parsed;
    }

    /**
     * Ask for a string and returns it.
     *
     * @param in     - The input where to read from.
     * @param out    - The output where to print to.
     * @param title  - The title printed before the prompt.
     * @param prompt - The prompt, redisplayed each time the input is wrong.
     * @return The string provided by the user.
     */
    @Nonnull
    public static String askString(@Nonnull final Scanner in, @Nonnull final PrintStream out,
                                   @Nullable final String title, @Nonnull final String prompt) {
        return ask(in, out, title, prompt, s -> s);
    }

    /**
     * Ask for any integer number.
     *
     * @param in     - The input where to read from.
     * @param out    - The output where to print to.
     * @param title  - The title printed before the prompt.
     * @param prompt - The prompt.
     * @return The integer supplied by the user.
     */
    public static int askInt(@Nonnull final Scanner in, @Nonnull final PrintStream out,
                             @Nullable final String title, @Nonnull final String prompt) {
        return ask(in, out, title, prompt, Integer::parseInt);
    }

    /**
     * Ask for an integer between min (inclusive) and max (exclusive).
     *
     * @param in     - The input where to read from.
     * @param out    - The output where to print to.
     * @param title  - The title printed before the prompt.
     * @param prompt - The prompt.
     * @param min    - Minimum of the number, inclusive.
     * @param max    - Maximum of the number, exclusive.
     * @return An int between min and max.
     */
    public static int askInt(@Nonnull final Scanner in, @Nonnull final PrintStream out,
                             @Nullable final String title, @Nonnull final String prompt,
                             final int min, final int max) {
        return ask(in, out, title, prompt,
                s -> {
                    final int i = Integer.parseInt(s);
                    if (i < min || i >= max)
                        return null;
                    else return i;
                }
        );
    }

    /**
     * Ask for an integer between min (inclusive) and max (exclusive).
     * If the input is errored, returns the default value.
     *
     * @param in     - The input where to read from.
     * @param out    - The output where to print to.
     * @param title  - The title printed before the prompt.
     * @param prompt - The prompt.
     * @param min    - Minimum of the number, inclusive.
     * @param max    - Maximum of the number, exclusive.
     * @param def    - The default value.
     * @return An integer between min and max or the default value.
     */
    public static int askInt(@Nonnull final Scanner in, @Nonnull final PrintStream out,
                             @Nullable final String title, @Nonnull final String prompt,
                             final int min, final int max, final int def) {
        return ask(in, out, title, prompt,
                s -> {
                    try {
                        final int i = Integer.parseInt(s);
                        if (i < min || i >= max)
                            return def;
                        else return i;
                    } catch (NumberFormatException e) {
                        return def;
                    }
                }
        );
    }

    /**
     * Create a menu and ask the user to choose an action and execute the associated {@link Runnable}.
     *
     * @param in      - The input where to read from.
     * @param out     - The output where to print to.
     * @param title   - The title printed before the menu.
     * @param options - The options, the name of the option associated with its {@link Runnable}.
     */
    @SafeVarargs
    public static void createMenu(@Nonnull final Scanner in, @Nonnull final PrintStream out,
                                  @Nonnull final String title,
                                  @Nonnull final Pair<String, Runnable>... options) {

        // Build title
        final StringBuilder titleB = new StringBuilder(title)
                .append("\n");

        for (int optI = 1; optI <= options.length; optI++) {
            titleB.append("\t")
                    .append(optI).append(". ")
                    .append(options[optI - 1].getLeft())
                    .append("\n");
        }

        // Ask what option to choose
        final int actionIndex = askInt(in, out, titleB.toString(), "> ",
                1, options.length + 1);

        // Execute the action.
        options[actionIndex - 1].getRight().run();
    }
}
