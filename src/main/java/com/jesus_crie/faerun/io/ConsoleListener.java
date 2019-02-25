package com.jesus_crie.faerun.io;

import com.jesus_crie.faerun.event.FightEvent;
import com.jesus_crie.faerun.event.GoodbyeEvent;
import com.jesus_crie.faerun.event.NewRoundEvent;
import com.jesus_crie.faerun.event.WelcomeEvent;
import com.jesus_crie.faerun.logic.FightEntry;
import com.jesus_crie.faerun.model.Side;
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
    public void onNewRound(@Nonnull final NewRoundEvent e) {
        // Header
        out.printf("--- Round %d | Player %s ---\n", e.getRound(), e.getPlayerName());

        // Query the cells side
        final Side[] cells = e.getCells();

        // Progression
        float amountCells = cells.length;
        float amountLeft = Arrays.stream(cells).filter(s -> s == Side.LEFT).count();
        float amountRight = Arrays.stream(cells).filter(s -> s == Side.RIGHT).count();
        out.printf("Left: %f%% || Right: %f%%\n", amountLeft / amountCells * 100f, amountRight / amountCells * 100f);

        // Board
        out.println();
        final StringBuilder boardStr = new StringBuilder();
        boardStr.append("Castle LEFT ");

        // Build cell type
        Arrays.stream(cells).map(s -> {
                    switch (s) {
                        case LEFT:
                            return 'X';
                        case RIGHT:
                            return 'O';
                        case BOTH:
                            return '|';
                        case EMPTY:
                        default:
                            return '_';
                    }
                }
        ).forEachOrdered(boardStr::append);

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

                                // Take care of hit direction
                                if (h.getAttackerSide() == e.getRecord().getAttackerSide()) {
                                    return String.format("%s ⚔ %s -%dHP",
                                            attackers[h.getAttackerIndex()].getClass().getSimpleName(),
                                            defenders[h.getDefenderIndex()].getClass().getSimpleName(),
                                            h.getDamage()
                                    );
                                } else {
                                    return String.format("%s ⚔ %s -%dHP",
                                            defenders[h.getAttackerIndex()].getClass().getSimpleName(),
                                            attackers[h.getDefenderIndex()].getClass().getSimpleName(),
                                            h.getDamage());
                                }

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

    @Override
    public void onGoodbye(@Nonnull final GoodbyeEvent e) {
        out.println("--- Game ended, winner: " + e.getWinner() + " ---");
    }
}
