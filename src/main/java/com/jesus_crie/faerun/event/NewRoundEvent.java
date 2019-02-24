package com.jesus_crie.faerun.event;

import com.jesus_crie.faerun.model.Player;

import javax.annotation.Nonnull;

/**
 * Triggered when a new round starts.
 * Contains the current cells owned by each players and the round number.
 */
public final class NewRoundEvent implements GameEvent {

    private static final long serialVersionUID = 4839576512516627686L;

    /**
     * Round number.
     */
    private final int round;

    /**
     * The name of the active player.
     */
    private final String playerSide;

    /**
     * The cells owned by the left player.
     */
    private final int[] cellsLeft;

    /**
     * The cells owned by the right player.
     */
    private final int[] cellsRight;

    /**
     * The cells where they are both players.
     */
    private final int[] fightCells;

    public NewRoundEvent(final int round,
                         @Nonnull final String playerSide,
                         final int[] cellsLeft,
                         final int[] cellsRight,
                         final int[] fightCells) {
        this.round = round;
        this.playerSide = playerSide;
        this.cellsLeft = cellsLeft;
        this.cellsRight = cellsRight;
        this.fightCells = fightCells;
    }

    public int getRound() {
        return round;
    }

    @Nonnull
    public String getPlayerName() {
        return playerSide;
    }

    public int[] getCellsLeft() {
        return cellsLeft;
    }

    public int[] getCellsRight() {
        return cellsRight;
    }

    public int[] getFightCells() {
        return fightCells;
    }
}
