package com.jesus_crie.faerun.io;

import com.jesus_crie.faerun.model.Player;
import com.jesus_crie.faerun.model.Side;
import com.jesus_crie.faerun.model.board.Board;
import com.jesus_crie.faerun.model.board.BoardCell;
import com.jesus_crie.faerun.model.board.BoardSettings;
import com.jesus_crie.faerun.model.board.Castle;
import com.jesus_crie.faerun.model.warrior.*;

import javax.annotation.Nonnull;
import java.io.PrintStream;
import java.util.*;

@Deprecated
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
    public boolean provideGamemode() {
        final String g = in.nextLine();

        // Default to local if not remote
        return g.startsWith("r");
    }

    @Override
    public String provideUsername() {
        String rawName = in.nextLine();
        rawName = rawName.replaceAll("\\s", "_")
                .replaceAll("[^a-zA-Z0-9]", "");

        return rawName;
    }

    @Override
    public void displayPromptSettings() {
        out.print("Enter settings in this order: size, baseCost, diceAmount, initResources, resources/round: ");
    }

    @Override
    public BoardSettings provideSettings() {
        return new BoardSettings(askInt(10),
                askInt(1),
                askInt(3),
                askInt(5),
                askInt(1));
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
        if (am > 0) map.put(DwarfLeader.class, am);

        out.print("How many elves ? ");
        am = askInt();
        if (am > 0) map.put(Elf.class, am);

        out.print("How many elf chiefs ? ");
        am = askInt();
        if (am > 0) map.put(ElfLeader.class, am);

        out.println("TRAINING PROMPT DONE\n");

        return map;
    }

    private int askInt() {
        return askInt(0);
    }

    private int askInt(final int defaultValue) {
        try {
            return Integer.valueOf(in.nextLine());
        } catch (NumberFormatException e) {
            return defaultValue;
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
    public void displayWelcome() {
        out.println("Welcome to Faerun battle !");
    }

    @Override
    public void displayPromptGameMode() {
        out.print("Do you want to play in local or remotely ? [L/r] ");
    }

    @Override
    public void displayPromptUsername() {
        out.print("Choose your username: ");
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
            if (cell.getSide() == Side.LEFT) ++amountLeft;
            else if (cell.getSide() == Side.RIGHT) ++amountRight;
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
