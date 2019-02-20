package com.jesus_crie.faerun.iohandler;

import com.jesus_crie.faerun.model.Player;
import com.jesus_crie.faerun.model.board.Board;
import com.jesus_crie.faerun.model.board.BoardCell;
import com.jesus_crie.faerun.model.board.BoardSettings;
import com.jesus_crie.faerun.model.board.Castle;
import com.jesus_crie.faerun.model.warrior.*;

import javax.annotation.Nonnull;
import java.io.PrintStream;
import java.util.*;

public class ConsoleHandler implements InputHandler, OutputHandler {

    private final Scanner in = new Scanner(System.in);
    private final PrintStream out = System.out;
    private final FightOutputHandler fightHandler;

    public ConsoleHandler(@Nonnull final FightOutputHandler fightHandler) {
        this.fightHandler = fightHandler;
    }

    /*
     *      InputHandler
     */

    @Override
    public Player[] providePlayers() {
        // TODO net players
        // Return 2 arbitrary players for now

        return new Player[]{
                new Player("Lucas", Player.Side.RIGHT),
                new Player("Jojo", Player.Side.LEFT)
        };
    }

    @Override
    public BoardSettings provideSettings() {
        // TODO ask settings
        // Return an arbitrary setting for now
        return new BoardSettings(10, 1, 3, 5, 1);
    }

    @Override
    public Map<Class<? extends Warrior>, Integer> provideQueue() {
        final HashMap<Class<? extends Warrior>, Integer> map = new HashMap<>();

        out.println("TRAINING TIME !");

        out.print("How many dwarfs ? ");
        int am = askInt();
        if (am > 0) map.put(Dwarf.class, am);

        out.print("How many dwarf chiefs ? ");
        am = askInt();
        if (am > 0) map.put(DwarfChief.class, am);

        out.print("How many elves ? ");
        am = askInt();
        if (am > 0) map.put(Elf.class, am);

        out.print("How many elf chiefs ? ");
        am = askInt();
        if (am > 0) map.put(ElfChief.class, am);

        out.println("TRAINING PROMPT DONE\n");

        return map;
    }

    private int askInt() {
        try {
            return in.nextInt();
        } catch (InputMismatchException e) {
            return 0;
        }
    }

    /*
     *      OutputHandler
     */

    @Override
    public FightOutputHandler getFightOutputHandler() {
        return fightHandler;
    }

    @Override
    public void displayNewRound(@Nonnull final Player player, final int roundNumber) {
        out.printf("\n\n--- Round %d - Player: %s [%s]\n", roundNumber, player.getPseudo(), player.getSide());
    }

    @Override
    public void displayBoardSettings(@Nonnull final BoardSettings settings) {
        out.println(settings);
    }

    @Override
    public void displayBoardProgression(@Nonnull final Board board) {
        float amountLeft = 0f;
        float amountRight = 0f;

        for (int cellIndex = 0; cellIndex < board.getSettings().getSize(); cellIndex++) {
            final BoardCell cell = board.getCell(cellIndex);
            if (cell.getSide() == Player.Side.LEFT) ++amountLeft;
            else if (cell.getSide() == Player.Side.RIGHT) ++amountRight;
        }

        out.printf("Left %d%% || %d%% Right\n",
                (int) (amountLeft / (float) board.getSettings().getSize()) * 100,
                (int) (amountRight / (float) board.getSettings().getSize()) * 100);
    }

    @Override
    public void displayBoard(@Nonnull final Board board) {
        out.print("Castle LEFT ");

        for (int cellIndex = 0; cellIndex < board.getSettings().getSize(); cellIndex++) {
            final BoardCell cell = board.getCell(cellIndex);
            switch (cell.getSide()) {
                case LEFT:
                    out.print("x");
                    break;
                case RIGHT:
                    out.print("o");
                    break;
                case BOTH:
                    out.print("|");
                    break;
                default:
                case EMPTY:
                    out.print("_");
                    break;
            }
        }

        out.println(" Castle RIGHT");
    }

    @Override
    public void displayCastleState(@Nonnull final Castle castle) {
        out.println("Available resources: " + castle.getResources());
        out.println("Queued warriors:");
        for (Warrior w : castle.getTrainingQueue())
            out.printf(" - %s\n", w.getClass().getSimpleName());
        out.println();
    }

    public static class ConsoleFightOutputHandler implements FightOutputHandler {

        private final PrintStream out = System.out;

        @Override
        public void displayLogStart(@Nonnull final BoardCell cell) {
            out.println("-- Fight in cell " + cell.getPosition() + " --");
        }

        @Override
        public void displayLogEnd(@Nonnull final BoardCell cell) {
            out.println("-- Fight ended --");
            out.println();
        }

        @Override
        public void displayWarriors(@Nonnull final List<Warrior> left, @Nonnull final List<Warrior> right) {
            printWarriors(left);
            out.println("VS");
            printWarriors(right);
            out.println();
        }

        private void printWarriors(List<Warrior> ws) {
            for (int i = 0; i < ws.size(); i++) {
                final Warrior warrior = ws.get(i);
                out.printf("#%d %s [%d HP] ", i, warrior.getClass().getSimpleName(), warrior.getHealth());
            }
            out.println();
        }

        @Override
        public void displayLogHit(@Nonnull final Warrior attacker, @Nonnull final Warrior defender, final int damage) {
            out.printf("%s ⚔️ %s -%dHP\n",
                    attacker.getClass().getSimpleName(), defender.getClass().getSimpleName(), damage);
        }

        @Override
        public void displayLogDead(@Nonnull final Warrior warrior) {
            out.printf("[%s] A %s just died, RIP\n",
                    warrior.getOwner().getSide(), warrior.getClass().getSimpleName());
        }
    }
}
