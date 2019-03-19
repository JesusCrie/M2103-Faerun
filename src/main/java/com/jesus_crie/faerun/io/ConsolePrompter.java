package com.jesus_crie.faerun.io;

import com.jesus_crie.faerun.event.*;
import com.jesus_crie.faerun.model.board.BoardSettings;
import com.jesus_crie.faerun.model.warrior.*;
import com.jesus_crie.faerun.utils.Pair;

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Ask the local player through the console.
 */
public class ConsolePrompter implements Prompter {

    private final Scanner in;
    private final PrintStream out;

    public ConsolePrompter(@Nonnull final InputStream in,
                           @Nonnull final PrintStream out) {
        this.in = new Scanner(in);
        this.out = out;
    }

    public ConsolePrompter() {
        this(System.in, System.out);
    }

    @Nonnull
    @Override
    public Boolean onAskResumeGame(@Nonnull AskResumeGame e) {
        return ConsoleUtils.askYesNo(in, out, "Do you want to resume your game ?");
    }

    @Nonnull
    @Override
    public String onAskUsername(@Nonnull final AskUsernameEvent e) {
        return ConsoleUtils.ask(in, out, "What username do you want to use ?", "> ", s -> s);
    }

    @Nonnull
    @Override
    public BoardSettings onAskSettings(@Nonnull final AskSettingsEvent e) {
        out.println("Configure game settings:");

        out.print(" - Size of the board ? [0-20] ");
        final int size = ConsoleUtils.askInt(in, out, null, "", 0, 21, 10);

        out.print(" - Cost multiplier ? [1-10] ");
        final int baseCost = ConsoleUtils.askInt(in, out, null, "", 1, 11, 1);

        out.print(" - Dice amount ? [1-10] ");
        final int diceAmount = ConsoleUtils.askInt(in, out, null, "", 1, 11, 3);

        out.print(" - Initial resources ? [10-10000] ");
        final int initialResources = ConsoleUtils.askInt(in, out, null, "", 10, 10_001, 5);

        out.print(" - Resources/round ? [1-10000] ");
        final int resourcesPerRound = ConsoleUtils.askInt(in, out, null, "", 1, 10_001, 1);

        return new BoardSettings(size, baseCost, diceAmount, initialResources, resourcesPerRound);
    }

    @Nonnull
    @Override
    public Map<Class<? extends Warrior>, Integer> onAskQueue(@Nonnull final AskQueueEvent e) {
        out.println("-- Training time !");
        out.println("Available resources: " + e.getAvailableResources());

        out.println(Arrays.stream(e.getCurrentQueue())
                .map(Class::getSimpleName)
                .collect(Collectors.joining(", ", "Current queue: ", ""))
        );

        return queueMenu();
    }

    private Map<Class<? extends Warrior>, Integer> queueMenu() {
        final Map<Class<? extends Warrior>, AtomicInteger> toTrain = new HashMap<>();

        // Ask for a unit and recurse
        ConsoleUtils.createMenu(in, out, "What to you want to train ?",
                Pair.of("Dwarf", () -> {
                    final int amount = ConsoleUtils.askInt(in, out, null, "How many dwarfs ? ");
                    toTrain.putIfAbsent(Dwarf.class, new AtomicInteger());

                    toTrain.get(Dwarf.class).addAndGet(amount);
                    queueMenu();
                }),
                Pair.of("Dwarf Leader", () -> {
                    final int amount = ConsoleUtils.askInt(in, out, null, "How many dwarf leaders ? ");
                    toTrain.putIfAbsent(DwarfLeader.class, new AtomicInteger());

                    toTrain.get(DwarfLeader.class).addAndGet(amount);
                    queueMenu();
                }),
                Pair.of("Elf", () -> {
                    final int amount = ConsoleUtils.askInt(in, out, null, "How many elves ? ");
                    toTrain.putIfAbsent(Elf.class, new AtomicInteger());

                    toTrain.get(Elf.class).addAndGet(amount);
                    queueMenu();
                }),
                Pair.of("Elf Leader", () -> {
                    final int amount = ConsoleUtils.askInt(in, out, null, "How many elf leaders ? ");
                    toTrain.putIfAbsent(ElfLeader.class, new AtomicInteger());

                    toTrain.get(ElfLeader.class).addAndGet(amount);
                    queueMenu();
                }),
                Pair.of("Nothing", () -> {})
        );

        // Convert map and return
        return toTrain.entrySet().stream()
                .collect(HashMap::new,
                        (map, entry) -> map.put(entry.getKey(), entry.getValue().get()),
                        HashMap::putAll);
    }

    @Nonnull
    @Override
    public Pair<String, Integer> onAskRemoteAddress(@Nonnull final AskRemoteAddress e) {
        out.println("Remote configuration");
        final String host = ConsoleUtils.askString(in, out, null, " - Host ? ");
        final int port = ConsoleUtils.askInt(in, out, null, " - Port ? [1-65635] ", 1, 65635);

        return Pair.of(host, port);
    }
}
