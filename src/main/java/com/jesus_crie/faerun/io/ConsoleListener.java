package com.jesus_crie.faerun.io;

import com.jesus_crie.faerun.event.FightEvent;
import com.jesus_crie.faerun.event.NewRoundEvent;
import com.jesus_crie.faerun.event.SettingsReadyEvent;
import com.jesus_crie.faerun.event.WelcomeEvent;
import com.jesus_crie.faerun.logic.FightEntry;
import com.jesus_crie.faerun.model.board.BoardSettings;
import com.jesus_crie.faerun.model.warrior.Warrior;

import javax.annotation.Nonnull;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Implementation of {@link Listener} designed to print event to the console.
 * This class remind the current {@link BoardSettings}.
 */
public class ConsoleListener implements Listener {

    /* IO */
    private final PrintStream out;

    private BoardSettings settings;

    public ConsoleListener(@Nonnull final PrintStream out) {
        this.out = out;
    }

    public ConsoleListener() {
        this(System.out);
    }

    @Override
    public void onWelcome(@Nonnull final WelcomeEvent e) {
        out.println("--- Welcome to Faerun battle ! ---");
    }

    @Override
    public void onSettingsReady(@Nonnull final SettingsReadyEvent e) {
        if (settings != null)
            settings = e.getSettings();

        out.println("Game settings:");
        out.println("- Board size: " + settings.getSize());
        out.println("- Unit cost multiplier: " + settings.getBaseCost());
        out.println("- Dices: " + settings.getDiceAmount());
        out.println("- Initial resources: " + settings.getInitialResources());
        out.println("- Resources/round: " + settings.getResourcesPerRound());
    }

    @Override
    public void onNewRound(@Nonnull final NewRoundEvent e) {
        // Header
        out.printf("--- Round %d | Player %s ---\n", e.getRound(), e.getPlayerName());

        // Progression
        float amountCells = settings.getSize();
        float amountLeft = e.getCellsLeft().length;
        float amountRight = e.getCellsRight().length;
        out.printf("Left: %f%% || Right: %f%%\n", amountLeft / amountCells * 100f, amountRight / amountCells * 100f);

        // Board
        out.println();
        final StringBuilder boardStr = new StringBuilder();
        boardStr.append("Castle LEFT ");

        // Build cell type
        final char[] cellTypes = new char[settings.getSize()];
        // Init cells
        for (int cell = 0; cell < cellTypes.length; cell++)
            cellTypes[cell] = '_';
        // Symbol left player
        for (int cell : e.getCellsLeft())
            cellTypes[cell] = 'X';
        // Symbol right player
        for (int cell : e.getCellsRight())
            cellTypes[cell] = 'O';
        // Symbol conflict
        for (int cell : e.getFightCells())
            cellTypes[cell] = '|';

        boardStr.append(cellTypes);
        boardStr.append(" Castle RIGHT");

        // Display
        out.println(boardStr.toString());
    }

    @Override
    public void onFight(@Nonnull final FightEvent e) {
        out.println("-- Fight on cell " + e.getRecord().getCellIndex());

        /* Print concurrent warriors */

        out.print(e.getRecord().getAttackerSide() + ": ");
        out.println(Arrays.stream(e.getRecord().getAttackers())
                .map(w -> String.format("%s [%d HP]", w.getClass().getSimpleName(), w.getHealth()))
                .collect(Collectors.joining(" "))
        );

        out.println("\tVS");

        out.print(e.getRecord().getDefenderSide() + ": ");
        out.println(Arrays.stream(e.getRecord().getDefenders())
                .map(w -> String.format("%s [%d HP]", w.getClass().getSimpleName(), w.getHealth()))
                .collect(Collectors.joining(" "))
        );


        /* Fight entries */

        out.println();
        final Warrior[] attackers = e.getRecord().getAttackers();
        final Warrior[] defenders = e.getRecord().getDefenders();

        // Map and print each entry
        Arrays.stream(e.getRecord().getFightEntries())
                .map(entry -> {
                            // Map hit entries
                            if (entry.isHitEntry()) {
                                final FightEntry.Hit h = entry.asHitEntry();
                                return String.format("%s ⚔ %s -%dHP",
                                        attackers[h.getAttackerIndex()].getClass().getSimpleName(),
                                        defenders[h.getDefenderIndex()].getClass().getSimpleName(),
                                        h.getDamage()
                                );

                                // Map dead entries
                            } else if (entry.isDeadEntry()) {
                                final FightEntry.Dead d = entry.asDeadEntry();
                                return String.format("[%s] A %s just died, RIP.",
                                        d.getDeadSide(),
                                        (e.getRecord().isAttacker(d.getDeadSide()) ? attackers : defenders)[d.getWarriorIndex()]
                                                .getClass().getSimpleName()
                                );
                            } else return "";
                        }
                ).forEachOrdered(out::println);


        out.println("-- Fight ended");
    }
}
