package faerun.logic;

public class Board {

    /**
     * Define the available formats for the board
     */
    public static enum BoardFormat {
        SMALL(5),
        MEDIUM(10),
        LARGE(15);

        private int size;
        BoardFormat(final int size) {
            this.size = size;
        }

        /**
         * @return The size of this format
         */
        public int getSize() {
            return size;
        }}

    /**
     * The board
     */
    private final BoardCell[] board;

    public Board() {
        this(BoardFormat.MEDIUM);
    }

    public Board(final BoardFormat format) {
        board = new BoardCell[format.getSize()];
    }
}
