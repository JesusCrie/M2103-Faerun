package faerun.logic;

import faerun.game.Player;
import faerun.warrior.Warrior;

import java.util.HashMap;
import java.util.Map;

public class Board {

    /**
     * Define the available formats for the board and their characteristics.
     */
    public enum BoardFormat {
        SMALL(5, 2, new int[]{0, 4}),
        MEDIUM(10, 2, new int[]{0, 9}),
        LARGE(15, 2, new int[]{0, 14});

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
    private final BoardCell[] board;
    private final BoardFormat format;

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
            throw new IllegalArgumentException("Too many players were provided !");

        // Create board
        this.format = format;
        board = new BoardCell[format.getSize()];
        for (int i = 0; i < board.length; i++)
            board[i] = new BoardCell();

        // Generate castles
        for (int i = 0; i < players.length; i++)
            castles.put(players[i], new Castle(format.castlePositions[i]));
    }

    /**
     * Try to place a warrior outside of the castle if there is room to do so.
     *
     * @param player  - The current player.
     * @param warrior - The warrior to place outside.
     * @return True if the warrior was successfully placed outside.
     */
    public boolean tryPollWarrior(final Player player, final Warrior warrior) {
        final int castlePosition = castles.get(player).getPosition();

        // Check adjacent cells
        if (castlePosition > 0 && board[castlePosition - 1].isEmpty()) {
            board[castlePosition - 1].setWarrior(warrior);
            return true;
        } else if (castlePosition < format.getSize() - 1 && board[castlePosition + 1].isEmpty()) {
            board[castlePosition + 1].setWarrior(warrior);
            return true;
        }

        return false;
    }
}
