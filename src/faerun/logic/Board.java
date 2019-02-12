package faerun.logic;

import faerun.game.Player;
import faerun.warrior.Warrior;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Board {

    /**
     * Define the available formats for the board and their characteristics.
     */
    public enum BoardFormat {
        SMALL(5, 2, new int[]{0, 4}),
        MEDIUM(10, 2, new int[]{0, 9}),
        LARGE(15, 2, new int[]{0, 14}),
        INSANE(100, 4, new int[]{0, 33, 66, 99});

        private final int size;

        private final int castleAmount;
        private final int[] castlePositions;

        BoardFormat(final int size, final int castleAmount, final int[] castlePositions) {
            this.size = size;
            this.castleAmount = castleAmount;
            this.castlePositions = castlePositions;
        }

        /**
         * @return The size of this format.
         */
        public int getSize() {
            return size;
        }

        /**
         * @return The amount of castle of this format.
         */
        public int getCastleAmount() {
            return castleAmount;
        }

        /**
         * @return The position of the castles
         */
        public int[] getCastlePositions() {
            return castlePositions;
        }}

    /**
     * The board.
     */
    private final List<BoardCell> board;

    private final BoardFormat format;
    /**
     * The castles.
     */
    private final Map<Player, Castle> castles = new HashMap<>();

    public Board(final Player... players) {
        this(BoardFormat.MEDIUM, players);
    }

    /**
     * @param format  - The format of the board to use.
     * @param players - The players present on this board.
     */
    public Board(final BoardFormat format, final Player... players) {
        if (players.length != format.size)
            throw new IllegalArgumentException("Incorrect amount of player provided regarding the format !");

        // Create board
        this.format = format;
        board = Arrays.asList(new BoardCell[format.getSize()]);
        board.replaceAll(c -> c == null ? new BoardCell() : c);

        // Generate castles
        for (int i = 0; i < players.length; i++)
            castles.put(players[i], new Castle(format.castlePositions[i]));
    }

    /**
     * @return A view of the board.
     */
    public List<BoardCell> getBoard() {
        return board;
    }

    /**
     * Retrieve the castle of a player.
     *
     * @param player - The owner of the requested castle.
     * @return The castle of the player if it exists.
     */
    public Castle getCastle(final Player player) {
        return castles.get(player);
    }

    /**
     * Check if a castle is at this position.
     *
     * @param position - The position to search
     * @return True if a castle is at this position, otherwise false.
     */
    public boolean isCastle(final int position) {
        return IntStream.of(format.castlePositions).anyMatch(i -> i == position);
    }

    /**
     * Determine the deploy points of a castle.
     * This assume that there are no castle right next to each other.
     *
     * @param castle - The castle to use.
     * @return An array of size 1 or 2 of the position of the deploy points.
     */
    public int[] getCastleDeployPoints(final Castle castle) {
        if (castle.getPosition() == 0)
            return new int[]{1};
        else if (castle.getPosition() == format.getSize() - 1)
            return new int[]{castle.getPosition() - 1};
        else
            return new int[]{castle.getPosition() - 1, castle.getPosition() + 1};
    }

    /**
     * Deploy the given warriors at the given position.
     *
     * @param position - The position where to deploy the warriors.
     * @param warriors - The warriors to deploy
     */
    public void deploy(final int position, final List<Warrior> warriors) {
        if (position < 0 || position >= board.size())
            throw new IllegalArgumentException("Provided position is out of bounds !");

        board.get(position).addWarriors(warriors);
        board.get(position).flush();
    }

    /**
     * Move the warriors from one cell to another.
     * When all movement have been performed, call {@link #flushCells()}.
     * The positions are assumed to be inbounds.
     *
     * @param owner       - The owner of the warriors to move.
     * @param position    - The position of the warriors to move.
     * @param destination - The destination of those warriors.
     */
    public void moveWarriors(final Player owner, final int position, final int destination) {
        final BoardCell source = board.get(position);
        final BoardCell dest = board.get(destination);

        dest.addWarriors(source.getWarriors().stream()
                .filter(w -> w.getOwner().equals(owner))
                .collect(Collectors.toList())
        );
        source.getWarriors().removeIf(w -> w.getOwner().equals(owner));
    }

    /**
     * Flush all of the cells.
     *
     * @see BoardCell#flush()
     */
    public void flushCells() {
        for (BoardCell cell : board) {
            cell.flush();
        }
    }
}
